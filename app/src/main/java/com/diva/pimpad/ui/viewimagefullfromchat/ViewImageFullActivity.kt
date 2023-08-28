package com.diva.pimpad.ui.viewimagefullfromchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.diva.pimpad.databinding.ActivityViewImageFullBinding

class ViewImageFullActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewImageFullBinding

    companion object {
        const val URL_IMAGE = "URL_IMAGE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewImageFullBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val urlImage = intent.getStringExtra(URL_IMAGE)
        Glide.with(this@ViewImageFullActivity).load(urlImage).into(binding.viewImageFull)
        supportActionBar!!.hide()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

}