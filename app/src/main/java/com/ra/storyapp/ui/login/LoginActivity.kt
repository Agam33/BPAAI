package com.ra.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.ra.storyapp.R
import com.ra.storyapp.databinding.ActivityLoginBinding
import com.ra.storyapp.domain.model.LoginResult
import com.ra.storyapp.ui.liststory.ListStoryActivity
import com.ra.storyapp.ui.register.RegisterActivity
import com.ra.storyapp.utils.Resources
import com.ra.storyapp.utils.hideView
import com.ra.storyapp.utils.isValidEmail
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val viewBinding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModel()

    override fun onStart() {
        super.onStart()
        viewModel.checkVerification()
        viewModel.isVerified.observe(this@LoginActivity) { event ->
            event.getContentIfNotHandled()?.let { state ->
                if(state) {
                    startActivity(Intent(this@LoginActivity, ListStoryActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        hideSystemUI()
        handleActionView()
        playAnimation()
    }

    private fun playAnimation() = with(viewBinding) {
        val animImgPhoto = ObjectAnimator.ofFloat(imgPhoto, View.ALPHA, 1f).setDuration(500)
        val animTvTitle = ObjectAnimator.ofFloat(tvTitle, View.ALPHA, 1f).setDuration(500)
        val animLayoutEdtEmail = ObjectAnimator.ofFloat(layoutEdtEmail, View.ALPHA, 1f).setDuration(500)
        val animLayoutEdtPass = ObjectAnimator.ofFloat(layoutEdtPassword, View.ALPHA, 1f).setDuration(500)
        val animBtnSignIn = ObjectAnimator.ofFloat(btnSignIn, View.ALPHA, 1f).setDuration(500)
        val animTvNewAccount = ObjectAnimator.ofFloat(tvCreateNewAccount, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            play(animImgPhoto).with(animTvTitle)
            playSequentially(animLayoutEdtEmail, animLayoutEdtPass, animBtnSignIn, animTvNewAccount)
            start()
        }
    }

    private fun setPreferences(loginResult: LoginResult?) {
        viewModel.saveToken(loginResult?.token)
        viewModel.saveVerification(true)
    }

    private fun handleActionView() = with(viewBinding) {
         tvCreateNewAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
         }

        btnSignIn.setOnClickListener {
           checkUserInput()
        }
    }

    private fun checkUserInput() = with(viewBinding) {
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()

        if(email.isEmpty()) {
            edtEmail.error = getString(R.string.txt_empty_column)
            return
        }
        if(password.isEmpty()) {
            edtPassword.error = getString(R.string.txt_empty_column)
            return
        }
        if(isValidEmail(email) && password.length < 6) return

        viewModel.loginWithEmailAndPassword(email, password)
        viewModel.loginResult.observe(this@LoginActivity) { result ->
            when(result) {
                is Resources.Loading -> {
                    progressBar.hideView(false)
                    disableOrEnableView(false)
                }
                is Resources.Success -> {
                    disableOrEnableView(false)
                    progressBar.hideView(true)
                    setPreferences(result.data)
                    val intent = Intent(this@LoginActivity, ListStoryActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is Resources.Error -> {
                    disableOrEnableView(true)
                    progressBar.hideView(true)
                    Toast.makeText(this@LoginActivity, getString(R.string.wrong_email_or_password), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun hideSystemUI() {
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

    private fun disableOrEnableView(state: Boolean) = with(viewBinding) {
        edtPassword.isEnabled = state
        edtEmail.isEnabled = state
        btnSignIn.isEnabled = state
        tvCreateNewAccount.isEnabled = state
    }
}