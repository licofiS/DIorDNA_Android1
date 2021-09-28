package com.example.diordna_android1

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        //Register or Sign Up
        signupBtn.setOnClickListener {

            if(validation()){
                val userName = userNameText.text.toString()
                val emailOfUser: String = emailText.text.toString()
                val passwordOfUser = passwordText.text.toString()
                //function for createUserWithEmailAndPassword
                createUserWithEmailAndPassword(emailOfUser, passwordOfUser, userName)
            }
        }

        //Already Have an account Clicked
        alreadyLogin.setOnClickListener{
            val intent = Intent(applicationContext,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // It will verify that if all fields are filled are not
    private fun validation(): Boolean {

            var flag = true

            if(userNameText.text.isNullOrEmpty()) {
                userNameText?.error = "Enter Username"
                flag = false
            }
            if(emailText.text.isNullOrEmpty()){
                emailText?.error = "Enter email"
                flag = false
            }
            if(passwordText.text.isNullOrEmpty()){
                passwordText?.error = "Enter password"
                flag = false
            }
            else if(passwordText.text.length <8 || passwordText.text.length > 16){
                passwordText?.error = "Password should be between 8-16 characters"
                flag = false
            }

            return flag
        }

    //Override onstart to launch recorderActivity
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            //Toast.makeText(this,"User account is already created", Toast.LENGTH_SHORT).show()
            updateUI()
        }
    }

    //It will redirect to MainActivity
    private fun updateUI(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    //Create email and password
    private fun createUserWithEmailAndPassword(email :String, password : String, userName: String?){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    //Toast.makeText(this,"User account created", Toast.LENGTH_SHORT).show()
                    updateProfile(userName)
                    updateUI()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    Toast.makeText(this,"Try Again", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Update User profile
    private fun updateProfile(userName: String?){
        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = "$userName"
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
//                    Toast.makeText(this,"Username added", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "updateProfile: User added successfully")
                }
            }
    }
}