package com.example.aviasales.ui.screen.query

import android.text.Editable.Factory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aviasales.data.FavoriteFlight
import com.example.aviasales.ui.screen.HomeScreenViewModel

@Composable
fun FavoritesBody(){

    val viewModel: FavoriteViewModel = viewModel(factory = FavoriteViewModel.factory)
    val FavoriteFlight = viewModel.favoriteFlights.collectAsState()
    LazyColumn(
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(FavoriteFlight.value.favoriteFlight){
                FavoriteFlightCard(onDelete = {favoriteFlight ->
                    viewModel.deleteFavoriteFlight(favoriteFlight)
                }, favoriteFlight = it )
        }
    }
}


@Composable
fun FavoriteFlightCard(onDelete: (FavoriteFlight)-> Unit,favoriteFlight: FavoriteFlight, modifier: Modifier = Modifier){
    Card(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Depart", fontSize = 17.5.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = modifier.height(5.dp))
                Text(text = favoriteFlight.departureCode, fontSize = 14.sp)
                Spacer(modifier = modifier.height(5.dp))
                Text(text = "Arrive", fontSize = 17.5.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = modifier.height(5.dp))
                Text(text = favoriteFlight.destinationCode, fontSize = 14.sp)
            }
            Column(modifier = Modifier.width(80.dp), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,) {
                IconButton(onClick = { onDelete(favoriteFlight)}) {
                    Icon(Icons.Rounded.Star , contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color.Yellow)
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewFavoriteCard(){
    FavoriteFlightCard(onDelete = {},favoriteFlight = FavoriteFlight(0,"DSD","XXX"))
}