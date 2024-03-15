package org.gdsc.presentation.view.allsearch

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.R
import org.gdsc.presentation.base.CancelViewListener
import org.gdsc.presentation.base.SearchViewListener
import org.gdsc.presentation.databinding.FragmentAllSearchBinding
import org.gdsc.presentation.utils.repeatWhenUiStarted
import org.gdsc.presentation.view.MainActivity

@AndroidEntryPoint
class AllSearchFragment : Fragment() {

    private var _binding: FragmentAllSearchBinding? = null
    private val binding get() = _binding!!

    private val parent by lazy { requireActivity() as MainActivity }

    private val viewModel: AllSearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parent.changeToolbarTitle("검색")

        binding.searchBar.setSearchViewListener(searchListener)
        binding.searchBar.setCancelViewListener(cancelViewListener)

        binding.tvDelete.setOnClickListener {
            viewModel.deleteAllSearchedKeyword()
        }

        repeatWhenUiStarted {
            viewModel.searchedKeywordsState.collect { keywordList ->
                binding.cgRecentSearch.removeAllViews()
                keywordList.forEach {
                    if (it.isNotBlank()) {
                        binding.tvRecentSearch.visibility = View.VISIBLE
                        binding.tvDelete.visibility = View.VISIBLE
                        binding.cgRecentSearch.addView(
                            newChip(it,
                                { keyword ->
                                    viewModel.deleteSearchedKeyword(keyword)
                                }) { keyword ->
                                binding.searchBar.editText.setText(keyword)
                                navigateToResultPage()
                            }
                        )
                    } else {
                        binding.tvRecentSearch.visibility = View.GONE
                        binding.tvDelete.visibility = View.GONE
                    }
                }
            }
        }
    }

    private val searchListener = object : SearchViewListener {
        override fun onSearchClear() {}
        override fun changeFocus(focus: Boolean) {}
        override fun onChangeText(text: CharSequence) {}
        override fun onSubmitText(text: CharSequence) {
            if (text.isEmpty()) return
            viewModel.updateSearchedKeyword(text.toString())
            val action =
                AllSearchFragmentDirections.actionAllSearchFragmentToAllSearchContainerFragment(text.toString())
            findNavController().navigate(action)
        }
    }

    private val cancelViewListener = object : CancelViewListener {
        override fun onCancel() {
            findNavController().navigateUp()
        }
    }

    private fun newChip(
        text: String,
        onCloseIconClicked: (String) -> Unit,
        onClicked: (String) -> Unit
    ): Chip {
        return Chip(requireContext()).apply {
            this.text = text
            isCloseIconVisible = true
            closeIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.cancel_icon
            )

            closeIconTint = ContextCompat.getColorStateList(
                requireContext(),
                R.color.grey200
            )

            chipBackgroundColor = ContextCompat.getColorStateList(
                requireContext(),
                R.color.white
            )
            chipStrokeColor = ContextCompat.getColorStateList(
                requireContext(),
                R.color.grey200
            )
            chipStrokeWidth = 1f

            rippleColor = ColorStateList.valueOf(Color.TRANSPARENT)

            setOnCloseIconClickListener {
                onCloseIconClicked(text)
            }

            setOnClickListener {
                onClicked(text)
            }

        }

    }

    private fun navigateToResultPage() {
        val action =
            AllSearchFragmentDirections.actionAllSearchFragmentToAllSearchContainerFragment(binding.searchBar.text)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        _binding = null
        parent.changeToolbarTitle("")
        super.onDestroyView()
    }
}