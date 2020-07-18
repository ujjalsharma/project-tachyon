package com.confessionapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class ChatItemAdapter(
    var mContext: Context,
    var mData: List<String>
) : RecyclerView.Adapter<ChatItemAdapter.MyViewHolder>() {

    val mAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row =
            LayoutInflater.from(mContext).inflate(R.layout.row_chat_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val chatID = mData[position]
        var userID: String? = null

        FirebaseDatabase.getInstance().getReference().child("users").child("chats").child(chatID).child("userID").addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                userID = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {}

        })

        holder.itemView.setOnClickListener {

            val intent = Intent(mContext, ChatActivity::class.java)
            intent.putExtra("chatID", chatID)
            intent.putExtra("userID", userID)
            mContext.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var usernamaeChatItemTV: TextView
        var profileChatItemImage: CircleImageView

        init {
            usernamaeChatItemTV = itemView.findViewById(R.id.chat_item_name)
            profileChatItemImage = itemView.findViewById(R.id.chat_item_profile_image)

        }
    }

    fun timesAgo(time: String?): String? {
        val timeAgo2 = TimeShow()
        val MyFinalValue = timeAgo2.covertTimeToText(time)
        return MyFinalValue
    }


}