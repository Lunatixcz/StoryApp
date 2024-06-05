package com.dicoding.storyapp.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.factory.AuthViewModelFactory
import com.dicoding.storyapp.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val emailEditText = binding.emailEditText
        val emailInputLayout = binding.emailEditTextLayout
        val passwordEditText = binding.passwordEditText
        val passwordInputLayout = binding.passwordEditTextLayout

        emailInputLayout.setEditText(emailEditText)
        passwordInputLayout.setEditText(passwordEditText)

        binding.loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.login(email, password)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.loginStatus.observe(this) { loginStatus ->
            when (loginStatus) {
                is LoginViewModel.LoginStatus.Success -> {
                    showDialog("Login successful: ${loginStatus.message}", true)
                }
                is LoginViewModel.LoginStatus.Error -> {
                    showDialog("Login failed: ${loginStatus.message}", false)
                }
                is LoginViewModel.LoginStatus.NetworkError -> {
                    showDialog("Network error: ${loginStatus.message}", false)
                }
            }
        }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDialog(message: String, shouldNavigate: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setTitle("Information")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                if (shouldNavigate) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navigateToMain()
                    }, 2000)
                }
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
