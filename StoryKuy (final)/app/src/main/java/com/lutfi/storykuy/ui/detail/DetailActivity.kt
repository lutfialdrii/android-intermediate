package com.lutfi.storykuy.ui.detail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lutfi.storykuy.data.models.ListStoryItem
import com.lutfi.storykuy.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<ListStoryItem>(EXTRA_DETAIL, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ListStoryItem>(EXTRA_DETAIL)
        }

        if (story != null) {
            Glide.with(this).load(story.photoUrl).into(binding.ivDetailPhoto)
            binding.tvDetailDescription.text = story.description
            binding.tvDetailName.text = story.name
        }

    }

    companion object {
        const val EXTRA_DETAIL = "KEY_STORY"
    }
}