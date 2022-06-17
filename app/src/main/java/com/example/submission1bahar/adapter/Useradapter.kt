package com.example.submission1bahar.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission1bahar.DetailActivity
import com.example.submission1bahar.databinding.ItemRowBinding


import com.example.submission1bahar.viewmodel.ListStoryItem

class Useradapter:PagingDataAdapter<ListStoryItem,Useradapter.UserViewHolder>(DIFF_CALLBACK) {


    inner class UserViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //add data storylist
        fun bind(user: ListStoryItem) {
            binding.apply {
                tvItemTitle.text = user.name
                descripsi.text = user.description
                Glide.with(itemView.context)
                    .load(user.photoUrl)
                    .fitCenter()
                    .into(imageView2)
            }

            // intent move detailActivity
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY, user)
                itemView.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context)
            , parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val data =  getItem(position)
        if (data!=null){
            holder.bind(data)
        }
    }



    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}