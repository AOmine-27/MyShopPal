package com.example.myshoppal.firestore

import android.media.session.MediaSessionManager
import android.util.Log
import com.example.myshoppal.activities.RegisterActivity
import com.example.myshoppal.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection("users")
            .document()
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                //Here call a function of a base activity for transferring the result to it.
                activity.userRegistrationSuccess() }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user",
                    e
                )
            }
    }
}