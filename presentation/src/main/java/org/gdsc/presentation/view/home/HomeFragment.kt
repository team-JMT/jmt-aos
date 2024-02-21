package org.gdsc.presentation.view.home

import android.graphics.PointF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
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
import org.gdsc.presentation.utils.repeatWhenUiStarted
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

    private val recommendPopularRestaurantList = listOf<RegisteredRestaurant>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setMap(savedInstanceState)
        observeState()

        val adapter = if (recommendPopularRestaurantList.isNotEmpty()) {
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

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val standardBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        standardBottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            binding.bottomSheet.background = ResourcesCompat.getDrawable(
                                resources,
                                R.color.white,
                                null
                            )
                        }

                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.bottomSheet.background = ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.bg_bottom_sheet_top_round,
                                null
                            )
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            }
        )
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
                mapMarkerAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
                    fun setMark() {
                        CoroutineScope(Dispatchers.Main).launch {
                            mapMarkerAdapter.snapshot().items.forEach { data ->
                                Marker().apply {
                                    position = LatLng(data.y, data.x)
                                    icon = OverlayImage.fromResource(R.drawable.jmt_marker)
                                    map = naverMap
                                }
                            }
                        }
                    }
                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        super.onItemRangeInserted(positionStart, itemCount)
                        setMark()
                    }

                    override fun onChanged() {
                        super.onChanged()
                        setMark()
                    }
                })
            }


            /*
            i(reason) - 움직임의 원인.
            b(animated) - 애니메이션 효과가 적용돼 움직이고 있을 경우 true, 그렇지 않을 경우 false.
             */
            naverMap.addOnCameraChangeListener { _, _ ->

                // 현재 위치(start, end)로 마커에 표시할 데이터 불러오기
                val projection: Projection = naverMap.projection

                val topLeft: LatLng = projection.fromScreenLocation(PointF(0F, 0F))
                val bottomRight: LatLng = projection.fromScreenLocation(PointF(naverMap.width.toFloat(), naverMap.height.toFloat()))

                viewModel.setStartLocation(Location(topLeft.longitude.toString(), topLeft.latitude.toString()))
                viewModel.setEndLocation(Location(bottomRight.longitude.toString(), bottomRight.latitude.toString()))
            }
        }

    }

    private fun observeState() {
        // Paging 처리 된 식당 데이터를 받아서 Marker 표시
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