package com.markusw.cosasdeunicorapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.markusw.cosasdeunicorapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}