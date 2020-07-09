package com.confessionapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class LikesViewActivity : AppCompatActivity() {

    var postID: String? = null
    val mAuth = FirebaseAuth.getInstance()
    var likesRecyclerView: RecyclerView? = null
    var likesAdapter: LikesAdapter? = null
    var likesList: List<String>? = null
    var likesToolbar: androidx.appcompat.widget.Toolbar? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likes_view)
        likesToolbar = findViewById(R.id.likesToolbar)
        setSupportActionBar(likesToolbar)

        postID = intent.getStringExtra("postID")



        likesRecyclerView = findViewById(R.id.likesRV)
        likesRecyclerView?.setLayoutManager(LinearLayoutManager(applicationContext))
        likesRecyclerView?.setHasFixedSize(true)


    }

    override fun onStart() {
        super.onStart()

        FirebaseDatabase.getInstance().getReference().child("likes").child(postID!!).addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                likesList = ArrayList()

                likesToolbar?.title = "Likes ("+snapshot.childrenCount.toString()+")"



                for (likessnap in snapshot.children) {
                    val like = likessnap.key.toString()
                    (likesList as ArrayList<String>).add(like)
                }
                likesList = (likesList as ArrayList<String>).reversed().toMutableList()
                likesAdapter = LikesAdapter(applicationContext,
                    likesList as MutableList<String>
                )
                likesRecyclerView!!.adapter = likesAdapter
            }


            override fun onCancelled(error: DatabaseError) {}
        })

    }
}