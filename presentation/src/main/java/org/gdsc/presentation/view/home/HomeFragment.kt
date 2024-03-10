package org.gdsc.presentation.view.home

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.Projection
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.domain.model.Location
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.R
import org.gdsc.presentation.base.BaseViewHolder
import org.gdsc.presentation.base.ViewHolderBindListener
import org.gdsc.presentation.databinding.FragmentHomeBinding
import org.gdsc.presentation.model.FoodCategoryItem
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.utils.toDp
import org.gdsc.presentation.view.custom.JmtSpinner


@AndroidEntryPoint
class HomeFragment : Fragment(), ViewHolderBindListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val viewModel: HomeViewModel by viewModels()

    private lateinit var mapView: MapView

    // TODO : titleAdapter 문구 정리 필요
    private val recommendPopularRestaurantTitleAdapter by lazy { RecommendPopularRestaurantTitleAdapter("그룹에서 인기가 많아요") }
    private val recommendPopularRestaurantWrapperAdapter by lazy { RecommendPopularRestaurantWrapperAdapter(recommendPopularRestaurantList)}
    private val restaurantFilterAdapter by lazy { RestaurantFilterAdapter(this) }
    private val mapMarkerAdapter by lazy { MapMarkerWithRestaurantsAdatper() }
    private val emptyAdapter by lazy { EmptyAdapter() }
    private lateinit var concatAdapter: ConcatAdapter

    private val recommendPopularRestaurantList = listOf<RegisteredRestaurant>()
    private val standardBottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.bottomSheet) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setMap(savedInstanceState)
        observeState()

        concatAdapter = if (recommendPopularRestaurantList.isNotEmpty()) {
            ConcatAdapter(
                recommendPopularRestaurantTitleAdapter,
                recommendPopularRestaurantWrapperAdapter,
                restaurantFilterAdapter,
                mapMarkerAdapter
            )
        } else {
            ConcatAdapter(
                restaurantFilterAdapter,
                mapMarkerAdapter
            )
        }
        setRecyclerView()
        return binding.root
    }

    private fun setRecyclerView() {
        binding.recyclerView.adapter = concatAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        standardBottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                fun setBottomSheetRelatedView(isVisible: Boolean) {
                    binding.bottomSheetHandle.isVisible = isVisible
                    binding.bottomSheetHandleSpace.isVisible = isVisible
                    binding.mapOptionContainer.isVisible = isVisible
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            binding.groupHeader.elevation = 10F
                            setBottomSheetRelatedView(false)
                            binding.bottomSheet.background = ResourcesCompat.getDrawable(
                                resources,
                                R.color.white,
                                null
                            )
                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            binding.mapOptionContainer.isVisible = true
                        }

                        BottomSheetBehavior.STATE_DRAGGING -> {
                            binding.groupHeader.elevation = 0F
                            setBottomSheetRelatedView(true)
                            binding.bottomSheet.background = ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.bg_bottom_sheet_top_round,
                                null
                            )
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) { }
            })

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            if (standardBottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED)
                standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    override fun onViewHolderBind(holder: BaseViewHolder<out ViewBinding>, _item: Any) {
        if (holder is RestaurantFilterAdapter.RestaurantFilterViewHolder) {

            val sortSpinner = holder.itemView.findViewById<JmtSpinner>(R.id.sort_spinner)
            val foodSpinner = holder.itemView.findViewById<JmtSpinner>(R.id.food_category_spinner)
            val drinkSpinner = holder.itemView.findViewById<JmtSpinner>(R.id.drink_possibility_spinner)

            sortSpinner.setMenu(SortType.getAllText()) {
                viewModel.setSortType(SortType.values()[it.itemId])
            }

            foodSpinner.setMenu(FoodCategory.getAllText()) {
                viewModel.setFoodCategory(FoodCategory.values()[it.itemId])
            }

            drinkSpinner.setMenu(DrinkPossibility.getAllText()) {
                viewModel.setDrinkPossibility(DrinkPossibility.values()[it.itemId])
            }

            repeatWhenUiStarted {
                viewModel.sortTypeState.collectLatest {
                    sortSpinner.setMenuTitle(it.text)
                }
            }
            repeatWhenUiStarted {
                viewModel.foodCategoryState.collectLatest {
                    foodSpinner.setMenuTitle(it.text)
                }
            }
            repeatWhenUiStarted {
                viewModel.drinkPossibilityState.collectLatest {
                    drinkSpinner.setMenuTitle(it.text)
                }
            }
        }
    }

    private fun setMap(savedInstanceState: Bundle?) {
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { naverMap ->

            val markerList = mutableListOf<Marker>()
            val markerInsertCheckList= mutableListOf<Int>()

            naverMap.uiSettings.isZoomControlEnabled = false
            naverMap.uiSettings.isScaleBarEnabled = false

            repeatWhenUiStarted {
                val location = viewModel.getCurrentLocation()
                location?.let {
                    val cameraUpdate = CameraUpdate.scrollTo(LatLng(it.latitude, it.longitude))
                    naverMap.moveCamera(cameraUpdate)

                    // 데이터 확인 후 변수화
                    val zoom = CameraUpdate.zoomTo(17.0)
                    naverMap.moveCamera(zoom)
                }

                mapMarkerAdapter.addLoadStateListener { loadState ->
                    if (loadState.append.endOfPaginationReached) {
                        if (mapMarkerAdapter.itemCount < 1) {
                            concatAdapter.addAdapter(emptyAdapter)
                            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        } else {
                            concatAdapter.removeAdapter(emptyAdapter)
                        }
                        setRecyclerView()
                    }
                }

                mapMarkerAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
                    fun setMark(items: List<RegisteredRestaurant>) {
                        CoroutineScope(Dispatchers.Main).launch {
                            items.forEach { data ->
                                markerInsertCheckList.add(data.id)

                                markerList.add(Marker().apply {
                                    position = LatLng(data.y, data.x)
                                    icon = OverlayImage.fromResource(
                                        FoodCategoryItem(
                                            FoodCategory.fromName(data.category)
                                        ).getMarkerIcon()
                                    )
                                    map = naverMap
                                })
                            }
                        }
                    }
                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        super.onItemRangeInserted(positionStart, itemCount)
                        setMark(
                            mapMarkerAdapter.snapshot().items.filter {
                                it.id !in markerInsertCheckList
                            })
                    }

                    override fun onChanged() {
                        super.onChanged()
                        setMark(mapMarkerAdapter.snapshot().items)
                    }

                    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                        super.onItemRangeRemoved(positionStart, itemCount)
                        val currentItemIds = mapMarkerAdapter.snapshot().items.map {
                            it.id
                        }
                        markerInsertCheckList.filter {
                            it !in currentItemIds
                        }.forEach {
                            markerList[markerInsertCheckList.indexOf(it)].map = null
                            markerList.removeAt(markerInsertCheckList.indexOf(it))
                            markerInsertCheckList.remove(it)
                        }
                    }
                })
            }

            binding.mapRefreshBtn.setOnClickListener {
                val projection: Projection = naverMap.projection

                val topLeft: LatLng = projection.fromScreenLocation(PointF(0F, 0F))
                val bottomRight: LatLng = projection.fromScreenLocation(PointF(naverMap.width.toFloat(), naverMap.height.toFloat()))

                viewModel.setStartLocation(Location(topLeft.longitude.toString(), topLeft.latitude.toString()))
                viewModel.setEndLocation(Location(bottomRight.longitude.toString(), bottomRight.latitude.toString()))
            }
        }

    }

    private fun observeState() {
        repeatWhenUiStarted {
            viewModel.registeredPagingData().collect {
                mapMarkerAdapter.submitData(it)
            }
        }

        repeatWhenUiStarted {
            viewModel.setSortType(SortType.DISTANCE)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}