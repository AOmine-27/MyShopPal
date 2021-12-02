package com.example.myshoppal.activities

import android.app.Dialog
import android.app.ProgressDialog.show
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.TextureView
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    fun showErrorSnackBar(message: String, errorMessage: Boolean){
            val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
            val snackBarView = snackBar.view

            if (!errorMessage) {
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@BaseActivity, R.color.colorSnackBarError
                    )
                )
            } else {
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@BaseActivity, R.color.colorSnackBarSuccess
                    )
                )
            }
            snackBar.show()
        }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen*/
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.findViewById<TextView>(R.id.tv_progress_text).text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }
}