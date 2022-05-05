package com.ra.storyapp.ui.liststory

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ra.storyapp.R
import com.ra.storyapp.adapter.ListStoryAdapter
import com.ra.storyapp.databinding.ActivityListStoryBinding
import com.ra.storyapp.domain.model.Story
import com.ra.storyapp.ui.addstory.AddStoryActivity
import com.ra.storyapp.ui.detailstory.DetailStoryActivity
import com.ra.storyapp.ui.detailstory.DetailStoryActivity.Companion.EXTRA_DETAIL_STORY
import com.ra.storyapp.ui.login.LoginActivity
import com.ra.storyapp.ui.maps.MapsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListStoryActivity : AppCompatActivity() {

    private val viewBinding: ActivityListStoryBinding by lazy {
        ActivityListStoryBinding.inflate(layoutInflater)
    }

    private val viewModel: ListStoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        setupActionBar()
        handleActionView()
        setListStory()
    }

    private fun setListStory() = with(viewBinding) {
        val listAdapter = ListStoryAdapter { story ->
            val intent = Intent(this@ListStoryActivity, DetailStoryActivity::class.java).apply {
                putExtra(EXTRA_DETAIL_STORY, story)
            }
            startActivity(intent)
        }
        rvListStory.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@ListStoryActivity)
            setHasFixedSize(true)
        }

        viewModel.getAllStory().observe(this@ListStoryActivity) { result ->
            listAdapter.submitData(lifecycle, result)
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
            R.id.menu_maps_view -> {
                startActivity(Intent(this@ListStoryActivity, MapsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}