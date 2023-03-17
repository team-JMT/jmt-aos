package org.gdsc.jmt.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import org.gdsc.jmt.databinding.ActivityMainBinding
import org.gdsc.jmt.viewmodel.WebViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val webViewModel: WebViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editText.requestFocus()
        binding.editText.setText("")

        binding.webBtn.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("url", binding.editText.text.toString())
            startActivity(intent)
        }
    }
}