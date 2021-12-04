package com.example.myshoppal.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.myshoppal.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)



        findViewById<Button>(R.id.btn_submit).setOnClickListener{
            sendPasswordResetEmail()
        }

        setupActionBar()
    }

    private fun setupActionBar(){

        val toolbar_forgot_password_activity = findViewById<Toolbar>(R.id.toolbar_forgot_password_activity)
        setSupportActionBar(toolbar_forgot_password_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }

        toolbar_forgot_password_activity?.setNavigationOnClickListener { onBackPressed() }
    }



    private fun sendPasswordResetEmail(){
        val email = findViewById<EditText>(R.id.et_forgot_email).text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            Toast.makeText(
                this@ForgotPasswordActivity,
                "Please enter email address",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "An email was sent to you",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                } else {
                    Toast.makeText(
                        this,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

}