package com.vicky7230.mvi.presentation.ui.screens

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
import com.vicky7230.mvi.data.dto.TodoDto
import com.vicky7230.mvi.domain.model.Todo
import com.vicky7230.mvi.presentation.ui.common.LoadingUi
import com.vicky7230.mvi.presentation.state.HomeUiSideEffect
import com.vicky7230.mvi.presentation.state.HomeUiState
import com.vicky7230.mvi.presentation.viewmodel.HomeViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val homeUiState by homeViewModel.collectAsState()

    when (homeUiState) {

        is HomeUiState.Loading -> {
            LoadingUi()
        }

        is HomeUiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    imageVector = Icons.Default.SignalWifiOff,
                    contentDescription = "internet not there"
                )
                Text(text = (homeUiState as HomeUiState.Error).error, textAlign = TextAlign.Center)
                Button(onClick = {
                    homeViewModel.getTodos()
                }) {
                    Text(text = "RETRY")
                }
            }
        }

        is HomeUiState.DataRetrieved -> {
            LazyColumn {
                items(
                    (homeUiState as HomeUiState.DataRetrieved).todos,
                    key = { todo: Todo -> todo.id }) {
                    Column {
                        ListItem(it)
                    }
                }
            }
        }

        HomeUiState.Idle -> {
            //Nothing yet
        }
    }

    val context = LocalContext.current

    homeViewModel.collectSideEffect { sideEffect: HomeUiSideEffect ->
        when (sideEffect) {
            is HomeUiSideEffect.ErrorToast -> {
                Toast.makeText(context, sideEffect.msg, Toast.LENGTH_SHORT).show()
            }

            is HomeUiSideEffect.SuccessToast -> {
                Toast.makeText(context, sideEffect.msg, Toast.LENGTH_SHORT).show()
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