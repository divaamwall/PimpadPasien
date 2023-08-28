package com.diva.pimpad.ui.chatting

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.diva.pimpad.adapter.ChattingAdapter
import com.diva.pimpad.databinding.ActivityChattingBinding
import com.diva.pimpad.model.Chat
import com.diva.pimpad.model.Dokters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.ArrayList
import java.util.Date

class ChattingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChattingBinding
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    private var userVisit: String? = null
    companion object {
        val TAG = "ChattingActivity"
        private val PICK_IMAGE_REQUEST: Int = 2020
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        val intent = getIntent()
        val dokterId = intent.getStringExtra("dokterId")
        var username = intent.getStringExtra("username")
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Dokters").child(dokterId!!)
        userVisit = dokterId
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val dokter = snapshot.getValue(Dokters::class.java)
                binding.tvUsername.text = dokter!!.username

                Glide.with(applicationContext).load(dokter.profileImage)
                    .into(binding.imageProfile)
                readMessage(firebaseUser!!.uid, dokterId, dokter.profileImage)
            }
        })

        binding.btnSendMessage.setOnClickListener {
            val message: String = binding.edtMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                binding.edtMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, dokterId, message)
                binding.edtMessage.setText("")
            }
        }
        seenMessage(dokterId)
        binding.imgAttachImageFile.setOnClickListener {
            chooseImage()
        }
    }
    private fun chooseImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Pick Image"), PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val loadingBar = ProgressDialog(this)
            loadingBar.setMessage("Please wait, the image is being sent...")
            loadingBar.show()
            val fileUri = data.data
            val storageReference = FirebaseStorage.getInstance().reference.child("ImagesFromChat")
            val reference = FirebaseDatabase.getInstance().reference
            val messageIdKey = reference.push().key
            val filePath = storageReference.child("$messageIdKey.jpg")
            filePath.putFile(fileUri!!).addOnSuccessListener { it ->
//                Log.d(RegisterActivity.TAG, "Successfully uploaded image : ${it.metadata?.path}")
                filePath.downloadUrl.addOnSuccessListener {
                    val url = it.toString()

                    val hashMapMessage: HashMap<String, Any?> = HashMap()
                    hashMapMessage.put("senderId", firebaseUser!!.uid)
                    hashMapMessage.put("receiverId", userVisit)
                    hashMapMessage.put("message", "Photo")
                    hashMapMessage.put("isseen", false)
                    hashMapMessage.put("urlSentImg", url)
                    hashMapMessage.put("timeStamp", Date().time)
                    hashMapMessage.put("messageId", messageIdKey)

                    reference.child("Chat").child(messageIdKey!!).setValue(hashMapMessage)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                loadingBar.dismiss()
                            }
                        }
                }
            }

        }
    }
    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val messageIdKey = reference!!.push().key
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)
        hashMap.put("isseen", false)
        hashMap.put("urlSentImg", "")
        hashMap.put("timeStamp", Date().time)
        hashMap.put("messageId", messageIdKey)
        reference.child("Chat").push().setValue(hashMap)
    }
    fun readMessage(senderId: String, receiverId: String, receiverUrlImage: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)
                    if (chat!!.senderId.equals(senderId) && chat.receiverId.equals(receiverId) ||
                        chat.senderId.equals(receiverId) && chat.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }
                val chatAdapter = ChattingAdapter(this@ChattingActivity, chatList,receiverUrlImage)
                binding.chatRecyclerView.layoutManager = LinearLayoutManager(this@ChattingActivity, LinearLayout.VERTICAL, false)
                binding.chatRecyclerView.adapter = chatAdapter
            }
        })
    }
    private var seenListener: ValueEventListener? = null
    private fun seenMessage(dokterId: String) {
        val reference = FirebaseDatabase.getInstance().reference.child("Chat")
        seenListener = reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                for (snapshot in datasnapshot.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat?.receiverId.equals(firebaseUser?.uid) && chat?.senderId.equals(dokterId)) {
                        val hashMap = HashMap<String, Any>()
                        hashMap.put("isseen", true)
                        snapshot.ref.updateChildren(hashMap)
                    }
                }

            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d(TAG, p0.message)
            }

        })
    }
    override fun onPause() {
        super.onPause()
        reference!!.removeEventListener(seenListener!!)
    }
}