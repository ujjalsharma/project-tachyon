package com.confessionapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

class CommentsViewActivity : AppCompatActivity() {

    var commentEditText: EditText? = null
    var postID: String? = null
    val mAuth = FirebaseAuth.getInstance()
    var commentRecyclerView: RecyclerView? = null
    var commentAdapter: CommentAdapter? = null
    var commentList: List<Comment>? = null
    var commentTitle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments_view)

        commentEditText = findViewById(R.id.commentEditText)

        postID = intent.getStringExtra("postID")

        commentTitle = findViewById(R.id.commentTitle)

        commentRecyclerView = findViewById(R.id.commentRV)
        commentRecyclerView?.setLayoutManager(LinearLayoutManager(applicationContext))
        commentRecyclerView?.setHasFixedSize(true)


    }

    override fun onStart() {
        super.onStart()

        // Get List Posts from the database
        FirebaseDatabase.getInstance().getReference().child("comments").child(postID!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList = ArrayList()

                commentTitle?.text = "Comments ("+dataSnapshot.childrenCount.toString()+")"

                for (commentsnap in dataSnapshot.children) {
                    val comment = commentsnap.getValue(Comment::class.java)
                    (commentList as ArrayList<Comment>).add(comment!!)
                }
                commentList = (commentList as ArrayList<Comment>).reversed().toMutableList()
                commentAdapter = CommentAdapter(applicationContext,
                    commentList as MutableList<Comment>
                )
                commentRecyclerView!!.adapter = commentAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

    fun commentSendClicked(view: View){
        try {

            val comment = commentEditText?.text.toString()
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val currentDate = sdf.format(Date())

            val commentID = UUID.randomUUID().toString()



            val commentMap: Map<String, String> = mapOf(
                "comment" to comment,
                "userID" to mAuth.currentUser?.uid.toString(),
                "timestamp" to currentDate,
                "commentID" to commentID
            )
            FirebaseDatabase.getInstance().getReference().child("comments").child(postID!!).push()
                .setValue(commentMap).addOnSuccessListener {

                    commentEditText?.setText("")

                }.addOnFailureListener {
                    Toast.makeText(this, "Failed! Please try again!", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(this, "Some Problem occured!", Toast.LENGTH_SHORT).show()
        }


    }



}