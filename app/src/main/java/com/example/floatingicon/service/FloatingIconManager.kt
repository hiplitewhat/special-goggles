package com.example.floatingicon.service

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import com.example.floatingicon.R
import kotlin.math.abs

class FloatingIconManager(private val context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var floatingView: FrameLayout? = null
    private var params: WindowManager.LayoutParams? = null

    private var lastX = 0
    private var lastY = 0
    private var startX = 0f
    private var startY = 0f
    private var isDragging = false

    fun showFloatingIcon() {
        if (floatingView != null) {
            return
        }

        val inflater = LayoutInflater.from(context)
        floatingView = inflater.inflate(R.layout.floating_icon_layout, null) as FrameLayout

        params = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            }
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

            width = dpToPx(56)
            height = dpToPx(56)
            x = dpToPx(20)
            y = dpToPx(100)
            gravity = Gravity.TOP or Gravity.START
        }

        floatingView?.let { view ->
            view.setOnTouchListener(createTouchListener())
            windowManager.addView(view, params)
        }
    }

    fun hideFloatingIcon() {
        floatingView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {
                // View was already removed or not added
            }
        }
        floatingView = null
        params = null
    }

    private fun createTouchListener(): View.OnTouchListener {
        return View.OnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.rawX
                    startY = event.rawY
                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()
                    isDragging = false

                    // Enable touch for dragging
                    updateWindowParams(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX = abs(event.rawX - startX)
                    val deltaY = abs(event.rawY - startY)

                    if (deltaX > 10 || deltaY > 10) {
                        isDragging = true
                    }

                    if (isDragging) {
                        val newX = lastX + (event.rawX - lastX).toInt()
                        val newY = lastY + (event.rawY - lastY).toInt()

                        params?.let {
                            it.x = newX
                            it.y = newY
                            windowManager.updateViewLayout(view, it)
                            lastX = newX
                            lastY = newY
                        }
                    }
                    true
                }

                MotionEvent.ACTION_UP -> {
                    if (!isDragging) {
                        handleIconClick()
                    }

                    // Disable touch after interaction
                    updateWindowParams(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                    isDragging = false
                    true
                }

                else -> false
            }
        }
    }

    private fun updateWindowParams(flags: Int) {
        params?.let {
            it.flags = flags
            floatingView?.let { view ->
                windowManager.updateViewLayout(view, it)
            }
        }
    }

    private fun handleIconClick() {
        Toast.makeText(context, "Floating icon tapped!", Toast.LENGTH_SHORT).show()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}
