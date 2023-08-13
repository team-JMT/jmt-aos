package org.gdsc.presentation.view.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.adapter.RegisteredRestaurantPagingDataAdapter
import org.gdsc.presentation.databinding.FragmentRegisteredRestaurantBinding
import org.gdsc.presentation.view.mypage.viewmodel.MyPageViewModel

@AndroidEntryPoint
class RegisteredRestaurantFragment : Fragment() {

    private var _binding: FragmentRegisteredRestaurantBinding? = null
    private val binding get() = _binding!!
    private lateinit var myRestaurantAdapter: RegisteredRestaurantPagingDataAdapter

    private val viewModel: MyPageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisteredRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myRestaurantAdapter = RegisteredRestaurantPagingDataAdapter()
        binding.registeredRestaurantRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.registeredRestaurantRecyclerView.adapter = myRestaurantAdapter
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}