package org.gdsc.presentation.view.restaurantregistration

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import org.gdsc.domain.Empty
import org.gdsc.domain.toMeterFormat
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FragmentConfirmRestaurantRegistrationBinding
import org.gdsc.presentation.utils.deviceMetrics
import org.gdsc.presentation.utils.getAbsolutePositionOnScreen
import org.gdsc.presentation.view.MainActivity
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

        setToolbarTitle()

        binding.selectButton.setOnClickListener {

            val action = ConfirmRestaurantRegistrationFragmentDirections
                .actionConfirmRestaurantRegistrationFragmentToRegisterRestaurantFragment(navArgs.restaurantLocationInfo)
            findNavController().navigate(action)

        }
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

        mapView.getMapAsync { naverMap ->
            naverMap.uiSettings.isZoomControlEnabled = false
            naverMap.uiSettings.isScaleBarEnabled = false

            val marker = Marker().apply {
                position = LatLng(
                    navArgs.restaurantLocationInfo.y.toDouble(),
                    navArgs.restaurantLocationInfo.x.toDouble()
                )
                map = naverMap
                icon = OverlayImage.fromResource(R.drawable.jmt_marker)
            }

            val cameraUpdate = CameraUpdate.scrollTo(
                LatLng(
                    navArgs.restaurantLocationInfo.y.toDouble(),
                    navArgs.restaurantLocationInfo.x.toDouble()
                )
            )
            naverMap.moveCamera(cameraUpdate)
            val zoom = CameraUpdate.zoomTo(17.0)
            naverMap.moveCamera(zoom)
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setRestaurantLocationInfoCard() {

        binding.restaurantLocationInfoCard.apply {

            restaurantName.text =
                navArgs.restaurantLocationInfo.placeName

            distanceFromCurrentLocation.text =
                getString(
                    R.string.distance_from_current_location,
                    navArgs.restaurantLocationInfo.distance.toMeterFormat()
                )

            restaurantAddress.text =
                navArgs.restaurantLocationInfo.addressName

            // 터치 이벤트 소비
            root.setOnTouchListener { _, _ ->
                true
            }
        }

    }

    private fun setToolbarTitle() {
        (requireActivity() as MainActivity).changeToolbarTitle(String.Empty)
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