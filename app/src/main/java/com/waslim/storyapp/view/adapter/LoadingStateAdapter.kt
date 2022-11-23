package com.waslim.storyapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waslim.storyapp.databinding.LoadingPageBinding

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        val binding = LoadingPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.apply {
            itemView.apply {
                binding.apply {
                    if (loadState is LoadState.Error) errorMsgPage.text = loadState.error.localizedMessage
                    progressBarPage.isVisible = loadState is LoadState.Loading
                    retryButtonPage.isVisible = loadState is LoadState.Error
                    errorMsgPage.isVisible = loadState is LoadState.Error

                    retryButtonPage.setOnClickListener {
                        retry.invoke()
                    }
                }
            }
        }
    }

    inner class LoadingStateViewHolder(var binding: LoadingPageBinding) : RecyclerView.ViewHolder(binding.root)

}