package org.gdsc.presentation.view.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.databinding.FragmentAccountManagementBinding
import org.gdsc.presentation.login.LoginActivity
import org.gdsc.presentation.view.MainActivity
import org.gdsc.presentation.view.custom.JmtAlert
import org.gdsc.presentation.view.mypage.viewmodel.MyPageViewModel

@AndroidEntryPoint
class AccountManagementFragment: Fragment() {

    private var _binding: FragmentAccountManagementBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MyPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAccountManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarTitle()

        binding.signOutLay.setOnClickListener {

        }

        binding.tvLogOut.setOnClickListener {

        }
    }

    private fun setToolbarTitle() {
        (requireActivity() as MainActivity).changeToolbarTitle("계정 관리")
//        (requireActivity() as MainActivity).changeToolbarTitle(getString(R.string.account_management))
    }
}