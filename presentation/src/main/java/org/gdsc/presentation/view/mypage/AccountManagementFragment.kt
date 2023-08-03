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
            JmtAlert(requireContext())
                .title("정말로 탈퇴하시겠어요?")
                .content("계정 정보를 포함한 모든 데이터가\n" +
                        "영구적으로 삭제되며 복구가 어려워요.")
                .multiButton {
                    leftButton("탈퇴하기", JmtAlert.FILL_OUTLINE) {
                        viewModel.signout {
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                        }
                    }
                    rightButton("유지하기", JmtAlert.FILL_FILL)
                }.show()
        }

        binding.tvLogOut.setOnClickListener {
            JmtAlert(requireContext())
                .title("로그아웃 할까요?")
                .content("비로그인 상태로 돌아가요.\n" +
                        "로그아웃을 진행할까요?")
                .multiButton {
                    leftButton("로그아웃하기", JmtAlert.FILL_OUTLINE) {
                        viewModel.logout {
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                        }
                    }
                    rightButton("유지하기", JmtAlert.FILL_FILL)
                }.show()
        }
    }

    private fun setToolbarTitle() {
        (requireActivity() as MainActivity).changeToolbarTitle("계정 관리")
//        (requireActivity() as MainActivity).changeToolbarTitle(getString(R.string.account_management))
    }
}