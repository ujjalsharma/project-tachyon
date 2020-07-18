package com.confessionapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class ChatListActivity : AppCompatActivity() {

    var chatListRV: RecyclerView? = null
    var chatsList: List<String>? = null
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

            override fun onDataChange(snapshot: DataSnapshot) {
                chatsList = ArrayList()

                for (chatItemsnap in snapshot.children) {
                    val chatItem = chatItemsnap.child("chatID").value.toString()
                    (chatsList as ArrayList<String>).add(chatItem)
                }
                chatsList = (chatsList as ArrayList<String>).reversed().toMutableList()
                chatAdapter = ChatItemAdapter(this@ChatListActivity,
                    chatsList as MutableList<String>
                )
                chatListRV!!.adapter = chatAdapter



            }

            override fun onCancelled(error: DatabaseError) {}

        })




    }


}