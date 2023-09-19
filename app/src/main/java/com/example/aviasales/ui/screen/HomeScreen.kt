package com.example.aviasales.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aviasales.data.Flight
import com.example.aviasales.ui.screen.query.FavoritesBody
import com.example.aviasales.ui.screen.searched.SearchedBody

enum class Airport {
    FAVORITE,
    SEARCH_AIRPORT
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    val viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.factory)

    val uiState = viewModel.uiState.collectAsState()

    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Flight Search")
                }
            ) 
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .padding(horizontal = 12.dp, vertical = 12.dp)){
            Search(viewModel = viewModel,value = uiState.value.query, onSuggestionChanged = {viewModel.updateQuery(it)} ,
                goToAirPort = {
                              viewModel.updateSelectedAirport(it)
                                viewModel.updateQuery(it.iataCode)
                        navController.navigate("query/${it.iataCode}") {
                        popUpTo("homeBody")
                        }
                }, modifier =Modifier )

            NavHost(navController = navController, startDestination = "favorite" ){
                composable("favorite"){
                    FavoritesBody()
                }
                composable("query/{iataCode}"){
                    SearchedBody()
                }
            }

        }
    }
    }




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    viewModel: HomeScreenViewModel,
    value: String,
    onSuggestionChanged: (String)-> Unit,
    goToAirPort: (Flight) -> Unit,
    modifier: Modifier = Modifier
){
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocussed by interactionSource.collectIsFocusedAsState()
    OutlinedTextField(
        value = value,
        onValueChange = {
            onSuggestionChanged(it)
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(),
        shape = CircleShape ,
        label = { Text("search") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search ,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
            )
        },
        interactionSource = interactionSource,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )

    )
    if(isFocussed){
        SuggestionList(viewModel = viewModel, goToAirPort = goToAirPort )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchAirportCard(flight: Flight,goToAirPort: (Flight) -> Unit, modifier: Modifier,){
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                focusManager.clearFocus()
                goToAirPort(flight)
            }
    ) {
        Text(text = flight.iataCode, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = modifier.width(3.dp))
        Text(text = flight.name, fontSize = 13.sp)
    }
}

@Composable
fun SuggestionList(viewModel: HomeScreenViewModel, goToAirPort: (Flight) -> Unit, modifier: Modifier = Modifier){
    val airports = viewModel.airports().collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(10.dp)
    ){
        items(airports.value.listOfAiroports){
            searchAirportCard(flight = it,goToAirPort = goToAirPort,modifier = Modifier)
        }
    }
}