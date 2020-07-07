package com.confessionapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.confessionapp.PostAdapter.MyViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PostAdapter(
    var mContext: Context,
    var mData: List<Post>
) : RecyclerView.Adapter<MyViewHolder>() {

    val mAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row =
            LayoutInflater.from(mContext).inflate(R.layout.row_post_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.postTextView.text = mData[position].confession
        holder.timeStampTextView.text = timesAgo(mData[position].timestamp)
        holder.yearTextView.text = mData[position].year
        holder.branchTextView.text = mData[position].branch
        holder.genderTextView.text = mData[position].gender
        holder.postNoTextView.text = "#" +mData[position].postNumber

        var isPostLiked = false

        val postID = mData[position].postID

        FirebaseDatabase.getInstance().getReference().child("likes").child(postID!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount==1L){
                    holder.likeButton.text = snapshot.childrenCount.toString() + " Like"
                } else{
                    holder.likeButton.text = snapshot.childrenCount.toString() + " Likes"
                }

                if(snapshot.child(mAuth.currentUser?.uid.toString()).exists()){
                    holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_red, 0, 0, 0)
                    holder.likeButton.setOnClickListener {
                        FirebaseDatabase.getInstance().getReference().child("likes").child(postID!!).child(mAuth.currentUser?.uid.toString()).removeValue()
                        holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_white, 0, 0, 0)
                    }
                } else {
                    holder.likeButton.setOnClickListener {
                        FirebaseDatabase.getInstance().getReference().child("likes").child(postID!!).child(mAuth.currentUser?.uid.toString()).setValue(true)
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {}
        })







    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var timeStampTextView: TextView
        var postTextView: TextView
        var yearTextView: TextView
        var branchTextView: TextView
        var genderTextView: TextView
        var postNoTextView: TextView
        var likeButton: Button

        init {
            timeStampTextView = itemView.findViewById(R.id.timeStampTextView)
            postTextView = itemView.findViewById(R.id.postTextView)
            yearTextView = itemView.findViewById(R.id.yearTextView)
            branchTextView = itemView.findViewById(R.id.branchTextView)
            genderTextView = itemView.findViewById(R.id.genderTextView)
            postNoTextView = itemView.findViewById(R.id.postNumberTextView)
            likeButton = itemView.findViewById(R.id.likeButton)

        }
    }

    fun timesAgo(time: String?): String? {
        val timeAgo2 = TimeShow()
        val MyFinalValue = timeAgo2.covertTimeToText(time)
        return MyFinalValue
    }


}