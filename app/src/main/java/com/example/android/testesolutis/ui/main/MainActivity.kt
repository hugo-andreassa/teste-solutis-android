package com.example.android.testesolutis.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.android.testesolutis.R
import com.example.android.testesolutis.databinding.ActivityMainBinding
import com.example.android.testesolutis.db.getDatabase
import com.example.android.testesolutis.ui.login.LoginActivity
import com.example.android.testesolutis.ui.main.adapter.ExtratoListAdapater
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            MainViewModel.Factory(database)
        ).get(MainViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.errorMessage.observe(this, {
            it?.let {
                Snackbar
                    .make(
                        findViewById(android.R.id.content),
                        it,
                        Snackbar.LENGTH_LONG
                    )
                    .setBackgroundTint(getColor(R.color.snackbarErrorColor))
                    .show()

                viewModel.doneShowingError()
            }
        })

        setupRecyclerView()
        setupLogout()
    }

    private fun setupRecyclerView() {
        val adapter = ExtratoListAdapater()
        binding.recyclerView.adapter = adapter

        viewModel.extrato.observe(this, {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun setupLogout() {
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }

        viewModel.navigateToLoginActivity.observe(this, {
            it?.let {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                viewModel.doneNavigationToLoginActivity()
                finish()
            }
        })
    }
}