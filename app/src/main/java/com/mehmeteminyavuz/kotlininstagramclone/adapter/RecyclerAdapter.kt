package com.mehmeteminyavuz.kotlininstagramclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mehmeteminyavuz.kotlininstagramclone.databinding.ActivityUploadBinding
import com.mehmeteminyavuz.kotlininstagramclone.databinding.RecyclerRowBinding
import com.mehmeteminyavuz.kotlininstagramclone.model.Post

class RecyclerAdapter(private val postList: ArrayList<Post>) : RecyclerView.Adapter<RecyclerAdapter.PostHolder>() {
    class PostHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerEmailText.text = postList.get(position).email
        holder.binding.recyclerCommentText.text = postList.get(position).comment


    }
}