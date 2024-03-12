package com.nbc.shownect.ui.ticket

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import com.nbc.shownect.R
import com.nbc.shownect.ui.detail_activity.DetailActivity
import com.nbc.shownect.util.Constants

//Gesture
open class OnSwipeTouchListener(ctx: Context) : View.OnTouchListener {

	private val gestureDetector: GestureDetector

	companion object {
		private val SWIPE_THRESHOLD = 500 // 스와이프 거리를 늘릴 값
		private val SWIPE_VELOCITY_THRESHOLD = 500 // 스와이프 인식에 필요한 최소 속도를 늘릴 값
	}

	init {
		gestureDetector = GestureDetector(ctx, GestureListener())
	}

	private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

		override fun onDown(e: MotionEvent): Boolean {
			return true
		}

		override fun onFling(
			e1: MotionEvent?,
			e2: MotionEvent,
			velocityX: Float,
			velocityY: Float
		): Boolean {
			var result = false
			try {
				val diffY = e2.y - e1!!.y
				val diffX = e2.x - e1.x
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffX > 0) {
							onSwipeRight()
						} else {
							onSwipeLeft()
						}
						result = true
					}
				} else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
					if (diffY > 0) {
						onSwipeBottom()
					} else {
						onSwipeTop()
					}
					result = true
				}
			} catch (exception: Exception) {
				exception.printStackTrace()
			}

			return result
		}

	}

	fun onSwipeRight() {}

	fun onSwipeLeft() {}

	open fun onSwipeTop() {}

	fun onSwipeBottom() {}
	override fun onTouch(v: View?, event: MotionEvent?): Boolean {
		try {
			return gestureDetector.onTouchEvent(event!!)
		} catch (e: Exception) {
			// Error Handling
		}
		return false
	}
}
