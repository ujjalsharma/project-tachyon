package com.confessionapp

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
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
        val ownID = mAuth.currentUser?.uid.toString()

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid.toString())
            .child("chats").child(chatID).child("userID")
            .addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                userID = snapshot.value.toString()

                FirebaseDatabase.getInstance().getReference().child("users").child(userID!!)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {}

                        override fun onDataChange(snapshot: DataSnapshot) {

                            if (snapshot.child("chats").child(chatID).child("anonymous").value==true){
                                holder.usernamaeChatItemTV.text = snapshot.child("name").value.toString()
                                Picasso.get().load(snapshot.child("profileImageURL").value.toString()).placeholder(R.drawable.profile).into(holder.profileChatItemImage)
                            }
                        }

                    })

                FirebaseDatabase.getInstance().getReference().child("chats").child(chatID)
                    .orderByChild("timestamp").limitToLast(1)
                    .addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {}

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (msnapshot in dataSnapshot.children){
                                holder.timeChatItemTV.text = timesAgo(msnapshot.child("timestamp").value.toString())
                                val message =  msnapshot.child("message").value.toString()
                                val charCount = message.length

                                if (msnapshot.child("userID").value.toString()==ownID){
                                    if (charCount>23){
                                        holder.messageChatItemTV.text ="~ " + message.substring(0, 23) + "..."
                                    } else {
                                        holder.messageChatItemTV.text ="~ " + message
                                    }
                                } else {

                                    if (msnapshot.child("read").value==true){
                                        if (charCount>23){
                                            holder.messageChatItemTV.text = message.substring(0, 23) + "..."
                                        } else {
                                            holder.messageChatItemTV.text = message
                                        }
                                    } else {
                                        holder.messageChatItemTV.setTypeface(holder.messageChatItemTV.getTypeface(), Typeface.BOLD)
                                        if (charCount>23){
                                            holder.messageChatItemTV.text = message.substring(0, 23) + "..."
                                        } else {
                                            holder.messageChatItemTV.text = message
                                        }
                                    }

                                    if (charCount>23){
                                        holder.messageChatItemTV.text = message.substring(0, 23) + "..."
                                    } else {
                                        holder.messageChatItemTV.text = message
                                    }
                                }


                            }

                        }

                    })


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
        var timeChatItemTV: TextView
        var messageChatItemTV: TextView
        var profileChatItemImage: CircleImageView

        init {
            usernamaeChatItemTV = itemView.findViewById(R.id.chat_item_name)
            messageChatItemTV = itemView.findViewById(R.id.chat_message_item)
            timeChatItemTV = itemView.findViewById(R.id.chat_message_item_time)
            profileChatItemImage = itemView.findViewById(R.id.chat_item_profile_image)

        }
    }

    fun timesAgo(time: String?): String? {
        val timeAgo2 = TimeShow()
        val MyFinalValue = timeAgo2.covertTimeToText(time)
        return MyFinalValue
    }


}