package com.diva.pimpad.ui.resultdetect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.diva.pimpad.R
import com.diva.pimpad.databinding.ActivityDetailResultDetectBinding
import com.diva.pimpad.model.ResultDetect
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DetailResultDetectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailResultDetectBinding
    private lateinit var firebaseUser: FirebaseUser
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser!!.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailResultDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        setupData()
    }

    private fun setupData() {
        val item = intent.getParcelableExtra<ResultDetect>("ResultDetect") as ResultDetect
        val actionBar = supportActionBar

        Picasso.get()
            .load(item.resultImage)
            .into(findViewById<ImageView>(R.id.image_result_detail_iv))
        binding.usernameDetailTv.text = item.username
        binding.timestampDetailTv.text = getDateTime(item.timeStamp!!)
        binding.resultDetectDetailTv.text = item.resultAcne
        actionBar?.title = "Hasil Deteksi " + item.username
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun getDateTime(timeStamp: Long) : String{
        val format = "HH:mm dd MMMM yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(Date(timeStamp))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}