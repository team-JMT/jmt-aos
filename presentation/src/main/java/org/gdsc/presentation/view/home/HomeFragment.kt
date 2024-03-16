package org.gdsc.presentation.view.home

import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.Projection
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
import org.gdsc.domain.model.response.Group
import org.gdsc.presentation.R
import org.gdsc.presentation.base.BaseViewHolder
import org.gdsc.presentation.base.ViewHolderBindListener
import org.gdsc.presentation.databinding.ContentSheetEmptyGroupBinding
import org.gdsc.presentation.databinding.ContentSheetGroupSelectBinding
import org.gdsc.presentation.databinding.FragmentHomeBinding
import org.gdsc.presentation.model.ResultState
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.WebViewActivity
import org.gdsc.presentation.view.custom.BottomSheetDialog
import org.gdsc.presentation.view.custom.JmtSpinner


@AndroidEntryPoint
class HomeFragment : Fragment(), ViewHolderBindListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var mapView: MapView

    private val bottomSheetDialog by lazy {
        BottomSheetDialog(requireContext())
            .bindBuilder(
                ContentSheetEmptyGroupBinding.inflate(LayoutInflater.from(requireContext()))
            ) { dialog ->
                with(dialog) {
                    binding.groupHeader.isVisible = false
                    dialog.setCancelable(false)
                    dialog.behavior.isDraggable = false
                    createGroupButton.setOnClickListener {
                        startActivity(
                            Intent(requireContext(), WebViewActivity::class.java)
                        )
                    }
                    searchGroupButton.setOnClickListener {
                        dialog.dismiss()
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToAllSearchFragment()
                        )
                    }
                    show()
                }
            }
    }

    // TODO : titleAdapter 문구 정리 필요
    private val recommendPopularRestaurantTitleAdapter by lazy {
        RecommendPopularRestaurantTitleAdapter(
            "그룹에서 인기가 많아요"
        )
    }
    private val recommendPopularRestaurantWrapperAdapter by lazy {
        RecommendPopularRestaurantWrapperAdapter(
            recommendPopularRestaurantList
        )
    }
    private val restaurantFilterAdapter by lazy { RestaurantFilterAdapter(this) }
    private val restaurantListAdapter by lazy { MapMarkerWithRestaurantsAdatper() }
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
        return binding.root
    }

    private fun setRecyclerView() {
        binding.recyclerView.adapter = concatAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMap(savedInstanceState)
        observeState()

        concatAdapter = if (recommendPopularRestaurantList.isNotEmpty()) {
            ConcatAdapter(
                recommendPopularRestaurantTitleAdapter,
                recommendPopularRestaurantWrapperAdapter,
                restaurantFilterAdapter,
                restaurantListAdapter
            )
        } else {
            ConcatAdapter(
                restaurantFilterAdapter,
                restaurantListAdapter
            )
        }

        setRestaurantListBottomSheet()
        setGroup()

    }

    override fun onViewHolderBind(holder: BaseViewHolder<out ViewBinding>, _item: Any) {
        if (holder is RestaurantFilterAdapter.RestaurantFilterViewHolder) {

            val sortSpinner = holder.itemView.findViewById<JmtSpinner>(R.id.sort_spinner)
            val foodSpinner = holder.itemView.findViewById<JmtSpinner>(R.id.food_category_spinner)
            val drinkSpinner =
                holder.itemView.findViewById<JmtSpinner>(R.id.drink_possibility_spinner)

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

    private fun setGroup() {
        binding.groupArrow.setOnClickListener {
            repeatWhenUiStarted {
                viewModel.getMyGroup().let { groupList ->
                    viewModel.setGroupList(groupList)
                }
            }

            BottomSheetDialog(requireContext())
                .bindBuilder(
                    ContentSheetGroupSelectBinding.inflate(LayoutInflater.from(requireContext()))
                ) { dialog ->

                    val listener: ViewHolderBindListener = object : ViewHolderBindListener {
                        override fun onViewHolderBind(
                            holder: BaseViewHolder<out ViewBinding>,
                            _item: Any
                        ) {
                            if (holder is GroupSelectAdapter.GroupSelectViewHolder && _item is Group) {
                                with(holder.itemView) {
                                    findViewById<TextView>(R.id.group_name).text = _item.groupName
                                    findViewById<ImageView>(R.id.select_button).isVisible =
                                        _item.isSelected
                                    Glide.with(this).load(_item.groupProfileImageUrl)
                                        .into(findViewById(R.id.group_image))

                                    setOnClickListener {
                                        // TODO GroupList : 추후에 Room에 Current Group 넣어주게 되면, 반영해야 하는 부분
                                        repeatWhenUiStarted {
                                            viewModel.selectGroup(_item.groupId)
                                            viewModel.setCurrentGroup(_item)
                                        }
                                        dialog.dismiss()
                                    }
                                }

                            }
                        }
                    }

                    with(dialog) {
                        val groupSelectAdapter = GroupSelectAdapter(listener)
                        viewModel.myGroupList.value.let {
                            when (it) {
                                is ResultState.OnSuccess -> {
                                    groupSelectAdapter.submitList(it.response ?: emptyList())
                                }

                                else -> {}
                            }
                        }

                        findViewById<RecyclerView>(R.id.group_select_recycler_view)?.apply {
                            adapter = groupSelectAdapter
                            layoutManager = LinearLayoutManager(requireContext())
                        }

                        show()
                    }
                }
        }
    }

    private fun setMap(savedInstanceState: Bundle?) {
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { naverMap ->

            val markerManager = MarkerManager(naverMap)

            naverMap.uiSettings.isZoomControlEnabled = false
            naverMap.uiSettings.isScaleBarEnabled = false

            repeatWhenUiStarted {

                viewModel.registeredPagingDataByMap().collect {
                    mapMarkerAdapter.submitData(it)
                }
            }

            repeatWhenUiStarted {
                val location = viewModel.getCurrentLocation()
                location?.let {
                    val cameraUpdate = CameraUpdate.scrollTo(LatLng(it.latitude, it.longitude))
                    naverMap.moveCamera(cameraUpdate)

                    // 데이터 확인 후 변수화
                    val zoom = CameraUpdate.zoomTo(17.0)
                    naverMap.moveCamera(zoom)
                }

                mapMarkerAdapter.registerAdapterDataObserver(object :
                    RecyclerView.AdapterDataObserver() {
                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        super.onItemRangeInserted(positionStart, itemCount)
                        markerManager.updateDataList(mapMarkerAdapter.snapshot().items)
                    }

                    override fun onChanged() {
                        super.onChanged()
                        markerManager.updateDataList(mapMarkerAdapter.snapshot().items)
                    }

                    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                        super.onItemRangeRemoved(positionStart, itemCount)
                        markerManager.updateDataList(mapMarkerAdapter.snapshot().items)
                    }
                })
            }

            binding.mapRefreshBtn.setOnClickListener {
                val projection: Projection = naverMap.projection

                val topLeft: LatLng = projection.fromScreenLocation(PointF(0F, 0F))
                val bottomRight: LatLng = projection.fromScreenLocation(
                    PointF(
                        naverMap.width.toFloat(),
                        naverMap.height.toFloat()
                    )
                )

                viewModel.setStartLocation(
                    Location(
                        topLeft.longitude.toString(),
                        topLeft.latitude.toString()
                    )
                )
                viewModel.setEndLocation(
                    Location(
                        bottomRight.longitude.toString(),
                        bottomRight.latitude.toString()
                    )
                )
            }
        }
    }


    private fun setRestaurantListBottomSheet() {

        binding.scrollUpButton.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
        }

        binding.registRestaurantButton.setOnClickListener {
            // TODO : 식당 등록 버튼 클릭 시 동작 정의 필요
            Log.d("testLog", "식당 등록 버튼 클릭")
        }

        restaurantListAdapter.addLoadStateListener { loadState ->
            if (loadState.append.endOfPaginationReached) {
                if (restaurantListAdapter.itemCount < 1) {
                    concatAdapter.addAdapter(emptyAdapter)
                    standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    concatAdapter.removeAdapter(emptyAdapter)
                }
                setRecyclerView()
            }
        }

        standardBottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                fun setBottomSheetRelatedView(isExpanded: Boolean) {
                    viewModel.currentGroup.value.let {
                        val isNotFullExpanded =
                            isExpanded.not() || it == null || it.restaurantCnt == 0

                        with(binding) {
                            binding.bottomSheetActionButtons.isVisible =
                                isExpanded && it != null && it.restaurantCnt != 0
                            bottomSheetHandle.isVisible = isNotFullExpanded
                            bottomSheetHandleSpace.isVisible = isNotFullExpanded
                            mapOptionContainer.isVisible = isNotFullExpanded
                            groupHeader.elevation = if (isNotFullExpanded) 0F else 10F
                            bottomSheet.background =
                                ResourcesCompat.getDrawable(
                                    resources,
                                    if (isNotFullExpanded)
                                        R.drawable.bg_bottom_sheet_top_round
                                    else
                                        R.color.white,
                                    null
                                )

                        }
                    }
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            setBottomSheetRelatedView(true)
                        }

                        BottomSheetBehavior.STATE_DRAGGING -> {
                            setBottomSheetRelatedView(false)
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            if (standardBottomSheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED)
                standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun observeState() {

        repeatWhenUiStarted {
            viewModel.registeredPagingDataByGroup().collect {
                restaurantListAdapter.submitData(it)
            }
        }

        repeatWhenUiStarted {
            viewModel.setSortType(SortType.DISTANCE)
        }

        repeatWhenUiStarted {
            viewModel.myGroupList.collect {
                binding.groupName.text
            }
        }

        repeatWhenUiStarted {
            viewModel.getMyGroup().let { groupList ->
                viewModel.setGroupList(groupList)
            }
        }

        repeatWhenUiStarted {
            viewModel.myGroupList.collect { state ->
                when (state) {
                    is ResultState.OnSuccess -> {
                        val groupList = state.response

                        if (groupList.isEmpty()) {
                            viewModel.setCurrentGroup(null)

                            if (bottomSheetDialog.isShowing.not()) {
                                bottomSheetDialog.show()
                            }
                        } else {
                            groupList.forEach {
                                if (it.isSelected) {
                                    viewModel.setCurrentGroup(it)
                                    return@forEach
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }
        }

        repeatWhenUiStarted {
            viewModel.currentGroup.collect {
                if (it != null) {
                    binding.groupHeader.isVisible = true
                    binding.groupName.text = it.groupName
                    binding.groupImage.apply {
                        Glide.with(this.context).load(it.groupProfileImageUrl)
                            .into(this)
                    }

                    if (it.restaurantCnt == 0) {
                        binding.bottomSheet.layoutParams.height =
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        binding.recyclerView.isVisible = false
                        binding.registLayout.isVisible = true
                    } else {
                        binding.bottomSheet.layoutParams.height =
                            ViewGroup.LayoutParams.MATCH_PARENT
                        binding.recyclerView.isVisible = true
                        binding.registLayout.isVisible = false
                    }
                }
            }

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}