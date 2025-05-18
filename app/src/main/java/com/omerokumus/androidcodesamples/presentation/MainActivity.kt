package com.omerokumus.androidcodesamples.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.omerokumus.androidcodesamples.R
import com.omerokumus.androidcodesamples.data.ApiService
import com.omerokumus.androidcodesamples.data.RemoteDataSource
import com.omerokumus.androidcodesamples.data.Repository
import com.omerokumus.androidcodesamples.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = createViewModel().also {
            it.observeIsLoading()
            it.observeUiData()
            it.loadData()
        }
    }

    private fun MainViewModel.observeIsLoading() {
        isLoading.observe(this@MainActivity) { isLoading ->
            binding.run {
                if (isLoading) {
                    Toast.makeText(this@MainActivity, "Loading...", Toast.LENGTH_SHORT).show()
                    dataTextView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@MainActivity, "Loaded...", Toast.LENGTH_SHORT).show()
                    dataTextView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun MainViewModel.observeUiData() {
        uiData.observe(this@MainActivity) { data ->
            binding.dataTextView.visibility = View.VISIBLE
            binding.dataTextView.text = data
        }
    }

    private fun createViewModel() = MainViewModel(
        repository = Repository(
            remoteDataSource = RemoteDataSource(
                api = Retrofit.Builder()
                    .baseUrl("https://jsonplaceholder.typicode.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            ),
            sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        )
    )
}