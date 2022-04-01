package com.example.flyretrofitcomplete.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flyretrofitcomplete.models.Destination
import com.smartherd.globofly.services.DestinationService
import com.example.flyretrofitcomplete.services.ServiceBuilder
import com.smartherd.globofly.R
import com.smartherd.globofly.databinding.ActivityDestinyCreateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DestinationCreateActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDestinyCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)


       setSupportActionBar(binding.toolbar)
        val context = this

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

       binding.btnAdd.setOnClickListener {
            val newDestination = Destination()
            newDestination.city = binding.etCity.text.toString()
            newDestination.description = binding.etDescription.text.toString()
            newDestination.country = binding.etCountry.text.toString()

            val destinationService = ServiceBuilder.buildService(DestinationService::class.java)
            val requestCall = destinationService.addDestination(newDestination)

            requestCall.enqueue(object: Callback<Destination> {

                override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
                    if (response.isSuccessful) {
                        finish() // Move back to DestinationListActivity
                        var newlyCreatedDestination = response.body() // Use it or ignore it
                        Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to add item", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Destination>, t: Throwable) {
                    Toast.makeText(context, "Failed to add item", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
