package org.gdsc.presentation.utils

import android.Manifest
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun EditText.addAfterTextChangedListener(block: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            block(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

fun View.fadeIn() {
    val self = this
    val animation = AlphaAnimation(0f, 1f)
    animation.duration = 200
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            self.visibility = View.VISIBLE
        }

        override fun onAnimationRepeat(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
        }
    })
    self.startAnimation(animation)
}

fun View.fadeOut() {
    val self = this
    val animation = AlphaAnimation(1f, 0f)
    animation.duration = 200
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            self.visibility = View.GONE
        }

        override fun onAnimationRepeat(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
        }
    })
    self.startAnimation(animation)
}

fun View.animateShrinkWidth(ratio: Int = 2) {
    val self = this@animateShrinkWidth
    this.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            self.viewTreeObserver.removeOnPreDrawListener(this)
            val anim = ValueAnimator.ofInt(self.width, self.width / ratio).apply {
                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int
                    val layoutParams = self.layoutParams
                    layoutParams.width = value
                    self.layoutParams = layoutParams
                }
                duration = 300
            }
            anim.start()
            return true
        }
    })
}

fun View.animateExtendWidth(ratio: Int = 2) {
    val self = this@animateExtendWidth
    this.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            self.viewTreeObserver.removeOnPreDrawListener(this)
            val anim = ValueAnimator.ofInt(self.width, self.width * ratio).apply {
                addUpdateListener { valueAnimator ->
                    val value = valueAnimator.animatedValue as Int
                    val layoutParams = self.layoutParams
                    layoutParams.width = value
                    self.layoutParams = layoutParams
                }
                duration = 300
            }
            anim.start()
            return true
        }
    })
}

fun View.getAbsolutePositionOnScreen(): Pair<Float, Float> {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return Pair(location[0].toFloat(), location[1].toFloat())
}

fun Fragment.showMediaPermissionsDialog() {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle("갤러 접근 권한 요청")
        .setMessage("갤러리 접근 권한 설정 창으로 이동할까요?")
        .setPositiveButton("네") { _, _ ->

            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${activity?.packageName}")
                )
            )
        }.setNegativeButton("아니오") { _, _ ->
            Toast.makeText(requireContext(), "갤러리 접근 권한이 꼭 필요합니다.", Toast.LENGTH_SHORT).show()
        }.show()
}

fun Fragment.checkMediaPermissions(
    requestPermissionsLauncher: androidx.activity.result.ActivityResultLauncher<Array<String>>,
    onSuccess: () -> Unit
) {

    val requiredPermissionsTIRAMISU = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    val requiredPermissionsOTHER = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)


    fun hasAllPermissions(context: Context, requiredPermissions: Array<String>) = requiredPermissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermission(requiredPermissions:Array<String>) {
        if(hasAllPermissions(requireContext(), requiredPermissions)) {
            onSuccess.invoke()
        }else{
            requestPermissionsLauncher.launch(requiredPermissions)
        }
    }

    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) {
        checkPermission(requiredPermissionsTIRAMISU)
    } else{
        checkPermission(requiredPermissionsOTHER)
    }
}

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.toDp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()