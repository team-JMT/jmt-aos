package org.gdsc.presentation.view.custom

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import org.gdsc.domain.FoodCategory
import org.gdsc.presentation.databinding.FoodCategoryBottomSheetDialogBinding
import org.gdsc.presentation.model.FoodCategoryItem
import org.gdsc.presentation.utils.toDp
import org.gdsc.presentation.utils.toPx
import org.gdsc.presentation.view.restaurantregistration.adapter.FoodCategoryRecyclerAdapter

class FoodCategoryBottomSheetDialog(private val onSelectButtonClicked: (FoodCategoryItem?) -> Unit) :
    BottomSheetDialogFragment() {

    private val items by lazy {
        FoodCategory.ALL.map {
            FoodCategoryItem(it)
        }
    }

    private var selectedItem: FoodCategoryItem? = null

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
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setOnShowListener {
                findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.apply {
                    background = MaterialShapeDrawable(
                        ShapeAppearanceModel.builder()
                            .setTopLeftCornerSize(16.toPx.toFloat())
                            .setTopRightCornerSize(16.toPx.toFloat())
                            .build()
                    ).apply {
                        fillColor = ColorStateList.valueOf(Color.WHITE) // TODO Color 변경하기
                    }

                    layoutParams.height = LayoutParams.MATCH_PARENT

                    BottomSheetBehavior.from<View>(this).apply {
                        peekHeight = (resources.displayMetrics.heightPixels * 0.4).toInt()
                        maxHeight = (resources.displayMetrics.heightPixels * 0.85).toInt()
                    }
                }

                val dialog = it as BottomSheetDialog
                val container = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.container)

                binding.buttonContainer.run {
                    (parent as ViewGroup).removeView(this)

                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        height,
                    ).apply {
                        gravity = Gravity.BOTTOM
                    }

                    container?.addView(this)
                }
            }
        }
    }

    private fun setRecyclerView() {

        val adapter = FoodCategoryRecyclerAdapter {
            selectedItem = it
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(items)
    }

    private fun setSelectButton() {
        binding.selectButton.setOnClickListener {
            onSelectButtonClicked(selectedItem)
            dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}