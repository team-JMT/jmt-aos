package org.gdsc.presentation.login

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.gdsc.presentation.BaseActivity
import org.gdsc.presentation.databinding.ActivityLoginBinding

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}