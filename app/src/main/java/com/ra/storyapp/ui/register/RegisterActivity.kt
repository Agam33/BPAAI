package com.ra.storyapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ra.storyapp.R
import com.ra.storyapp.databinding.ActivityRegisterBinding
import com.ra.storyapp.ui.login.LoginActivity
import com.ra.storyapp.utils.Resources
import com.ra.storyapp.utils.isValidEmail
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val viewBinding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        setupActionBar()
        handleActionView()
    }

    private fun setupActionBar() {
        supportActionBar?.title = getString(R.string.txt_registration)
    }

    private fun handleActionView() = with(viewBinding) {
        btnCreateAccount.setOnClickListener {
            checkUserInput()
        }
    }

    private fun checkUserInput() = with(viewBinding) {
        val name: String = edtName.text.toString()
        val email: String = edtEmail.text.toString()
        val password: String = edtPassword.text.toString()

        if(name.isEmpty()) {
            edtName.error = getString(R.string.txt_empty_column)
            return
        }
        if(email.isEmpty()) {
            edtEmail.error = getString(R.string.txt_empty_column)
            return
        }
        if(password.isEmpty()) {
            edtPassword.error = getString(R.string.txt_empty_column)
            return
        }
        if(!isValidEmail(email) && password.length < 6) return

        viewModel.register(name, email, password)
        viewModel.registerResult.observe(this@RegisterActivity) { result ->
            when(result) {
                is Resources.Loading -> {}
                is Resources.Success -> {
                    Toast.makeText(this@RegisterActivity, getString(R.string.txt_successful), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
                is Resources.Error -> {
                    Toast.makeText(this@RegisterActivity, "${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}