package com.example.andriodproj6

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.andriodproj6.ui.theme.AndriodProj6Theme
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler
import okhttp3.Headers
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import org.json.JSONObject
import kotlin.random.Random

data class PokemonData(
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val weight: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndriodProj6Theme {
                var pokemon by remember { mutableStateOf(PokemonData("Loading...", 0, 0, 0)) }
                var searchQuery by remember { mutableStateOf("") }

                LaunchedEffect(Unit) {
                    val randomId = Random.nextInt(1, 1025)
                    fetchData(randomId) { data ->
                        pokemon = data
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PokemonDetails(pokemon = pokemon)

                        Spacer(modifier = Modifier.height(16.dp))

                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Enter Pokémon name or ID") }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // ✅ Search button
                        Button(onClick = {
                            if (searchQuery.isNotEmpty()) {
                                fetchData(searchQuery) { data ->
                                    pokemon = data
                                }
                            }
                        }) {
                            Text("Search")
                        }


                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = {
                            val randomId = Random.nextInt(1, 1025) // Pokémon 1 to 1025
                            fetchData(randomId) { data ->
                                pokemon = data
                            }
                        }) {
                            Text(text = "Get Random Pokémon")
                        }
                    }
                }
            }
        }
        }
    }
    private fun fetchData(query: Any, onResult: (PokemonData) -> Unit) {
        val client = AsyncHttpClient()
        client.get("https://pokeapi.co/api/v2/pokemon/$query", object : TextHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, response: String) {
                val json = JSONObject(response)
                val name = json.getString("name").replaceFirstChar { it.uppercase() }
                val baseExperience = json.getInt("base_experience")
                val height = json.getInt("height")
                val weight = json.getInt("weight")
                Log.d("okay", name)
                Log.d("BE", baseExperience.toString() )
                Log.d("API_SUCCESS", "Internet works! Status code: $statusCode")
                val pokemonData = PokemonData(name, baseExperience, height, weight)
                onResult(pokemonData)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                Log.e("API_ERROR", "Error: $errorResponse", t)
            }
        })
    }



@Composable
fun PokemonDetails(pokemon: PokemonData) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Name: ${pokemon.name}")
        Text(text = "Base Experience: ${pokemon.baseExperience}")
        Text(text = "Height: ${pokemon.height}")
        Text(text = "Weight: ${pokemon.weight}")
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndriodProj6Theme {
        //PokemonDetails(pokemonData)
    }
}