package com.example.flyretrofitcomplete.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartherd.globofly.services.MessageService
import com.example.flyretrofitcomplete.services.ServiceBuilder
import com.smartherd.globofly.R
import com.smartherd.globofly.databinding.ActivityWelcomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WelcomeActivity : AppCompatActivity() {


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


		// To be replaced by retrofit code

        val messageService = ServiceBuilder.buildService(MessageService::class.java)
        val requestCall = messageService.getMessages("http://10.0.2.2:7000/messages")

        requestCall.enqueue(object: Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val msg = response.body()
                    msg?.let {
                        binding.message.text = msg
                    }
                } else {
                    Toast.makeText(this@WelcomeActivity,
                        "Failed to retrieve items", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@WelcomeActivity,
                    "Failed to retrieve items", Toast.LENGTH_LONG).show()
            }

        })
	}

	fun getStarted(view: View) {
		val intent = Intent(this, DestinationListActivity::class.java)
		startActivity(intent)
		finish()
	}
}
