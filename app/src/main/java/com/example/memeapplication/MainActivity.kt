package com.example.memeapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.memeapplication.models.Meme
import com.example.memeapplication.ui.screens.detail.DetailScreen
import com.example.memeapplication.ui.screens.main.MainScreen
import com.example.memeapplication.ui.theme.MemeApplicationTheme
import com.example.memeapplication.utils.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemeApplicationTheme {
                val navController = rememberNavController()
                var memesList by remember {
                    mutableStateOf(listOf<Meme>())
                }
                val scope = rememberCoroutineScope()

                LaunchedEffect(key1 = true){
                    scope.launch(Dispatchers.IO) {
                        val response = try {
                            RetrofitInstance.api.getMemesList()
                        } catch (e: Exception) {
                            Log.e("Exception", e.message.toString())
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "http error: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            return@launch
                        }

                        if (response.isSuccessful && response.body() != null){
                            withContext(Dispatchers.Main){
                                memesList = response.body()!!.data.memes
                            }
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = "MainScreen"){
                    composable(route = "MainScreen"){
                        MainScreen(memesList = memesList, navController = navController)
                    }
                    composable(route = "DetailScreen?id={id}",
                        arguments = listOf(
                            navArgument(name = "id"){
                                type = NavType.StringType
                            }
                        )
                    ){navEntry ->
                        val id = navEntry.arguments?.getString("id")
                        DetailScreen(
                            meme = if (id != null) memesList.filter { it.id.contains(id) }[0] else null,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}