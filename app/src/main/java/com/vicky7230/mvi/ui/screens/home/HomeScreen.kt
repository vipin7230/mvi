package com.vicky7230.mvi.ui.screens.home

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vicky7230.mvi.data.network.model.Todo
import com.vicky7230.mvi.ui.common.LoadingUi
import org.orbitmvi.orbit.compose.collectAsState
import timber.log.Timber

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val homeUiState by homeViewModel.collectAsState()

    if (homeUiState.loading) {
        LoadingUi()
    } else if (homeUiState.error != "null") {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = Icons.Default.SignalWifiOff,
                contentDescription = "internet not there"
            )
            Text(text = homeUiState.error, textAlign = TextAlign.Center)
            Button(onClick = {
                homeViewModel.getTodos()
            }) {
                Text(text = "RETRY")
            }
        }
    } else {
        LazyColumn {
            items(homeUiState.todos, key = { todo: Todo -> todo.id }) {
                Column {
                    ListItem(it)
                }
            }
        }
    }

}

@Composable
private fun ListItem(todo: Todo) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(start = 15.dp)
            .height(70.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = Icons.Default.Person, contentDescription = "null")
        Column(modifier = Modifier.padding(start = 15.dp)) {
            Text(
                text = todo.title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp
            )
        }
    }
    HorizontalDivider(color = Color.Black, thickness = 1.dp)
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Light Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    showSystemUi = true,
    name = "Dark Mode"
)
@Composable
fun PreviewComposable() {
    HomeScreen()
}