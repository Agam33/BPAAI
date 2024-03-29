package com.ra.storyapp.ui.liststory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.ra.storyapp.R
import com.ra.storyapp.databinding.ActivityListStoryBinding
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.ui.addstory.AddStoryActivity
import com.ra.storyapp.ui.detailstory.DetailStoryActivity
import com.ra.storyapp.ui.detailstory.DetailStoryActivity.Companion.EXTRA_DETAIL_STORY
import com.ra.storyapp.ui.liststory.adapter.ListStoryAdapter
import com.ra.storyapp.ui.login.LoginActivity
import com.ra.storyapp.utils.Resources
import com.ra.storyapp.utils.hideView
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListStoryActivity : AppCompatActivity(), ListStoryAdapter.OnClickItemCallback {

    private val viewBinding: ActivityListStoryBinding by lazy {
        ActivityListStoryBinding.inflate(layoutInflater)
    }

    private val viewModel: ListStoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        setupActionBar()
        handleActionView()
        setObservers()
    }

    private fun setObservers() = with(viewBinding) {
        viewModel.getAllStory().observe(this@ListStoryActivity) { result ->
            when(result) {
                is Resources.Loading -> {
                    progressBar.hideView(false)
                }
                is Resources.Success -> {
                    tvNoData.hideView(true)
                    progressBar.hideView(true)
                    result.data?.let { stories ->
                        setListStory(stories)
                    } ?: setListStory(ArrayList())
                }
                is Resources.Error -> {
                    progressBar.hideView(true)
                    tvNoData.hideView(false)
                }
            }
        }
    }

    private fun setListStory(stories: List<Story>) = with(viewBinding) {
        val listAdapter = ListStoryAdapter()
        listAdapter.setList(stories)
        listAdapter.setOnClickItem(this@ListStoryActivity)
        rvListStory.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@ListStoryActivity)
            setHasFixedSize(true)
        }
    }

    private fun setupActionBar() {
        supportActionBar?.title = getString(R.string.txt_story)
    }

    private fun handleActionView() = with(viewBinding) {
        fabAddStory.setOnClickListener {
            startActivity(Intent(this@ListStoryActivity, AddStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_logout -> {
                viewModel.signOut()
                startActivity(Intent(this@ListStoryActivity, LoginActivity::class.java))
                finish()
                true
            }
            R.id.menu_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun detailStory(story: Story) {
        val intent = Intent(this@ListStoryActivity, DetailStoryActivity::class.java).apply {
            putExtra(EXTRA_DETAIL_STORY, story)
        }
        startActivity(intent)
    }
}