package com.dicoding.storyapp.view.storydetail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.response.Story
import com.dicoding.storyapp.databinding.ActivityDetailBinding
import com.dicoding.storyapp.factory.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        val storyId = intent.getStringExtra("id")

        storyId?.let {
            viewModel.getStoryDetail(it)
        }

        viewModel.story.observe(this, Observer { storyResponse ->
            if (!storyResponse.error!!) {
                storyResponse.story?.let { showStoryDetails(it) }
            } else {
                showDialog("Gagal memuat cerita")
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showStoryDetails(story: Story) {
        binding.tvDetailTitle.text = story.name
        binding.tvDetailDesc.text = story.description
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.ivDetailImage)

        binding.ivDetailImage.transitionName = "image"
        binding.tvDetailTitle.transitionName = "title"
        binding.tvDetailDesc.transitionName = "desc"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setTitle("Information")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }
}