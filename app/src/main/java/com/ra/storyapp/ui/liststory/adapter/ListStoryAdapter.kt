package com.ra.storyapp.ui.liststory.adapter


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ra.storyapp.databinding.ItemListStoryBinding
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.utils.setImage

class ListStoryAdapter: RecyclerView.Adapter<ListStoryAdapter.MyViewHolder>() {

    private var stories: List<Story> = ArrayList()

    private lateinit var onClickItemCallback: OnClickItemCallback

    inner class MyViewHolder(
        private val binding: ItemListStoryBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) = with(binding)  {
            imgPhoto.setImage(story.photoUrl)
            tvAuthor.text = story.name
            tvDate.text = story.createdAt.subSequence(0, 10)
            tvDescription.text = story.description

            root.setOnClickListener {
                onClickItemCallback.detailStory(story)
            }

            val animItem = ObjectAnimator.ofFloat(root, View.ALPHA, 1f).setDuration(500)
            AnimatorSet().apply {
                play(animItem)
                start()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(stories: List<Story>) {
        this.stories = stories
        notifyDataSetChanged()
    }

    fun setOnClickItem(onClickItemCallback: OnClickItemCallback) {
        this.onClickItemCallback = onClickItemCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
       MyViewHolder(
           ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int = stories.size

    interface OnClickItemCallback {
        fun detailStory(story: Story)
    }
}


