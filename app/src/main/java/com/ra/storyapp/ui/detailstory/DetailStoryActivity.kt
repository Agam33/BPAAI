package com.ra.storyapp.ui.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ra.storyapp.R
import com.ra.storyapp.databinding.ActivityDetailStoryBinding
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.utils.setImage

class DetailStoryActivity : AppCompatActivity() {

    private val viewBinding: ActivityDetailStoryBinding by lazy {
        ActivityDetailStoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        setupActionBar()
        setupView()
    }

    private fun setupActionBar() {
        supportActionBar?.title = getString(R.string.txt_detail_story)
    }

    private fun setupView() = with(viewBinding) {
        val story = intent.getParcelableExtra<Story>(EXTRA_DETAIL_STORY)
        story?.let {
            imgUserPhoto.setImage(story.photoUrl)
            tvAuthor.text = story.name
            tvDate.text = story.createdAt.subSequence(0, 10)
            tvDescription.text = story.description
        }
    }

    companion object {
        const val EXTRA_DETAIL_STORY = "detail-story"
    }
}