package com.diva.pimpad.adapter

import android.app.Activity
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.diva.pimpad.R
import com.diva.pimpad.databinding.ItemResultDetectBinding
import com.diva.pimpad.model.ResultDetect
import com.diva.pimpad.ui.resultdetect.DetailResultDetectActivity
import com.squareup.picasso.Picasso

class ResultDetectAdapter(private val listResultDetect: ArrayList<ResultDetect>) : RecyclerView.Adapter<ResultDetectAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemResultDetectBinding) : RecyclerView.ViewHolder(binding.root) {
        val resultDetectImage: ImageView = itemView.findViewById(R.id.result_detect_iv)
        val username: TextView = itemView.findViewById(R.id.username_tv)
        val timeStamp: TextView = itemView.findViewById(R.id.timeStampResult)
        val resultAcne: TextView = itemView.findViewById(R.id.result_acne_tv)


        fun bind(item: ResultDetect) {
            Picasso.get()
                .load(item.resultImage)
                .into(resultDetectImage)
            username.text = item.username
            timeStamp.text = DateUtils.getRelativeTimeSpanString(item.timeStamp!!).toString()
            resultAcne.text = item.resultAcne
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailResultDetectActivity::class.java)
                intent.putExtra("ResultDetect", item)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(resultDetectImage, "resultImage"),
                        Pair(username, "username"),
                        Pair(timeStamp, "timeStamp"),
                        Pair(resultAcne, "resultAcne")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemResultDetectBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listResultDetect[position])
    }

    override fun getItemCount(): Int = listResultDetect.size
}