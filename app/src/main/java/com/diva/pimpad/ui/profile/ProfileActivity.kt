package com.diva.pimpad.ui.profile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.diva.pimpad.MainActivity
import com.diva.pimpad.databinding.ActivityProfileBinding
import com.diva.pimpad.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.IOException
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var selectedImage: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        loadUser()

        binding.userImage.setOnClickListener {
            chooseImage()
        }
        binding.btnSave.setOnClickListener {
            uploadImage()
            binding.progressBar.visibility = View.VISIBLE
        }

        val actionBar = supportActionBar
        actionBar?.title = "Profile"
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun chooseImage() {
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    fun loadUser() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                binding.edtUsername.setText(user!!.username)
                if (user.profileImage == ""){
                    binding.userImage.setImageResource(com.diva.pimpad.R.drawable.profile)
                } else {
                    Picasso.get()
                        .load(user.profileImage)
                        .into(binding.userImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (data.data != null) {
                selectedImage = data.data!!
                try {
                    binding.userImage.setImageURI(selectedImage)
                    binding.btnSave.visibility = View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }


            }
        }
    }

    private fun uploadImage() {
        val reference: StorageReference = storageRef.child("Profile/User").child(Date().time.toString())
        reference.putFile(selectedImage).addOnCompleteListener{
            reference.putFile(selectedImage).addOnCompleteListener {
                if (it.isSuccessful) {
                    reference.downloadUrl.addOnSuccessListener { task ->
                        uploadInfo(task.toString())
                        binding.btnSave.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun uploadInfo(imageUrl: String) {
        val user = Users(imageUrl, firebaseUser.uid, binding.edtUsername.text.toString(), status = "offline" )
        databaseReference
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this@ProfileActivity, "Data Uploaded", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                finish()
            }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}