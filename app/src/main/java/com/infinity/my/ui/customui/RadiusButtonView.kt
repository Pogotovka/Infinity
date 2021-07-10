package com.infinity.my.ui.customui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.toBitmap
import com.infinity.my.R
import com.infinity.my.utils.dp
import com.infinity.my.utils.setForeground

class RadiusButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr){


    private val paint: Paint = Paint().apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.colorAccent)
    }

    private val gd: GradientDrawable = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
    }

    var centerDrawable: Drawable?

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RadiusButtonView, defStyleAttr, 0)
        gd.apply {
            cornerRadius = a.getDimension(R.styleable.RadiusButtonView_corner_radius, 12f.dp(resources))
            setColor(a.getColor(R.styleable.RadiusButtonView_bg_color, resources.getColor(R.color.colorPrimary)))
        }
        background = gd
        centerDrawable = a.getDrawable(R.styleable.RadiusButtonView_drawable_center)?.apply {
            colorFilter = getColorInColorFilter(a.getColor(R.styleable.RadiusButtonView_drawable_tint, resources.getColor(R.color.colorAccent)))
        }
        a.recycle()
        setForeground()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        centerDrawable?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTintMode(compoundDrawableTintMode)
            canvas.drawBitmap(
                toBitmap(),
                (this@RadiusButtonView.width / 2f) - (this.bounds.right / 2),
                (this@RadiusButtonView.height / 2f) - (this.bounds.bottom / 2),
                paint
            )
        }
    }


    fun setBgColor(color: Int) {
        if (color == 0) return
        gd.setColor(color)
        invalidate()
    }

    private fun getColorInColorFilter(color: Int) = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)

}