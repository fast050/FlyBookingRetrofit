package com.example.flyretrofitcomplete.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.flyretrofitcomplete.models.Destination
import com.smartherd.globofly.services.DestinationService
import com.example.flyretrofitcomplete.services.ServiceBuilder
import com.smartherd.globofly.R
import com.smartherd.globofly.databinding.ActivityDestinyDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DestinationDetailActivity : AppCompatActivity() {

    lateinit var binding:ActivityDestinyDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDestinyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.detailToolbar)
        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras

        if (bundle?.containsKey(ARG_ITEM_ID)!!) {

            val id = intent.getIntExtra(ARG_ITEM_ID, 0)

            binding.loadDetails(id)

            binding.initUpdateButton(id)

            binding.initDeleteButton(id)
        }
    }

    private fun ActivityDestinyDetailBinding.loadDetails(id: Int) {

        val destinationService = ServiceBuilder.buildService(DestinationService::class.java)
        val requestCall = destinationService.getDestination(id)

        requestCall.enqueue(object : retrofit2.Callback<Destination> {

            override fun onResponse(call: Call<Destination>, response: Response<Destination>) {

                if (response.isSuccessful) {
                    val destination = response.body()
                    destination?.let {
                        etCity.setText(destination.city)
                        etDescription.setText(destination.description)
                        etCountry.setText(destination.country)

                        collapsingToolbar.title = destination.city
                    }
                } else {
                    Toast.makeText(this@DestinationDetailActivity, "Failed to retrieve details", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Destination>, t: Throwable) {
                Toast.makeText(
                    this@DestinationDetailActivity,
                    "Failed to retrieve details " + t.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun ActivityDestinyDetailBinding.initUpdateButton(id: Int) {

        binding.btnUpdate.setOnClickListener {

            val city = etCity.text.toString()
            val description = etDescription.text.toString()
            val country = etCountry.text.toString()

            val destinationService = ServiceBuilder.buildService(DestinationService::class.java)
            val requestCall = destinationService.updateDestination(id, city, description, country)

            requestCall.enqueue(object: Callback<Destination> {

                override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
                    if (response.isSuccessful) {
                        finish() // Move back to DestinationListActivity
                        var updatedDestination = response.body() // Use it or ignore It
                        Toast.makeText(this@DestinationDetailActivity,
                            "Item Updated Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DestinationDetailActivity,
                            "Failed to update item", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Destination>, t: Throwable) {
                    Toast.makeText(this@DestinationDetailActivity,
                        "Failed to update item", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun ActivityDestinyDetailBinding.initDeleteButton(id: Int) {

        btnDelete.setOnClickListener {

            val destinationService = ServiceBuilder.buildService(DestinationService::class.java)
            val requestCall = destinationService.deleteDestination(id)

            requestCall.enqueue(object: Callback<Unit> {

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        finish() // Move back to DestinationListActivity
                        Toast.makeText(this@DestinationDetailActivity, "Successfully Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DestinationDetailActivity, "Failed to Delete", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(this@DestinationDetailActivity, "Failed to Delete", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            navigateUpTo(Intent(this, DestinationListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        const val ARG_ITEM_ID = "item_id"
    }
}
