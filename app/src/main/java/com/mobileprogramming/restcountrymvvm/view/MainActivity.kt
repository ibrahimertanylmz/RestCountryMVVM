package com.mobileprogramming.restcountrymvvm.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mobileprogramming.restcountrymvvm.R
import com.mobileprogramming.restcountrymvvm.databinding.ActivityMainBinding
import com.mobileprogramming.restcountrymvvm.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor
    private lateinit var binding: ActivityMainBinding
    private var errorCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        var countryName = GET.getString("countryName", "holland")

        binding.edtCountryName.setText(countryName)
        countryName?.let {
            viewModel.refreshData(countryName)
        }
        getLiveData()

        binding.imageSearch.setOnClickListener {
            val countryName = binding.edtCountryName.text.toString()
            SET.putString("countryName", countryName)
            SET.apply()
            viewModel.refreshData(countryName)

            val inputManager:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

            getLiveData()
        }
    }
    private fun getLiveData() {
        viewModel.country_data.observe(this, Observer { data ->
            binding.imgFlag.visibility = View.VISIBLE

            val countryCode = (data[0].altSpellings[0]).toLowerCase()
            val requestOption = RequestOptions()
                .placeholder(R.drawable.loading).centerCrop()
            Glide.with(this).load("https://www.countryflags.io/$countryCode/flat/64.png")
                .apply(requestOption)
                .override(200, 200)
                .into(binding.imgFlag)

            binding.apply {
            twCapital.text = ("Capital City: " + data[0].capital)
            twPopulation.text = ("Population: " + data[0].population.toString())
            twCurrencyName.text = ("Currency Name: " + data[0].currencies[0].name)
            twSubregion.text = ("Subregion: " + data[0].subregion) }

        })

        viewModel.country_load.observe(this, Observer { load ->
            load.let {
                if (it) {
                    binding.imgFlag.visibility = View.INVISIBLE

                } else {
                    binding.imgFlag.visibility = View.VISIBLE
                }
            }
        })
        viewModel.country_error.observe(this, Observer { error ->
            Log.d("MainActivity","Error: $error")
            if (error == true) {
                errorCount++
                if (errorCount ==2) {
                Toast.makeText(this,"an Error Occured (not valid country) ",Toast.LENGTH_SHORT).show()
                    errorCount = 0
                }
            } else {
            }
        })
    }
}