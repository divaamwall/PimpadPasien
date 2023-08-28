package com.diva.pimpad.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.diva.pimpad.R
import com.diva.pimpad.databinding.ActivityDetailHomeBinding
import com.diva.pimpad.model.Acne
import com.squareup.picasso.Picasso

class DetailHomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailHomeBinding

    companion object {
        const val acne = "Acne"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupData()
    }

    private fun setupData() {

        val item = intent.getParcelableExtra<Acne>("Acne") as Acne
        val actionBar = supportActionBar

        Picasso.get()
            .load(item.image)
            .into(findViewById<ImageView>(R.id.imageView_detail))
        binding.nameTvDetail.text = item.name
        binding.descriptionTvDetail.text = item.description
        binding.causeTvDetail.text = item.cause
        binding.solutionTvDetail.text = item.solution
        binding.referenceTvDetail.text = item.reference

        binding.referenceTvDetail.setOnClickListener {
            openUrl(item.reference!!)
        }

        actionBar?.title = item.name
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openUrl(link: String){
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}