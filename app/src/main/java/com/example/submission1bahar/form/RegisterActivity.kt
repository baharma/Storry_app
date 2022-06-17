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
import com.example.submission1bahar.R
import com.example.submission1bahar.databinding.ActivityRegisterBinding

import com.example.submission1bahar.preferences.UserPreference

import com.example.submission1bahar.viewmodel.AuthViewModel
import com.example.submission1bahar.viewmodel.Invoice
import com.example.submission1bahar.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupViewModel()
        onClickRegister()




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
        viewModel = ViewModelProvider(this, ViewModelFactory(pref,this))[AuthViewModel::class.java]
        viewModel.authmodel.observe(this) {
            when (it) {
                is Invoice.Success -> {
                    showLoading(false)
                    Toast.makeText(this, it.data, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }
                is Invoice.Loading -> showLoading(true)
                is Invoice.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }
    }


    private fun onClickRegister() {
        binding.btnRegister.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                email.isEmpty() -> {
                    binding.textInputLayout3.error = getString(R.string.input_name)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.input_password)
                }
                name.isEmpty() -> {
                    binding.textInputLayout2.error = getString(R.string.input_name)
                }
                else -> {
                    closeKeyboard(this)
                    viewModel.register(name, email, password)
                }
            }
        }
    }

    private fun closeKeyboard(activity: AppCompatActivity) {
        val view: View? = activity.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBarRegister.visibility = if (state) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !state
    }


}