package com.example.myshoppal.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.myshoppal.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        //Click event assigned to Forgot Password text.
        findViewById<TextView>(R.id.tv_forgot_password).setOnClickListener(this)
        //Click event assigned to Login button.
        findViewById<TextView>(R.id.btn_login).setOnClickListener(this)
        //Click event assigned to Register button
        findViewById<TextView>(R.id.tv_register).setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.tv_forgot_password -> {
                    //Launch the forgot password screen when the user clicks on the text
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)

                }

                R.id.btn_login -> {
                    logInRegisteredUser()
                }

                R.id.tv_register -> {
                    //Launch the register screen when the user clicks on the text
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(findViewById<TextView>(R.id.et_email).text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(findViewById<TextView>(R.id.et_password).text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
//                showErrorSnackBar("Your details are valid", false)
                true
            }
        }
    }

    private fun logInRegisteredUser(){

        if (validateLoginDetails()) {

            //Show the progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))

            //Get the text from editText and trim the space
            val email = findViewById<TextView>(R.id.et_email).text.toString().trim { it <= ' '}
            val password = findViewById<TextView>(R.id.et_password).text.toString().trim { it <= ' '}

            //Log-in using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                //Hide dialog
                hideProgressDialog()

                if (task.isSuccessful) {
                    showErrorSnackBar("You are logged in successfully!", false)
                } else {
                    showErrorSnackBar(task.exception!!.message!!.toString(), true)
                }
            }
        }

    }
}