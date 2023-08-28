package com.diva.pimpad.adapter

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diva.pimpad.R
import com.diva.pimpad.model.Chat
import com.diva.pimpad.ui.viewimagefullfromchat.ViewImageFullActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class ChattingAdapter (private val context: Context, private val chatList: ArrayList<Chat>, private val urlImage: String) :
    RecyclerView.Adapter<ChattingAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    var firebaseUser: FirebaseUser? = null

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (chatList[position].senderId == firebaseUser!!.uid) {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            return ViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            return ViewHolder(view)
        }
    }

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        holder.timeStamp.text = DateUtils.getRelativeTimeSpanString(chat.timeStamp!!)

        if (chat.message.equals("Photo") && !chat.urlSentImg.equals("")) {
//        Right Message
            if (chat.senderId.equals(firebaseUser!!.uid)) {
                holder.txtMessage.visibility = View.GONE
                holder.rightImageMessage.visibility = View.VISIBLE
                Glide.with(holder.itemView.context).load(chat.urlSentImg)
                    .into(holder.rightImageMessage)

                holder.rightImageMessage.setOnClickListener {
                    val options = arrayOf<CharSequence>(
                        "View Full Image",
                        "Delete Image",
                        "Cancel"
                    )
                    val builder = MaterialAlertDialogBuilder(holder.itemView.context)
                    builder.setTitle("What do you want ? ")
                    builder.setItems(options) { dialog, which ->
                        if (which == 0) {
                            val intent = Intent(
                                holder.itemView.context,
                                ViewImageFullActivity::class.java
                            ).apply {
                                putExtra(ViewImageFullActivity.URL_IMAGE, chat.urlSentImg)
                            }
                            holder.itemView.context.startActivity(intent)
                        } else if (which == 1) {
                            deleteMessage(position, holder)
                        } else {
                            dialog.cancel()
                        }
                    }
                    builder.show()
                }
            }
//        Left Message
            else if (!chat.senderId.equals(firebaseUser!!.uid)) {
                holder.txtMessage.visibility = View.GONE
                holder.leftImageMessage.visibility = View.VISIBLE
                Glide.with(holder.itemView.context).load(chat.urlSentImg)
                    .into(holder.leftImageMessage)

                holder.leftImageMessage.setOnClickListener {
                    val options = arrayOf<CharSequence>(
                        "View Full Image",
                        "Cancel"
                    )
                    val builder = MaterialAlertDialogBuilder(holder.itemView.context)
                    builder.setTitle("What do you want ? ")
                    builder.setItems(options) { dialog, which ->
                        if (which == 0) {
                            val intent = Intent(
                                holder.itemView.context,
                                ViewImageFullActivity::class.java
                            ).apply {
                                putExtra(ViewImageFullActivity.URL_IMAGE, chat.urlSentImg)
                            }
                            holder.itemView.context.startActivity(intent)
                        } else {
                            dialog.cancel()
                        }
                    }
                    builder.show()
                }
            }
        } else {
            holder.txtMessage.text = chat.message
            if (firebaseUser!!.uid == chat.senderId) {
                holder.txtMessage.setOnClickListener {
                    val options = arrayOf<CharSequence>(
                        "Delete Message",
                        "Cancel"
                    )
                    val builder = MaterialAlertDialogBuilder(holder.itemView.context)
                    builder.setTitle("What do you want ? ")
                    builder.setItems(options) { dialog, which ->
                        if(which == 0) {
                            deleteMessage(position,holder)
                        } else {
                            dialog.cancel()
                        }
                    }
                    builder.show()
                }
            }
        }

//        Sent and seen message
        if (position == chatList.size - 1) {
            if (chat.isseen) {
                holder.txtSeen.text = "Seen"
                if (chat.message.equals("Photo") && !chat.urlSentImg.equals("")) {
                    val lp = holder.txtSeen.layoutParams as LinearLayout.LayoutParams?
                    lp?.setMargins(0, 0, 0, 0)
                    holder.txtSeen.layoutParams = lp
                }
            } else {
                holder.txtSeen.text = "Sent"
                if (chat.message.equals("Photo") && !chat.urlSentImg.equals("")) {
                    val lp = holder.txtSeen.layoutParams as LinearLayout.LayoutParams?
                    lp?.setMargins(0, 0, 0, 0)
                    holder.txtSeen.layoutParams = lp
                }
            }
        } else {
            holder.txtSeen.visibility = View.GONE
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtMessage: TextView = view.findViewById(R.id.tvMessage)
        val rightImageMessage: ImageView = view.findViewById(R.id.img_message)
        val leftImageMessage: ImageView = view.findViewById(R.id.img_message)
        val txtSeen: TextView = view.findViewById(R.id.tv_seen)
        val timeStamp: TextView = view.findViewById(R.id.tvTimeStamp)
    }

    private fun deleteMessage(position: Int, holder: ChattingAdapter.ViewHolder) {
        val ref = FirebaseDatabase.getInstance().reference.child("Chat")
            .child(chatList[position].messageId!!)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Message Deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        holder.itemView.context,
                        "Failed, Message Cannot Deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}