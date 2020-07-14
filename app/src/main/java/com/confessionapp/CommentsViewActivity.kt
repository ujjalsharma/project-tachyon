package com.confessionapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

class CommentsViewActivity : AppCompatActivity() {

    var commentEditText: EditText? = null
    var postID: String? = null
    val mAuth = FirebaseAuth.getInstance()
    var commentRecyclerView: RecyclerView? = null
    var commentAdapter: CommentAdapter? = null
    var commentList: List<Comment>? = null
    var commentsToolbar: Toolbar? = null
    var commentProfileImage: CircleImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments_view)
        commentsToolbar = findViewById(R.id.commentsToolbar)
        setSupportActionBar(commentsToolbar)

        commentEditText = findViewById(R.id.commentEditText)

        postID = intent.getStringExtra("postID")

        commentProfileImage = findViewById(R.id.comment_own_profile)


        commentRecyclerView = findViewById(R.id.commentRV)
        commentRecyclerView?.setLayoutManager(LinearLayoutManager(this))
        commentRecyclerView?.setHasFixedSize(true)

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid.toString()).addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                Picasso.get().load(snapshot.child("profileImageURL").value.toString()).placeholder(R.drawable.profile).into(commentProfileImage)
            }

            override fun onCancelled(error: DatabaseError) {}
        })


    }

    override fun onStart() {
        super.onStart()

        // Get List Posts from the database
        FirebaseDatabase.getInstance().getReference().child("comments").child(postID!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList = ArrayList()

                commentsToolbar?.title = "Comments ("+dataSnapshot.childrenCount.toString()+")"

                for (commentsnap in dataSnapshot.children) {
                    val comment = commentsnap.getValue(Comment::class.java)
                    (commentList as ArrayList<Comment>).add(comment!!)
                }
                commentList = (commentList as ArrayList<Comment>).reversed().toMutableList()
                commentAdapter = CommentAdapter(this@CommentsViewActivity,
                    commentList as MutableList<Comment>
                )
                commentRecyclerView!!.adapter = commentAdapter

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

    fun commentSendClicked(view: View){

        val comment = commentEditText?.text.toString()
        if (comment.isNotBlank()) {
            try {

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val currentDate = sdf.format(Date())

                val commentID = UUID.randomUUID().toString()


                val commentMap: Map<String, String> = mapOf(
                    "comment" to comment,
                    "userID" to mAuth.currentUser?.uid.toString(),
                    "timestamp" to currentDate,
                    "commentID" to commentID
                )
                FirebaseDatabase.getInstance().getReference().child("comments").child(postID!!)
                    .push()
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





}