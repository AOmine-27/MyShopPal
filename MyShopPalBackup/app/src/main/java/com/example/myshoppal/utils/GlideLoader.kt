package com.example.myshoppal.utils

import android.content.Context
import android.media.Image
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshoppal.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            //Load the user image in the ImageView
            Glide
                .with(context)
                .load(image) //URI of the image
                .centerCrop() //Scale type of the image
                .placeholder(R.drawable.ic_user_placeholder)//Default place holder if the image fails to load
                .into(imageView)//the view in which the image will be loaded
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}