package org.gdsc.presentation.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.BaseActivity
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.ActivityMainBinding
import org.gdsc.presentation.utils.slideDown
import org.gdsc.presentation.utils.slideUp
import org.gdsc.presentation.utils.toPx

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val navController by lazy {
        requireNotNull(supportFragmentManager.findFragmentById(R.id.nav_host_fragment)).findNavController()
    }
    private lateinit var binding: ActivityMainBinding

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        if (permissions.all { it.value }) {
            Toast.makeText(this, "위치 권한이 허용되었습니다!", Toast.LENGTH_SHORT).show()
        } else {
            showLocationPermissionDialog()
        }

    }

    private fun checkLocationRequest() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // TODO: change to JMT Dialog
    private fun showLocationPermissionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("위치 권한 요청")
            .setMessage("위치 권한 설정 창으로 이동할까요?")
            .setPositiveButton("네") { _, _ ->

                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", packageName, null)
                    )
                )
            }
            .setNegativeButton("아니요") { _, _ ->
                Toast.makeText(this, "위치 권한이 꼭 필요합니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        checkLocationRequest()
        setContentView(binding.root)
        initBottomNavigationView()
        setToolbar()
        setToFullPage()
    }

    private fun initBottomNavigationView() {

        binding.bottomNavigationView.apply {

            setupWithNavController(navController)

            itemIconTintList = null

            itemTextColor = ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_selected),
                    intArrayOf()
                ),
                intArrayOf(
                    getColor(R.color.main600),
                    getColor(R.color.grey200)
                )
            )
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            // bottomNavigationView Control
            if (destination.id == R.id.home_fragment || destination.id == R.id.my_page_fragment)
                slideUpBottomNavigationView()
            else
                slideDownBottomNavigationView()

            // toolbar visibility Control
            when(destination.id) {
                R.id.home_fragment, R.id.confirm_restaurant_registration_fragment -> {
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
        (toolBarView.layoutParams as ViewGroup.MarginLayoutParams).apply {
            setMargins(0, getStatusBarHeight(), 0, 0)
        }

    }

    private fun setToFullPage() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun slideDownBottomNavigationView() {
        binding.bottomNavigationView.slideDown()
    }

    fun slideUpBottomNavigationView() {
        binding.bottomNavigationView.slideUp()
    }

    fun changeToolbarTitle(title: String) {
        binding.toolBarTitle.text = title
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