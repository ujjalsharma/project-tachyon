package com.confessionapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.confessionapp.PostAdapter.MyViewHolder


class PostAdapter(
    var mContext: Context,
    var mData: List<Post>
) : RecyclerView.Adapter<MyViewHolder>() {
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

        init {
            timeStampTextView = itemView.findViewById(R.id.timeStampTextView)
            postTextView = itemView.findViewById(R.id.postTextView)
            yearTextView = itemView.findViewById(R.id.yearTextView)
            branchTextView = itemView.findViewById(R.id.branchTextView)
            genderTextView = itemView.findViewById(R.id.genderTextView)
            postNoTextView = itemView.findViewById(R.id.postNumberTextView)
        }
    }

    fun timesAgo(time: String?): String? {
        val timeAgo2 = TimeShow()
        val MyFinalValue = timeAgo2.covertTimeToText(time)
        return MyFinalValue
    }


}