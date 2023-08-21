package com.mehmeteminyavuz.kotlininstagramclone.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.mehmeteminyavuz.kotlininstagramclone.databinding.ActivityFeedBinding
import java.util.UUID

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedImage: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

        registerLauncher()
    }

    fun addImageClicked(view:View){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_MEDIA_IMAGES)){
                Snackbar.make(view,"Permission needed for gallery!",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }.show()
            }
            else{
                permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)

            }
        }
        else{
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    fun sendClicked(view: View){
        val uuid = UUID.randomUUID()
        val imageId = "$uuid.jpg"
        val reference = storage.reference
        val imageReference=reference.child("images").child(imageId)
        if (selectedImage != null){
            imageReference.putFile(selectedImage!!).addOnSuccessListener {
                val uploadImageReference = storage.reference.child("images").child(imageId)
                uploadImageReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    if(auth.currentUser != null) {
                        val postMap = hashMapOf<String, Any>()
                        postMap.put("downloadUrl", downloadUrl)
                        postMap.put("comment",binding.editTextAddComment.text.toString())
                        postMap.put("date",com.google.firebase.Timestamp.now())
                        postMap.put("userEmail", auth.currentUser!!.email!!)

                        firestore.collection("postDetails").add(postMap).addOnSuccessListener {
                            finish()
                            Toast.makeText(this,"Post successfully shared!",Toast.LENGTH_LONG).show()

                        }.addOnFailureListener{
                            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }


                    }


                }.addOnFailureListener{
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    selectedImage = intentFromResult.data
                    selectedImage?.let {
                        binding.addImage.setImageURI(it)
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->

            if (result){
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            else {
                //permission denied
                Toast.makeText(this@FeedActivity,"Permission needed!",Toast.LENGTH_LONG)
            }

        }
    }

}