package org.gdsc.presentation.login

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import org.gdsc.presentation.databinding.FragmentSignUpCompleteBinding
import org.gdsc.presentation.view.PermissionFragmentDirections

class SignUpCompleteFragment : Fragment() {

    private var _binding: FragmentSignUpCompleteBinding? = null
    private val binding
        get() = requireNotNull(_binding)

    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProfileImageButtonAnimation()

        binding.nicknameText.text = viewModel.nicknameState.value

        binding.profileImageAddButton.setOnClickListener {
            // TODO: 갤러리에서 이미지 pick
            SignUpCompleteFragmentDirections.actionSignUpCompleteFragmentToPermissionFragment()
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setProfileImageButtonAnimation() {
        binding.profileImageAddButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val parent = v.parent as CardView
                    parent.animate()
                        .scaleX(1.2f)
                        .scaleY(1.2f)
                }
                MotionEvent.ACTION_UP -> {
                    val parent = v.parent as CardView
                    parent.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                }
            }
            false
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val URI_SELECTED = "URI_SELECTED"
        const val TAG = "SignUpCompleteFragment"
    }
}
