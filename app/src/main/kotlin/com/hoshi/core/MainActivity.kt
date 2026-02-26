package com.hoshi.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hoshi.core.databinding.ActivityMainBinding
import com.hoshi.core.utils.SystemUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvVersion.text = SystemUtils.getCoreVersion()
    }

}