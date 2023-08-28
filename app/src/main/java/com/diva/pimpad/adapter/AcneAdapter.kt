package com.diva.pimpad.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.diva.pimpad.R
import com.diva.pimpad.databinding.ItemAcnesBinding
import com.diva.pimpad.model.Acne
import com.diva.pimpad.ui.detail.DetailHomeActivity
import com.squareup.picasso.Picasso

class AcneAdapter(private val listAcnes: ArrayList<Acne>) : RecyclerView.Adapter<AcneAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAcnesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listAcnes[position])
    }

    override fun getItemCount(): Int = listAcnes.size

    class ViewHolder(var binding: ItemAcnesBinding) : RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = itemView.findViewById(R.id.imageView)
        val name: TextView = itemView.findViewById(R.id.name_tv)

        fun bind(item: Acne) {
            Picasso.get()
                .load(item.image)
                .into(image)
            name.text = item.name
            Log.d("TAG: HomeFragment", "Image: " + image)
            Log.d("TAG: HomeFragment", "name" + name)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailHomeActivity::class.java)
                intent.putExtra("Acne", item)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(image, "image"),
                        Pair(name, "name")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Acne)
    }
}