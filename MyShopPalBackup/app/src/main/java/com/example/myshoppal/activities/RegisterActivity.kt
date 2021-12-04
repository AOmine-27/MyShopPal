package com.example.myshoppal.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.myshoppal.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupActionBar()

        findViewById<TextView>(R.id.tv_login).setOnClickListener {
//            val intent = Intent( this@RegisterActivity, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
            onBackPressed()
        }


        findViewById<TextView>(R.id.btn_register).setOnClickListener{
            registerUser()
        }
    }

    private fun setupActionBar(){

        val toolbar_register_activity = findViewById<Toolbar>(R.id.toolbar_register_activity)
        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        toolbar_register_activity?.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(findViewById<TextView>(R.id.et_first_name).text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(findViewById<TextView>(R.id.et_lat_name).text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(findViewById<TextView>(R.id.et_email).text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(findViewById<TextView>(R.id.et_password).text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(findViewById<TextView>(R.id.et_confirm_password).text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            findViewById<TextView>(R.id.et_password).text.toString().trim {it <= ' '} != findViewById<TextView>(R.id.et_confirm_password).text.toString().trim {it <= ' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }

            !findViewById<CheckBox>(R.id.cb_terms_and_condition).isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition),true)
                false
            }

            else -> {
//                showErrorSnackBar(resources.getString(R.string.msg_register_success), false)
                true
            }
        }
    }

    private fun registerUser() {
        //Check with validate function if the entries are valid or not.
        if (validateRegisterDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = findViewById<TextView>(R.id.et_email).text.toString().trim {it <= ' '}
            val password: String = findViewById<TextView>(R.id.et_password).text.toString().trim {it <= ' '}

            //create an instance and create a register a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                OnCompleteListener <AuthResult> { task ->

                    hideProgressDialog()

                    //if the registration is successfully done
                    if (task.isSuccessful) {

                        //Firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        showErrorSnackBar(
                            "You are registered successfully. Your user is is ${firebaseUser.uid}",
                            false
                        )

                        @Suppress("DEPRECATION")
                        Handler().postDelayed(
                            {
                                //Launch Main Activity
                                FirebaseAuth.getInstance().signOut()
                                finish()
                            }, 2500

                        )



                    } else {
                        //If the registering is not successful then show error message
                        showErrorSnackBar( task.exception!!.message.toString(), true)
                    }
                }
            )

        }
    }
}