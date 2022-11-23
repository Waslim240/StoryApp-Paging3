package com.waslim.storyapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.waslim.storyapp.R
import com.waslim.storyapp.databinding.ItemStoryBinding
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.view.activity.DetailActivity

class StoryPagingAdapter : PagingDataAdapter<Story, StoryPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            itemView.apply {
                binding.apply {
                    Glide.with(context)
                        .load(item?.photoUrl)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .into(ivStories)
                    tvNameStories.text = item?.name
                    tvDescriptionStories.text = item?.description

                    setOnClickListener {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.DATA_STORY, item)

                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as Activity,
                                Pair(ivStories, "iv_detail"),
                                Pair(tvNameStories, "tv_name_detail"),
                                Pair(tvDescriptionStories, "tv_description_detail"),
                            )
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    inner class ViewHolder (var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK by lazy {
            object : DiffUtil.ItemCallback<Story>() {
                override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean =
                    oldItem == newItem

            }
        }
    }
}