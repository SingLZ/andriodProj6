package com.example.andriodproj6

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler
import okhttp3.Headers
import org.json.JSONObject
import kotlin.random.Random

data class PokemonData(
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val weight: Int
)

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PokemonAdapter(listOf())
        recyclerView.adapter = adapter

        val btnLoadRandom: Button = findViewById(R.id.btnLoadRandom)
        btnLoadRandom.setOnClickListener {
            loadRandomPokemonList()
        }

        // Load initial list
        loadRandomPokemonList()
    }

    private fun loadRandomPokemonList() {
        val client = AsyncHttpClient()
        val count = 10
        val list = mutableListOf<PokemonData>()
        var responsesReceived = 0

        for (i in 1..count) {
            val randomId = Random.nextInt(1, 1025)
            client.get("https://pokeapi.co/api/v2/pokemon/$randomId", object : TextHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Headers, response: String) {
                    val json = JSONObject(response)
                    val name = json.getString("name").replaceFirstChar { it.uppercase() }
                    val baseExperience = json.getInt("base_experience")
                    val height = json.getInt("height")
                    val weight = json.getInt("weight")

                    synchronized(list) {
                        list.add(PokemonData(name, baseExperience, height, weight))
                    }

                    responsesReceived++
                    if (responsesReceived == count) {
                        runOnUiThread {
                            adapter.updateData(list)
                        }
                    }
                }

                override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, t: Throwable?) {
                    Log.e("API_ERROR", "Error: $errorResponse", t)
                    responsesReceived++
                    if (responsesReceived == count) {
                        runOnUiThread {
                            adapter.updateData(list)
                        }
                    }
                }
            })
        }
    }
}
