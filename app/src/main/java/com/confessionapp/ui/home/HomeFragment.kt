package com.confessionapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.confessionapp.Post
import com.confessionapp.PostAdapter
import com.confessionapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    var postRecyclerView: RecyclerView? = null
    var postAdapter: PostAdapter? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var postList: List<Post>? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentView: View =
            inflater.inflate(R.layout.fragment_home, container, false)
        postRecyclerView = fragmentView.findViewById(R.id.postRV)
        postRecyclerView?.setLayoutManager(LinearLayoutManager(activity))
        postRecyclerView?.setHasFixedSize(true)

        return fragmentView
    }

    override fun onStart() {
        super.onStart()



        // Get List Posts from the database
        FirebaseDatabase.getInstance().getReference().child("confessions").orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList = ArrayList()
                for (postsnap in dataSnapshot.children) {
                    val post = postsnap.getValue(Post::class.java)
                    (postList as ArrayList<Post>).add(post!!)
                }
                postList = (postList as ArrayList<Post>).reversed().toMutableList()
                postAdapter = activity?.let { PostAdapter(it, postList as ArrayList<Post>) }
                postRecyclerView!!.adapter = postAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


}