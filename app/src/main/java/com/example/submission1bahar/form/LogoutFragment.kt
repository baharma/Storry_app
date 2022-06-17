package com.example.submission1bahar.form

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.submission1bahar.databinding.FragmentLogoutBinding
import com.example.submission1bahar.preferences.UserPreference
import com.example.submission1bahar.viewmodel.ViewModelFactory
import com.example.submission1bahar.viewmodel.ViewModelLogout


class LogoutFragment : DialogFragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
    private var _binding: FragmentLogoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ViewModelLogout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        val view = binding.root
        val pref = UserPreference.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref, requireActivity()))[ViewModelLogout::class.java]

        setupView()
        return view
    }

    private fun setupView() {
        binding.btnCenselLogout.setOnClickListener {
            dismiss()
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }


}