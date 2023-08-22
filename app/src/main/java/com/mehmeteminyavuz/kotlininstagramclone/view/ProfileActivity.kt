package com.mehmeteminyavuz.kotlininstagramclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mehmeteminyavuz.kotlininstagramclone.R
import com.mehmeteminyavuz.kotlininstagramclone.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun homePageClicked(view: View){
        val intent = Intent(this,UploadActivity::class.java)
        startActivity(intent)
    }
}