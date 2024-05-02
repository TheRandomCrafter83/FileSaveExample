package com.coderzf1.filesaveexample

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.coderzf1.filesaveexample.databinding.ActivityMainBinding
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    private lateinit var fileSave:ActivityResultLauncher<String>
    private lateinit var fileLoad:ActivityResultLauncher<Array<String>>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        viewModel.data.observe(this) {
            binding.contentEdittext.setText(it)
        }

        fileSave = registerForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { uri ->
            Log.d("MAIN", "onCreate: ${uri.toString()}")
            uri?.let {
                Log.d("MAIN", "onCreate: ${binding.contentEdittext.text.toString()}")
                viewModel.saveFile(it,binding.contentEdittext.text.toString())
            }
        }

        fileLoad = registerForActivityResult(ActivityResultContracts.OpenDocument()){uri ->
            uri?.let {
                viewModel.loadFile(it)
            }
        }

        binding.saveButton.setOnClickListener {
            fileSave.launch("${UUID.randomUUID()}.txt")
        }

        binding.loadButton.setOnClickListener {
            fileLoad.launch(arrayOf("text/plain"))
        }
    }
}