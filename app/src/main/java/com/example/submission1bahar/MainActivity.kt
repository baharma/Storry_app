package com.example.submission1bahar

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.myunlimitedquotes.adapter.LoadingPagingAdapter
import com.example.submission1bahar.adapter.Useradapter
import com.example.submission1bahar.camera.AddStoryActivity
import com.example.submission1bahar.databinding.ActivityMainBinding
import com.example.submission1bahar.form.LogoutFragment
import com.example.submission1bahar.maps.MapsActivity
import com.example.submission1bahar.preferences.User
import com.example.submission1bahar.preferences.UserPreference
import com.example.submission1bahar.viewmodel.MainViewModel
import com.example.submission1bahar.viewmodel.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val pref = UserPreference.getInstance(dataStore)
        var user = pref.getUserAuth()
        mainViewModel = ViewModelProvider(this, ViewModelFactory(pref,this))[MainViewModel::class.java]
        mainViewModel.getToken().observe(
            this
        ) { token: String ->
            if (token.isEmpty()) {
                val session = Intent(this, OnboardingActivity::class.java)
                startActivity(session)
                finish()
            }else{
                val testoken = token
            }
        }



        binding.fabAdd.setOnClickListener {
            Intent(this@MainActivity, AddStoryActivity::class.java).also {
                startActivity(it)
            }
        }
        showRecyclist()
    }

    private fun showRecyclist() {

        binding.apply {
            rvNotes.setHasFixedSize(true)
            val adapter = Useradapter()
            rvNotes.adapter = adapter.withLoadStateFooter(
                footer = LoadingPagingAdapter{
                    adapter.retry()
                }
            )
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvNotes.layoutManager = GridLayoutManager(this@MainActivity, 2)
            } else {
                rvNotes.layoutManager = GridLayoutManager(this@MainActivity, 1)
            }
                mainViewModel.getToken().observe(this@MainActivity){token:String ->
                    mainViewModel.getTheListStories("Bearer $token").observe(this@MainActivity){
                        adapter.submitData(lifecycle,it)
                        Log.d("test : " , token)
                    }
                }
            showLoading(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                val dialoglogout = LogoutFragment()
                dialoglogout.show(supportFragmentManager, "Logout")
                return true
            }R.id.menu2 -> {
                moveMaps()
            return true
            }
            else -> return true
        }
    }
    private fun moveMaps(){
        Intent(this,MapsActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}