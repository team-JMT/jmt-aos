package org.gdsc.presentation.view.custom

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.FoodCategoryBottomSheetDialogBinding
import org.gdsc.presentation.model.FoodCategoryItem
import org.gdsc.presentation.view.restaurantregistration.FoodCategoryRecyclerAdapter

class FoodCategoryBottomSheetDialog(private val onSelectButtonClicked: (List<FoodCategoryItem>) -> Unit) :
    BottomSheetDialogFragment() {

    private val items by lazy {
        resources.getStringArray(R.array.food_categories).map {
            FoodCategoryItem(it)
        }
    }

    private var _binding: FoodCategoryBottomSheetDialogBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FoodCategoryBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setSelectButton()
        setDialog()
    }

    private fun setRecyclerView() {

        val adapter = FoodCategoryRecyclerAdapter()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(items)
    }

    private fun setSelectButton() {
        binding.selectButton.setOnClickListener {
            onSelectButtonClicked(items.filter { it.isSelected })
            dismiss()
        }
    }

    private fun setDialog() {
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.let { bottomSheet ->

                bottomSheet.apply {
                    layoutParams.height = LayoutParams.MATCH_PARENT
                    setBackgroundResource(R.drawable.bottom_sheet_background)
                }

                BottomSheetBehavior.from<View>(bottomSheet).apply {
                    peekHeight = (resources.displayMetrics.heightPixels * 0.4).toInt()
                    maxHeight = (resources.displayMetrics.heightPixels * 0.85).toInt()
                }

            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun getTheme(): Int {
        return R.style.JmtBottomSheetDialog
    }
}