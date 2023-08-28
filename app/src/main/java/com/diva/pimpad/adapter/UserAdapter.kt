package com.diva.pimpad.adapter

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diva.pimpad.R
import com.diva.pimpad.model.Chat
import com.diva.pimpad.model.Dokters
import com.diva.pimpad.ui.chatting.ChattingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter (private val context: Context, private val dokterList: ArrayList<Dokters>, private val statusCheck: Boolean) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    var lastMessage: String? = null
    var timeStamp: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dokterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dokter = dokterList[position]
        holder.txtUserName.text = dokter.username
        Picasso.get().load(dokter.profileImage).placeholder(R.drawable.profile).into(holder.imageUser)

        if (statusCheck) {
             retrieveLastMessage(dokter.dokterId, holder.txtLastMessage, holder.timeStamp)
        } else {
            holder.txtLastMessage.visibility = View.GONE
            holder.timeStamp.visibility = View.GONE
        }

        if (statusCheck) {
            if (dokter.status == "online") {
                holder.imgOnline.visibility = View.VISIBLE
            } else {
                holder.imgOnline.visibility = View.GONE
                holder.imgOffline.visibility = View.VISIBLE
            }
        } else {
            holder.imgOnline.visibility = View.GONE
            holder.imgOffline.visibility = View.GONE
        }

        holder.layoutUser.setOnClickListener {
            val intent = Intent(context, ChattingActivity::class.java)
            intent.putExtra("profilImage", dokter.profileImage)
            intent.putExtra("dokterId", dokter.dokterId)
            intent.putExtra("username", dokter.username)
            intent.putExtra("status", dokter.status)
            context.startActivity(intent)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtUserName: TextView = view.findViewById(R.id.username)
        val txtLastMessage: TextView = view.findViewById(R.id.lastMessage)
        val imageUser: CircleImageView = view.findViewById(R.id.userImage)
        val imgOnline: CircleImageView = view.findViewById(R.id.image_online)
        val imgOffline: CircleImageView = view.findViewById(R.id.image_offline)
        val timeStamp: TextView = view.findViewById(R.id.timeStamp)

        val layoutUser: LinearLayout = view.findViewById(R.id.layoutUser)
    }

    private fun retrieveLastMessage(chatUserId: String?, tvLastMessage: TextView?, tvTimeStamp: TextView?) {
        lastMessage = "defaultMessage"
        timeStamp = "defaultTimeStamp"

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().reference.child("Chat")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("MainActivity", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (datasnapshot in p0.children) {
                    val chat = datasnapshot.getValue(Chat::class.java)

                    if (firebaseUser != null && chat != null) {
                        if (chat.receiverId == firebaseUser.uid
                            && chat.senderId == chatUserId ||
                            chat.receiverId == chatUserId &&
                            chat.senderId == firebaseUser.uid
                        ) {
                            lastMessage = chat.message
                            timeStamp = DateUtils.getRelativeTimeSpanString(chat.timeStamp!!).toString()

                        }
                    }
                }


                when (lastMessage) {
                    "defaultMessage" -> tvLastMessage?.text = "Tidak ada pesan"
                    "sent you an image." -> tvLastMessage?.text = "Foto."
                    else -> tvLastMessage?.text = lastMessage
                }

                lastMessage = "defaultMessage"

                when (timeStamp) {
                    "defaultTimeStamp" -> tvTimeStamp?.text = ""
                    else -> tvTimeStamp?.text = timeStamp
                }

                timeStamp = "defaultTimeStamp"
            }

        })
    }
}