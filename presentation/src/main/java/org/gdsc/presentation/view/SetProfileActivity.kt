
package org.gdsc.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.ActivitySetProfileBinding

class SetProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivitySetProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}