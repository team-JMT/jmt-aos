package org.gdsc.presentation.view

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val navController by lazy {
        requireNotNull(supportFragmentManager.findFragmentById(R.id.nav_host_fragment)).findNavController()
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigationView()

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
    }
}