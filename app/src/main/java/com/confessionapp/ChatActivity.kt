package com.confessionapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    var chatToolbar: androidx.appcompat.widget.Toolbar? = null
    var otherID: String? = null
    var ownID: String? = null
    var messagesRecyclerView: RecyclerView? = null
    var messageAdapter: MessageAdapter? = null
    var messageList: List<Message>? = null
    var messageEditText: EditText? = null
    var anonymousTV: TextView? = null
    var otherNameTV: TextView? = null
    var chatID: String? = null
    var chat_profile_image: CircleImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatToolbar = findViewById(R.id.chatToolbar)
        setSupportActionBar(chatToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        chatID = intent.getStringExtra("chatID")
        otherID = intent.getStringExtra("userID")
        ownID = mAuth.currentUser?.uid.toString()

        messageEditText = findViewById(R.id.messageEditText)
        anonymousTV = findViewById(R.id.anonymousTV)
        otherNameTV = findViewById(R.id.otherUserTextView)
        chat_profile_image = findViewById(R.id.chat_profile_image)

        messagesRecyclerView = findViewById(R.id.chatMsgRV)
        messagesRecyclerView?.setLayoutManager(LinearLayoutManager(this))
        messagesRecyclerView?.setHasFixedSize(true)


        FirebaseDatabase.getInstance().getReference().child("users").child(ownID!!)
            .child("chats").child(chatID!!).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("anonymous").value==true){
                        anonymousTV?.text = ""
                    }
                }

            })

        FirebaseDatabase.getInstance().getReference().child("users").child(otherID!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.child("chats").child(chatID!!).child("anonymous").value==true){
                        otherNameTV?.text = snapshot.child("name").value.toString()
                        Picasso.get().load(snapshot.child("profileImageURL").value.toString()).placeholder(R.drawable.profile).into(chat_profile_image)
                    }
                }

            })



    }

    override fun onStart() {
        super.onStart()

        FirebaseDatabase.getInstance().getReference().child("chats").child(chatID!!).orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                messageList = java.util.ArrayList()

                for (messagesnap in dataSnapshot.children) {
                    val message = messagesnap.getValue(Message::class.java)
                    (messageList as java.util.ArrayList<Message>).add(message!!)
                }
                messageAdapter = MessageAdapter(applicationContext,
                    messageList as MutableList<Message>
                )


                messagesRecyclerView!!.adapter = messageAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })




    }

    fun sendMessageClicked(view: View){

        val message = messageEditText?.text.toString()
        messageEditText?.setText("")
        if (message.isNotBlank()) {
            try {

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val currentDate = sdf.format(Date())

                val messageID = "message" +UUID.randomUUID().toString() + currentDate


                val messageMap: Map<String, String> = mapOf(
                    "message" to message,
                    "userID" to mAuth.currentUser?.uid.toString(),
                    "timestamp" to currentDate,
                    "chatID" to chatID!!,
                    "messageID" to messageID
                )
                FirebaseDatabase.getInstance().getReference().child("chats").child(chatID!!)
                    .child(messageID)
                    .setValue(messageMap).addOnSuccessListener {

                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed! Please try again!", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(this, "Some Problem occured!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item?.itemId == R.id.show){
            FirebaseDatabase.getInstance().getReference().child("users")
                .child(ownID!!).child("chats").child(chatID!!)
                .child("anonymous").setValue(true)

            anonymousTV?.text = ""

        }

        return super.onOptionsItemSelected(item)
    }


}