package com.waslim.storyapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.waslim.storyapp.R
import com.waslim.storyapp.databinding.ActivityRegisterBinding
import com.waslim.storyapp.model.Result
import com.waslim.storyapp.model.showLoading
import com.waslim.storyapp.model.showToast
import com.waslim.storyapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        checkStatus()
        registerUser()
        goToLogin()
        checkLength()
    }

    private fun registerUser() = binding.btnRegister.setOnClickListener {
        checkRegister()
        closedKeyboard()
    }

    private fun checkStatus() = registerViewModel.register.observe(this) { status ->
        when (status) {
            is Result.Loading -> showLoading(true, binding.progressBarRegister)
            is Result.Failure -> {
                showLoading(false, binding.progressBarRegister)
                showToast(status.message)
            }
            is Result.Success -> {
                showLoading(false, binding.progressBarRegister)
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
                showToast(status.data.message.toString())
                finish()
            }
        }
    }

    private fun checkRegister() {
        val name = binding.etNameRegister.text.toString()
        val email = binding.etEmailRegister.text.toString()
        val password = binding.etPasswordRegister.text.toString()

        when {
            name.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 6 -> {
                registerViewModel.registerUser(name, email, password)
            }
            name.isEmpty() -> {
                showToast(getString(R.string.nama_harus_di_isi))
                showLoading(false, binding.progressBarRegister)
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast(getString(R.string.email_harus_valid))
                showLoading(false, binding.progressBarRegister)
            }
            else -> {
                showToast(getString(R.string.password_harus_6_karakter))
                showLoading(false, binding.progressBarRegister)
            }
        }
    }

    private fun goToLogin() = binding.loginHere.setOnClickListener {
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    private fun closedKeyboard() {
        val view: View? = currentFocus
        val inputMethodManager: InputMethodManager
        when {
            view != null -> {
                inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun checkLength() = binding.apply{
        etPasswordRegister.doAfterTextChanged { text ->
            when {
                text!!.length in 1..5 -> textInputLayoutPasswordRegister.isEndIconVisible = false
                text.length >= 5 || text.isEmpty() -> textInputLayoutPasswordRegister.isEndIconVisible = true
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}