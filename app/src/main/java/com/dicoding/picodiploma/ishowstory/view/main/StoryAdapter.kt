package com.dicoding.picodiploma.ishowstory.view.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.ishowstory.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.ishowstory.databinding.ItemStoryBinding

class StoryAdapter(private val stories: List<ListStoryItem?>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null


    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem?) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story?.photoUrl)
                    .into(ivStoryPhoto)
                tvStoryName.text = story?.name
                tvStoryDescription.text = story?.description

                itemView.setOnClickListener {
                    if (story != null) {
                        onItemClick?.invoke(story.id ?: "")
                    }
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                    if (story != null) {
                        intent.putExtra("id", story.id)
                    }
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size
}