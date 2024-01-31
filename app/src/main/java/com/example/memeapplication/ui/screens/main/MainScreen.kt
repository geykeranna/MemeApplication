package com.example.memeapplication.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.memeapplication.models.Meme
import com.example.memeapplication.ui.screens.components.MemeItem
import com.example.memeapplication.ui.screens.components.SearchView

@Composable
fun MainScreen(
    memesList: List<Meme>,
    navController: NavController
) {
     Column(
         modifier = Modifier
             .background(Color.DarkGray)
             .fillMaxSize()
     ) {
        val textState = remember {
            mutableStateOf(TextFieldValue(""))
        }

         SearchView(state = textState)

         LazyVerticalStaggeredGrid(
             columns = StaggeredGridCells.Fixed(2),
             contentPadding = PaddingValues(10.dp)
         ){
            items(
                items = memesList.filter {
                    it.name.contains(textState.value.text, ignoreCase = true)
                },
                key = {it.id}){item ->
                MemeItem(
                    itemName = item.name,
                    itemUrl = item.url,
                    modifier = Modifier.clickable {
                        navController.navigate("DetailScreen?id=${item.id}")
                    })
            }
         }

     }
}