package org.gdsc.presentation.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.JmtSpinnerBinding

class JmtSpinner(context: Context, attrs: AttributeSet) : LinearLayoutCompat(context, attrs) {

    private val binding = JmtSpinnerBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private lateinit var menu: PopupMenu

    init {


        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.JmtSpinner,
            0, 0
        ).apply {
            try {

                getBoolean(R.styleable.JmtSpinner_isSolidType, false).let { isSolidType ->
                    if (isSolidType) {
                        binding.container.background =
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.solid_spinner_background,
                                null
                            )
                    } else {
                        binding.container.background =
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.spinner_background,
                                null
                            )
                    }

                }

            } finally {
                recycle()
            }
        }

        removeAllViews()
        addView(binding.root)

        binding.container.setOnClickListener {
            menu.show()
        }


    }

    fun setMenu(menuTitles: Collection<String>, menuItemClickListener: (MenuItem) -> Unit = {}) {
        menu = PopupMenu(this.context, binding.container).apply {

            menuTitles.forEachIndexed { index, it ->
                menu.add(Menu.NONE, index, Menu.NONE, it)
            }

            setOnMenuItemClickListener { menuItem ->
                menuItemClickListener(menuItem)
                true
            }
        }

    }

    fun setMenuTitle(menuTitle: String) {
        binding.menuTitle.text = menuTitle
    }

}