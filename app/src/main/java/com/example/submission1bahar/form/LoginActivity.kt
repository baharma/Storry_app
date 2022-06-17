package com.example.submission1bahar.form

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.submission1bahar.MainActivity
import com.example.submission1bahar.R
import com.example.submission1bahar.databinding.ActivityLoginBinding
import com.example.submission1bahar.preferences.UserPreference
import com.example.submission1bahar.viewmodel.AuthViewModel
import com.example.submission1bahar.viewmodel.Invoice
import com.example.submission1bahar.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authmodel: AuthViewModel
    private var state = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        onClickLogin()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val pref = UserPreference.getInstance(dataStore)
        authmodel = ViewModelProvider(this, ViewModelFactory(pref,this))[AuthViewModel::class.java]

        authmodel.authmodel.observe(this) {
            when (it) {
                is Invoice.Success -> {
                    showLoading(false)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    state = true
                }
                is Invoice.Loading -> showLoading(true)
                is Invoice.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }
    }

    private fun onClickLogin() {

        binding.loginButton.setOnClickListener {

            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                email.isNullOrBlank() -> {
                    binding.textInputLayout.error = getString(R.string.email_dont_match)
                }
                password.isNullOrBlank() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.password_dont_match)
                }
                email.isNullOrEmpty() -> {
                    binding.textInputLayout.error = "Masukkan email"
                }
                password.isNullOrEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukan Password"
                }
                else -> {
                    closeKeyboard(this)
                    authmodel.login(email, password)
                }

            }
        }
    }


    override fun onStop() {
        super.onStop()
        if (state)
            finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        binding.progressBarLogin.visibility = if (state) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !state
    }

    private fun closeKeyboard(activity: AppCompatActivity) {
        val view: View? = activity.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}