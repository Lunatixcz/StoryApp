package com.dicoding.storyapp.view.signup

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.data.api.AuthConfig
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.repository.AuthRepository
import com.dicoding.storyapp.databinding.ActivitySignupBinding
import com.dicoding.storyapp.factory.AuthViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by viewModels {
        AuthViewModelFactory(
            AuthRepository(AuthConfig.getApiService(getUserToken()), UserPreference.getInstance(this))
        )
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val emailEditText = binding.emailEditText
        val emailInputLayout = binding.emailEditTextLayout
        val passwordEditText = binding.passwordEditText
        val passwordInputLayout = binding.passwordEditTextLayout

        emailInputLayout.setEditText(emailEditText)
        passwordInputLayout.setEditText(passwordEditText)

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            signupViewModel.register(name, email, password)
        }

        signupViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        signupViewModel.registrationStatus.observe(this) { registrationStatus ->
            when (registrationStatus) {
                is SignupViewModel.RegistrationStatus.Success -> {
                    showDialog(registrationStatus.message)
                }
                is SignupViewModel.RegistrationStatus.Error -> {
                    showDialog(registrationStatus.message)
                }
                else -> { /* Do nothing for other statuses */ }
            }
        }
    }

    private fun showDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setTitle("Information")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                finish()
            }

        val dialog = builder.create()
        dialog.show()
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

    private fun getUserToken(): String {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_TOKEN, "") ?: ""
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val PREF_NAME = "UserPreferences"
        const val USER_TOKEN = "UserToken"
    }
}
