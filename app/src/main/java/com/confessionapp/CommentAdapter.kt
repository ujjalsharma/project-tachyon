package com.confessionapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter(
    var mContext: Context,
    var mData: List<Comment>
) : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    val mAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row =
            LayoutInflater.from(mContext).inflate(R.layout.row_comment_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.timestampTV.text = timesAgo(mData[position].timestamp)

        holder.commentTextView.text = mData[position].comment

        FirebaseDatabase.getInstance().getReference().child("users").child(mData[position].userID.toString()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.userCommentTV.text = snapshot.child("name").value.toString()
                Picasso.get().load(snapshot.child("profileImageURL").value.toString()).placeholder(R.drawable.profile).into(holder.userImageView)
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        holder.likeBtn.setOnClickListener {
            Toast.makeText(mContext, timesAgo(mData[position].timestamp), Toast.LENGTH_SHORT).show()
        }



    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var commentTextView: TextView
        var userCommentTV: TextView
        var timestampTV: TextView
        var userImageView: CircleImageView
        var likeBtn: Button

        init {

            commentTextView = itemView.findViewById(R.id.commentTextView)
            userCommentTV = itemView.findViewById(R.id.userCommentTV)
            timestampTV = itemView.findViewById(R.id.timestampCommentTv)
            userImageView = itemView.findViewById(R.id.comment_user_img)
            likeBtn = itemView.findViewById(R.id.commentLikeBtn)

        }
    }

    fun timesAgo(time: String?): String? {
        val timeAgo2 = TimeShow()
        val MyFinalValue = timeAgo2.covertTimeToText(time)
        return MyFinalValue
    }


}