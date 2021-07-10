package com.infinity.my.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.snackbar.Snackbar
import com.infinity.my.R

fun View.hideKeyboard() = context.getSystemService(Context.INPUT_METHOD_SERVICE)
    .castTo<InputMethodManager>()
    .hideSoftInputFromWindow(windowToken, 0)

fun View.showToast(
    lifecycleOwner: LifecycleOwner,
    event: LiveData<SingleEvent<Any>>,
    timeLength: Int
) {
    event.observe(lifecycleOwner, { e ->
        e.getContentIfNotHandled()?.let {
            when (it) {
                is String -> Toast.makeText(context, it, timeLength).show()
                is Int -> Toast.makeText(context, context.getString(it), timeLength).show()
                else -> throw NotImplementedError("NotImplementedError  $this")

            }
        }
    })
}

fun View.showSnackbar(
    lifecycleOwner: LifecycleOwner,
    event: LiveData<SingleEvent<Any>>,
    timeLength: Int
) {
    event.observe(lifecycleOwner, { e ->
        e.getContentIfNotHandled()?.let {
            when (it) {
                is String -> Snackbar.make(this, it, timeLength).show()
                is Int -> Snackbar.make(this, context.getString(it), timeLength).show()
                else -> throw NotImplementedError("NotImplementedError  $this")
            }
        }
    })
}

fun Context.showAlertDialog(title: String?, message: String?, f: () -> Unit) =
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(getString(R.string.ok)) { _, _ -> f.invoke() }
        .create()
        .show()

fun Float.dp(res: Resources): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, res.displayMetrics)

fun View.setForeground() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        isClickable = true
        isFocusable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            foreground = resources.getDrawable(R.drawable.selectable_item_borderless_ripple, context.theme)
    }
}

fun MaterialCardView.setStyleToMaterialCardView() {
    val leftShapePathModel = ShapeAppearanceModel().toBuilder().apply {
        setTopLeftCorner(
            CornerFamily.ROUNDED,
            CornerSize { return@CornerSize 80F })
        setTopRightCorner(
            CornerFamily.ROUNDED,
            CornerSize { return@CornerSize 80F })
    }

    val bg = MaterialShapeDrawable(leftShapePathModel.build())
    /*  bg.fillColor = ColorStateList.valueOf(
          context.resources.getColor(android.R.color.white)
      )*/
    bg.elevation = 8F
    background = bg

    invalidate()
}

/** Returns true if the device has an available back camera. False otherwise */
fun ProcessCameraProvider.hasBackCamera(): Boolean = hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) other false

/** Returns true if the device has an available front camera. False otherwise */
fun ProcessCameraProvider.hasFrontCamera(): Boolean = hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) other false
