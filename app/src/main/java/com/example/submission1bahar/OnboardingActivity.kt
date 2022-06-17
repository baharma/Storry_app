package com.example.submission1bahar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.submission1bahar.databinding.ActivityOnboardingBinding
import com.example.submission1bahar.form.LoginActivity
import com.example.submission1bahar.form.RegisterActivity
import com.example.submission1bahar.preferences.UserPreference
import com.example.submission1bahar.viewmodel.ViewModelFactory
import com.example.submission1bahar.viewmodel.ViewModelLogout

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class OnboardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnboardingBinding
    lateinit var logout: ViewModelLogout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = UserPreference.getInstance(dataStore)

        logout = ViewModelProvider(this, ViewModelFactory(pref,this))[ViewModelLogout::class.java]
        logout.getToken().observe(
            this
        ) { token: String ->
            if (token.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // inten login and register
        binding.loginid.setOnClickListener {
            startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
        }
        binding.registerid.setOnClickListener {
            startActivity(Intent(this@OnboardingActivity, RegisterActivity::class.java))
        }

        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView4, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val login = ObjectAnimator.ofFloat(binding.loginid, View.ALPHA, 1f).setDuration(1000)
        val signup = ObjectAnimator.ofFloat(binding.registerid, View.ALPHA, 1f).setDuration(1000)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }


        AnimatorSet().apply {
            playSequentially(together)
            start()
        }
    }
}