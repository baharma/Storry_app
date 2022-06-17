package com.example.submission1bahar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.submission1bahar.databinding.ActivityDetailBinding
import com.example.submission1bahar.viewmodel.Invoice
import com.example.submission1bahar.viewmodel.ListStoryItem

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var storyDetail: ListStoryItem
    private var _invois = MutableLiveData<Invoice<String>>()
    val invoice: LiveData<Invoice<String>> = _invois
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyDetail = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY) as ListStoryItem
        binding.apply {
            tvJudul.text = storyDetail.name
            tvDescription.text = storyDetail.description
            Glide.with(applicationContext)
                .load(storyDetail.photoUrl)
                .centerCrop()
                .into(imageView3)
        }

    }


    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}