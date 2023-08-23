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
import com.google.firebase.ktx.app
import com.mehmeteminyavuz.kotlininstagramclone.R
import com.mehmeteminyavuz.kotlininstagramclone.adapter.RecyclerAdapter
import com.mehmeteminyavuz.kotlininstagramclone.databinding.ActivityProfileBinding
import com.mehmeteminyavuz.kotlininstagramclone.model.Post

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var postArrayList : ArrayList<Post>
    private lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        postArrayList = ArrayList<Post>()
        auth = Firebase.auth
        db = Firebase.firestore

        getData()

        binding.recyclerView3.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = RecyclerAdapter(postArrayList)
        binding.recyclerView3.adapter = recyclerAdapter

        setContentView(view)
    }

    private fun getData(){
        db.collection("postDetails").whereEqualTo("userEmail",auth.currentUser!!.email!!).orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(this,error.localizedMessage, Toast.LENGTH_LONG).show()
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

    fun homePageClicked(view: View){
        val intent = Intent(this,UploadActivity::class.java)
        startActivity(intent)
    }
}