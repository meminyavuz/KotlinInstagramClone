package com.mehmeteminyavuz.kotlininstagramclone.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mehmeteminyavuz.kotlininstagramclone.adapter.RecyclerAdapter
import com.mehmeteminyavuz.kotlininstagramclone.databinding.ActivityUploadBinding
import com.mehmeteminyavuz.kotlininstagramclone.model.Post

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var postArrayList : ArrayList<Post>
    private lateinit var recyclerAdapter: RecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        postArrayList = ArrayList<Post>()
        auth = Firebase.auth
        db = Firebase.firestore

        getData()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = RecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = recyclerAdapter

    }

    private fun getData(){
        db.collection("postDetails").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->


            if (error != null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if (value != null){
                    if(!value.isEmpty){
                        val docs = value.documents

                        postArrayList.clear()

                        for (document in docs){
                            val comment = document.get("comment") as String
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            val post = Post(userEmail,comment,downloadUrl)
                            postArrayList.add(post)
                        }
                        recyclerAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    fun addPostClicked(view: View){
        val intent = Intent(this, FeedActivity::class.java)
        startActivity(intent)
    }

    fun profileClicked(view: View){
        val intent = Intent(this,ProfileActivity::class.java)
        startActivity(intent)

    }
    fun logoutClicked(view: View){
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}