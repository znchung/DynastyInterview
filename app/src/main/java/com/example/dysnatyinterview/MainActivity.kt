package com.example.dysnatyinterview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dysnatyinterview.ui.app.AllListings
import com.example.dysnatyinterview.ui.app.ArticleDetails
import com.example.dysnatyinterview.ui.theme.DysnatyInterviewTheme
import com.example.dysnatyinterview.viewmodel.ListingViewModel

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    val listingViewModel by viewModels<ListingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            DysnatyInterviewTheme {
                NavHost(navController = navController, startDestination = "listings") {
                    composable("listings") {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.background
                        ) {
                            AllListings(navController, listingViewModel)
                        }
                    }
                    composable("articleDetails") {
                        ArticleDetails(article = listingViewModel.clickedItem)
                    }
                }
                // A surface container using the 'background' color from the theme
//                ListingViewModel().getListings()
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Greeting("Android")
//                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    DysnatyInterviewTheme {
//        Greeting("Android")
//    }
//}