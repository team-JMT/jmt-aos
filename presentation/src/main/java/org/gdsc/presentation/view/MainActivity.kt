package org.gdsc.presentation.view

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.BaseActivity
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.ActivityMainBinding
import org.gdsc.presentation.utils.slideDown
import org.gdsc.presentation.utils.slideUp
import org.gdsc.presentation.utils.toPx
import org.gdsc.presentation.view.home.HomeFragmentDirections
import org.gdsc.presentation.view.mypage.viewmodel.MyPageViewModel


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val navController by lazy {
        requireNotNull(supportFragmentManager.findFragmentById(R.id.nav_host_fragment)).findNavController()
    }
    private lateinit var binding: ActivityMainBinding

    private val myPageViewModel: MyPageViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initBottomNavigationView()
        setToolbar()
//        setToFullPage()
    }

    private fun initBottomNavigationView() {

        binding.bottomNavigationView.apply {

            setupWithNavController(navController)

            itemIconTintList = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_selected),
                    intArrayOf()
                ),
                intArrayOf(
                    getColor(R.color.grey800),
                    getColor(R.color.grey200)
                )
            )

            itemTextColor = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_selected),
                    intArrayOf()
                ),
                intArrayOf(
                    getColor(R.color.grey700),
                    getColor(R.color.grey200)
                )
            )
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            // bottomNavigationView Control
            when (destination.id) {
                R.id.home_fragment ->  {
                    slideUpBottomNavigationView()
                    binding.navHostFragment.setPadding(0, 0, 0, 0)
                }
                R.id.my_page_fragment, R.id.my_group_fragment -> {
                    slideUpBottomNavigationView()
                    binding.navHostFragment.setPadding(0, 0, 0, binding.bottomNavigationView.measuredHeight)
                }
                else -> {
                    slideDownBottomNavigationView()
                    binding.navHostFragment.setPadding(0, 0, 0, 0)
                }
            }
            
            // toolbar visibility Control
            when(destination.id) {
                R.id.home_fragment -> {
                    binding.toolBar.visibility = View.GONE
                }
                // TODO: Notification Icon
                R.id.my_page_fragment -> {
                    requireNotNull(supportActionBar).setDisplayHomeAsUpEnabled(false)
                    binding.toolBar.visibility = View.VISIBLE
                }
                else ->{
                    requireNotNull(supportActionBar).setDisplayHomeAsUpEnabled(true)
                    binding.toolBar.visibility = View.VISIBLE
                }
            }

        }

    }

    private fun setToolbar() {
        val toolBarView = binding.toolBar
        setSupportActionBar(toolBarView)
        requireNotNull(supportActionBar).apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_arrow)
            setDisplayShowTitleEnabled(false)
        }

        // for status bar size margin
//        (toolBarView.layoutParams as ViewGroup.MarginLayoutParams).apply {
//            setMargins(0, getStatusBarHeight(), 0, 0)
//        }

    }

    private fun setToFullPage() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun slideDownBottomNavigationView() {
        val myAnimationListener: Animator.AnimatorListener =
            object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {}
                override fun onAnimationEnd(p0: Animator) {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                override fun onAnimationCancel(p0: Animator) {}
                override fun onAnimationRepeat(p0: Animator) {}
            }
        runOnUiThread {
            binding.bottomNavigationView.slideDown(endListener = myAnimationListener)
        }
    }

    fun slideUpBottomNavigationView() {
        runOnUiThread {
            binding.bottomNavigationView.visibility = View.VISIBLE
            binding.bottomNavigationView.slideUp()
        }
    }

    fun changeToolbarTitle(title: String) {
        binding.toolBarTitle.text = title
    }

    fun changeToolbarVisible(isVisible: Boolean) {
        binding.toolBar.isVisible = isVisible
    }

    fun navigateToEditRestaurantInfo(restaurantId: Int) {

        val action = HomeFragmentDirections.actionHomeFragmentToRegisterRestaurantFragment(
            targetRestaurantId = restaurantId
        )
        navController.navigate(action)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navController.popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    private fun getStatusBarHeight(): Int {
        var result = 24.toPx
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}