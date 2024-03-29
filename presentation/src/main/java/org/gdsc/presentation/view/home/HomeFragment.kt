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
import androidx.lifecycle.lifecycleScope
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
import org.gdsc.presentation.databinding.ItemMapWithRestaurantBinding
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

    private val restaurantFilterAdapter by lazy { RestaurantFilterAdapter(this) }
    private val restaurantListAdapter by lazy { MapMarkerWithRestaurantsAdatper(this) }
    private val mapMarkerAdapter by lazy { MapMarkerWithRestaurantsAdatper(this) }
    private val emptyAdapter by lazy { EmptyAdapter() }
    private lateinit var concatAdapter: ConcatAdapter

    private val recommendPopularRestaurantList = listOf<RegisteredRestaurant>()

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

        concatAdapter = ConcatAdapter(
            restaurantFilterAdapter,
            restaurantListAdapter
        )

        setRestaurantListBottomSheet()
        setGroup()
        setView()

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
        } else if (holder is MapMarkerWithRestaurantsAdatper.RestaurantsWithMapViewHolder && _item is RegisteredRestaurant) {

            val binding = ItemMapWithRestaurantBinding.bind(holder.itemView)
            binding.run {
                Glide.with(root)
                    .load(_item.userProfileImageUrl)
                    .placeholder(R.drawable.base_profile_image)
                    .into(userProfileImage)

                userName.text = _item.userNickName

                Glide.with(root)
                    .load(_item.restaurantImageUrl)
                    .placeholder(R.drawable.base_profile_image)
                    .into(restaurantImage)

                restaurantCategory.text = _item.category
                drinkAvailability.text = if (_item.canDrinkLiquor) "주류 가능" else "주류 불가능"

                restaurantName.text = _item.name
                restaurantDesc.text = _item.introduce
            }
            holder.itemView.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToRestaurantDetailFragment(
                        _item.id
                    )
                )
            }

        }
    }
    
    private fun setView() {
        binding.groupSearch.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAllSearchFragment())
        }
    }

    private fun setGroup() {
        binding.groupArrow.setOnClickListener {
            lifecycleScope.launch {
                viewModel.requestGroupList()
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

            lifecycleScope.launch {

                viewModel.registeredPagingDataByMap().collect {
                    mapMarkerAdapter.submitData(it)
                }
            }

            lifecycleScope.launch {
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
        
        binding.registButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchRestaurantLocationInfoFragment(
                viewModel.currentGroup.value?.groupId ?: 0
            ))
        }

        binding.scrollUpButton.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
        }

        binding.registRestaurantButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchRestaurantLocationInfoFragment(
                viewModel.currentGroup.value?.groupId ?: 0
            ))
        }


        val standardBottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.bottomSheet) }

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
    }

    private fun observeState() {

        lifecycleScope.launch {
            viewModel.registeredPagingDataByList().collect {
                restaurantListAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            val data = viewModel.getRestaurantMapWithLimitCount(SortType.DISTANCE, viewModel.currentGroup.value)

            if (data.isNullOrEmpty().not()) {

                // TODO : titleAdapter 문구 정리 필요
                val recommendPopularRestaurantTitleAdapter = RecommendPopularRestaurantTitleAdapter("그룹에서 인기가 많아요")
                val recommendPopularRestaurantWrapperAdapter = RecommendPopularRestaurantWrapperAdapter(data)

                concatAdapter.addAdapter(0, recommendPopularRestaurantTitleAdapter)
                concatAdapter.addAdapter(1, recommendPopularRestaurantWrapperAdapter)
            }
        }

        repeatWhenUiStarted {
            viewModel.setSortType(SortType.DISTANCE)
        }

        lifecycleScope.launch {
            viewModel.requestGroupList()
        }

        repeatWhenUiStarted {
            viewModel.myGroupList.collect { state ->
                when (state) {
                    is ResultState.OnSuccess -> {
                        val groupList = state.response

                        if (groupList.isEmpty()) {
                            bottomSheetDialog.show()
                        } else {
                            groupList.forEach {
                                if (it.isSelected) {
                                    viewModel.setCurrentGroup(it)
                                    return@forEach
                                }
                            }
                            bottomSheetDialog.dismiss()
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


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        mapView.onDestroy()
    }
}