package com.dicoding.storyapp.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.databinding.ItemStoryCardBinding
import com.dicoding.storyapp.view.storydetail.DetailActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(StoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)

        if (story != null) {
            holder.bind(story)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            if (story != null) {
                intent.putExtra("id", story.id)
            }

            val imagePair = android.util.Pair.create(holder.binding.ivStoryImage as View, "image")
            val titlePair = android.util.Pair.create(holder.binding.tvStoryTitle as View, "title")
            val descPair = android.util.Pair.create(holder.binding.tvStoryDesc as View, "desc")

            val option = ActivityOptions.makeSceneTransitionAnimation(
                holder.itemView.context as Activity,
                imagePair,
                titlePair,
                descPair
            ).toBundle()

            holder.itemView.context.startActivity(intent, option)
        }
    }


    class StoryViewHolder(val binding: ItemStoryCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvStoryTitle.text = story.name
            binding.tvStoryDesc.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.ivStoryImage)
        }
    }

    class StoryDiffCallback : DiffUtil.ItemCallback<ListStoryItem>(){
        override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem == newItem
        }
    }

}