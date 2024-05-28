package com.lutfi.storykuy.ui.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lutfi.storykuy.data.models.ListStoryItem
import com.lutfi.storykuy.databinding.ItemStoryBinding
import com.lutfi.storykuy.ui.detail.DetailActivity

class ListStoryAdapter :
    ListAdapter<ListStoryItem, ListStoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ctx: Context, storyItem: ListStoryItem) {
            binding.tvName.text = storyItem.name
            binding.tvDesc.text = storyItem.description
            Glide.with(itemView.context).load(storyItem.photoUrl).into(binding.ivPhoto)

            binding.root.setOnClickListener {
                Intent(ctx, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_DETAIL, storyItem)
                    ctx.startActivity(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }


    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(holder.itemView.context,getItem(position))
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