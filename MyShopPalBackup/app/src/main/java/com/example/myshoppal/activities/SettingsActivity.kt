package com.example.myshoppal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.utils.GlideLoader
import com.google.firebase.firestore.auth.User

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupActionBar()
    }

    private fun setupActionBar(){

        val toolbar_settings_activity = findViewById<Toolbar>(R.id.toolbar_settings_activity)
        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        toolbar_settings_activity?.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: com.example.myshoppal.models.User){
        hideProgressDialog()

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, findViewById(R.id.iv_user_photo))
        findViewById<TextView>(R.id.tv_name).text = "${user.firstName} ${user.lastName}"
        findViewById<TextView>(R.id.tv_gender).text = user.gender
        findViewById<TextView>(R.id.tv_email).text = user.email
        findViewById<TextView>(R.id.tv_mobile_number).text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }
}