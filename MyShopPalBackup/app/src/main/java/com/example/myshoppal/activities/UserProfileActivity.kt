
package com.example.myshoppal.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.provider.Settings.System.getString
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import java.io.IOException
import java.util.jar.Manifest

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        findViewById<EditText>(R.id.et_first_name).isEnabled = false
        findViewById<EditText>(R.id.et_first_name).setText(mUserDetails.firstName)

        findViewById<EditText>(R.id.et_last_name).isEnabled = false
        findViewById<EditText>(R.id.et_last_name).setText(mUserDetails.lastName)

        findViewById<EditText>(R.id.et_email).isEnabled = false
        findViewById<EditText>(R.id.et_email).setText(mUserDetails.email)

        findViewById<ImageView>(R.id.iv_user_photo).setOnClickListener(this@UserProfileActivity)

        findViewById<Button>(R.id.btn_submit).setOnClickListener(this@UserProfileActivity)

    }

    override fun onClick(v: View?) {
        if (v != null){
            when (v.id) {
                R.id.iv_user_photo -> {

                    //Here we will check if the permission is already allowed or we need to request for it.
                    //First of all we will check the READ_EXTERNAL_STORAGE permission and if it is not allowed we will request for it
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                Constants.showImageChooser(this)
                    } else {

                        //Requests permissions to be granted to this application. These permissions must be requested in your manifest
                            //they should not be granted to your app, and they should have protection level
                        ActivityCompat.requestPermissions(
                            this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_EXTERNAL_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit -> {
                    if (validateUserProfileDetails()){
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (mSelectedImageFileUri != null){
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                        } else {
                            updateUSerProfileDetails()
                        }
                    }
                }
            }
        }
    }

    private fun updateUSerProfileDetails(){
        val userHashMap = HashMap<String, Any>()

        val mobileNumber = findViewById<EditText>(R.id.et_mobile_number).text.toString().trim {it <= ' '}

        val gender = if (findViewById<RadioButton>(R.id.rb_male).isChecked){
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        userHashMap[Constants.GENDER] = gender

        if (mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        userHashMap[Constants.COMPLETE_PROFILE] = 1

        FirestoreClass().updateUserProfileData(this,userHashMap)
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()


        Toast.makeText(this, R.string.msg_profile_update_success, Toast.LENGTH_SHORT).show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Constants.READ_EXTERNAL_STORAGE_PERMISSION_CODE == requestCode){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                Log.e("Stop0","qwe")
                if (data != null){
                    try {
                        Log.e("Stop01","qwe")
                        // the uri of selected image from phone storage
                        mSelectedImageFileUri = data.data!!

                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!,findViewById(R.id.iv_user_photo))
//                        findViewById<ImageView>(R.id.iv_user_photo).setImageURI(Uri.parse(mSelectedImageFileUri.toString()))
                    } catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //A log is printed when user close or cancel the image selection
            Log.e("Request canceled", "Image selection cancelled")
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(findViewById<EditText>(R.id.et_mobile_number).text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageUrl: String) {

        mUserProfileImageURL = imageUrl

        updateUSerProfileDetails()
    }
}