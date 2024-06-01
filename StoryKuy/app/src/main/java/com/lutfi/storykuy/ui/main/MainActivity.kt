package com.lutfi.storykuy.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.lutfi.storykuy.R
import com.lutfi.storykuy.data.ResultState
import com.lutfi.storykuy.data.models.ListStoryItem
import com.lutfi.storykuy.databinding.ActivityMainBinding
import com.lutfi.storykuy.ui.ViewModelFactory
import com.lutfi.storykuy.ui.addstory.AddActivity
import com.lutfi.storykuy.ui.auth.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        val toolbar: MaterialToolbar = binding.toolbar
        setSupportActionBar(toolbar)

        viewModel.getLoginResult().observe(this) {
            if (it == null) {
                moveToLogin()
            } else {
                setContentView(binding.root)
                binding.rvStories.layoutManager = LinearLayoutManager(this)
                observeStories()
            }
        }

        binding.fabAddStory.setOnClickListener {
            Intent(this, AddActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun observeStories() {
        viewModel.getStories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }

                    is ResultState.Success -> {
                        Log.d(TAG, "onCreate: ${result.data.listStory}")
                        showLoading(false)
                        setData(result.data.listStory)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getLoginResult().observe(this) {
            if (it == null) {
                moveToLogin()
            } else {
                setContentView(binding.root)
                binding.rvStories.layoutManager = LinearLayoutManager(this)
                observeStories()
            }
        }
    }

    private fun setData(listStory: List<ListStoryItem?>?) {
        val adapter = ListStoryAdapter()
        adapter.submitList(listStory)
        binding.rvStories.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                viewModel.logout()
                val intent = Intent(this, LoginActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
}