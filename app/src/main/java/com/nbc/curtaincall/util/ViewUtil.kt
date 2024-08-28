package com.nbc.curtaincall.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation

object ViewUtil {
    fun ImageView.setPoster(view: View, posterPath: String?) {
        Glide.with(view).load(posterPath).into(this)
    }

    fun ImageView.setBackGround(context: Context, posterPath: String?) {
        Glide.with(context).load(posterPath)
            .apply(
                RequestOptions.bitmapTransform(
                    BlurTransformation(20, 5)
                )
            ).into(this)
    }
}