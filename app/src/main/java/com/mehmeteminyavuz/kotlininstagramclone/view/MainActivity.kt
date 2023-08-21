package com.mehmeteminyavuz.kotlininstagramclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mehmeteminyavuz.kotlininstagramclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // Initialize Firebase Auth
        auth = Firebase.auth

        val currentUser = auth.currentUser


        if(currentUser != null){
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
            finish()
        }
    }




    fun signUpClicked(view: View){

        val email = binding.editTextEmailAddress.text.toString()
        val password = binding.editTextPassword.text.toString()

        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Email and Password can not be empty!",Toast.LENGTH_LONG).show()
        }
        else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {task ->
                val intent = Intent(this@MainActivity, UploadActivity::class.java)
                Toast.makeText(this,"Authentication completed successfully!",Toast.LENGTH_LONG).show()
                binding.editTextEmailAddress.text.clear()
                binding.editTextPassword.text.clear()


            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }



    fun loginClicked(view: View){

        val email = binding.editTextEmailAddress.text.toString()
        val password = binding.editTextPassword.text.toString()

        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Email and Password can not be empty!",Toast.LENGTH_LONG).show()
        }
        else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent = Intent(this@MainActivity, UploadActivity::class.java)
                startActivity(intent)
                Toast.makeText(this,"Login with ${email} email!",Toast.LENGTH_LONG).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

}