package com.ra.storyapp.adapter


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ra.storyapp.databinding.ItemListStoryBinding
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.utils.setImage

class ListStoryAdapter(
    val onItemClickCallback: (Story) -> Unit
): PagingDataAdapter<Story, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    inner class MyViewHolder(
        private val binding: ItemListStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) = with(binding) {
            imgPhoto.setImage(story.photoUrl)
            tvAuthor.text = story.name
            tvDate.text = story.createdAt.subSequence(0, 10)
            tvDescription.text = story.description

            onItemClickCallback(story)

            val animItem = ObjectAnimator.ofFloat(root, View.ALPHA, 1f).setDuration(750)
            AnimatorSet().apply {
                play(animItem)
                start()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}


