
package com.example.myshoppal.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import java.io.IOException
import java.util.jar.Manifest

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        var userDetails: User = User()

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        findViewById<EditText>(R.id.et_first_name).isEnabled = false
        findViewById<EditText>(R.id.et_first_name).setText(userDetails.firstName)

        findViewById<EditText>(R.id.et_last_name).isEnabled = false
        findViewById<EditText>(R.id.et_last_name).setText(userDetails.lastName)

        findViewById<EditText>(R.id.et_email).isEnabled = false
        findViewById<EditText>(R.id.et_email).setText(userDetails.email)

        findViewById<ImageView>(R.id.iv_user_photo).setOnClickListener(this@UserProfileActivity)

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
            }
        }
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
                if (data != null){
                    try {
                        // the uri of selected image from phone storage
                        val selectedImageFileUri = data.data!!

                        findViewById<ImageView>(R.id.iv_user_photo).setImageURI(Uri.parse(selectedImageFileUri.toString()))
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
        }
    }
}