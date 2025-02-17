package com.confessionapp

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class ChatListActivity : AppCompatActivity() {

    var chatListRV: RecyclerView? = null
    var chatsList: List<ChatItem>? = null
    val mAuth = FirebaseAuth.getInstance()
    var chatAdapter: ChatItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        chatListRV = findViewById(R.id.chatlistRV)
        chatListRV?.setLayoutManager(LinearLayoutManager(this))
        chatListRV?.setHasFixedSize(true)


    }

    override fun onStart() {
        super.onStart()


        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid.toString()).child("chats").addValueEventListener(object : ValueEventListener{

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                chatsList = ArrayList()

                var latestTimeStamp: Long? = null

                for (chatItemsnap in snapshot.children) {
                    val chatItemID = chatItemsnap.child("chatID").value.toString()

                    FirebaseDatabase.getInstance().getReference().child("chats").child(chatItemID)
                        .orderByChild("timestamp").limitToLast(1)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {}

                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (msnapshot in snapshot.children) {
                                    val latestTimeStampString = msnapshot.child("timestamp").value.toString()
                                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    val pasTime = dateFormat.parse(latestTimeStampString)
                                    latestTimeStamp = pasTime!!.time
                                }
                            }

                        })

                    val chatItem = ChatItem(chatItemID, latestTimeStamp)
                    (chatsList as ArrayList<ChatItem>).add(chatItem)
                }

                val sortedChatsList = chatsList!!.sortedWith(compareBy { it.latestTimestamp })

                chatAdapter = ChatItemAdapter(this@ChatListActivity,
                    sortedChatsList as MutableList<ChatItem>
                )
                chatListRV!!.adapter = chatAdapter



            }

            override fun onCancelled(error: DatabaseError) {}

        })




    }




}