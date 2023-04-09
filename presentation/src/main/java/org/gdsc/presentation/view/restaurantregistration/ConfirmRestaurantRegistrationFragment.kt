package org.gdsc.presentation.view.restaurantregistration

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.fragment.navArgs
import com.naver.maps.map.MapView
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentConfirmRestaurantRegistrationBinding
import org.gdsc.presentation.utils.deviceMetrics
import org.gdsc.presentation.utils.getAbsolutePositionOnScreen
import org.gdsc.presentation.view.custom.JmtSnackbar

class ConfirmRestaurantRegistrationFragment : Fragment() {

    private var _binding: FragmentConfirmRestaurantRegistrationBinding? = null
    private val binding get() = _binding!!
    private val navArgs by navArgs<ConfirmRestaurantRegistrationFragmentArgs>()

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmRestaurantRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMap(savedInstanceState)
        setRestaurantLocationInfoCard()
        initView(view)
    }

    private fun initView(view: View) {
        if (navArgs.canRegister) {
            binding.buttonContainer.visibility = View.VISIBLE
        } else {
            view.post {
                // TODO: dynamic bottom padding
                val bottomPadding =
                    (deviceMetrics.heightPixels - binding.restaurantLocationInfoCard.root.getAbsolutePositionOnScreen().second).toInt() - 90
                JmtSnackbar.make(
                    binding.root,
                    getString(R.string.already_registered),
                    Toast.LENGTH_SHORT
                )
                    .setTextColor(getColor(requireContext(), R.color.unable_nickname_color))
                    .setBackgroundAlpha(0.7f)
                    .setPadding(
                        bottom = bottomPadding
                    )
                    .show()

            }
        }
    }

    private fun setMap(savedInstanceState: Bundle?) {
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { map ->
            map.uiSettings.isZoomControlEnabled = false
            map.uiSettings.isScaleBarEnabled = false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setRestaurantLocationInfoCard() {

        binding.restaurantLocationInfoCard.apply {

            restaurantName.text =
                navArgs.restaurantLocationInfo.placeName

            distanceFromCurrentLocation.text =
                navArgs.restaurantLocationInfo.distance

            restaurantAddress.text =
                navArgs.restaurantLocationInfo.addressName

            // 터치 이벤트 소비
            root.setOnTouchListener { _, _ ->
                true
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