package com.example.recyclerpractice.presentation.recipe

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recyclerpractice.data.remote.model.Meal
import com.example.recyclerpractice.presentation.main.MainActivity
import com.example.recyclerpractice.ui.theme.RecyclerPracticeTheme

class RecipeFinder : ComponentActivity() {
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            RecyclerPracticeTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    HomeScreen(viewModel)
                    Button(
                        onClick = {
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    ) {
                        Text("Go to Main Activity")
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: RecipeViewModel) {
    val state by viewModel.state
    when (state) {
        is RecipeViewState.Loading -> {
            LoadingScreen()
        }

        is RecipeViewState.Success -> {
            val recipes = (state as RecipeViewState.Success).recipes
            SuccessScreen(recipes = recipes, onSearchClick = { query ->
                viewModel.processIntent(RecipeViewIntent.SearchRecipe(query))
            })
        }

        is RecipeViewState.Error -> {
            val message = (state as RecipeViewState.Error).message
            ErrorScreen(message, onRefreshClick = {
                viewModel.processIntent(RecipeViewIntent.LoadRandomRecipe)
            })
        }
    }

    LaunchedEffect(Unit) {
        viewModel.processIntent(RecipeViewIntent.LoadRandomRecipe)
    }
}

@Composable
fun SuccessScreen(recipes: List<Meal>, onSearchClick: (query: String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Recipe Finder",
            fontWeight = FontWeight(900),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            fontFamily = FontFamily.Cursive,
            modifier = Modifier.padding(8.dp)
        )
        SearchItem(onSearchClick)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(recipes) {
                RecipeListItem(it)
            }
        }
    }
}

@Composable
fun ErrorScreen(errorMessage: String, onRefreshClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.onError,
            textAlign = TextAlign.Center
        )
        Button(onClick = onRefreshClick) {
            Text("Refresh")
        }
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text("Loading...", color = MaterialTheme.colorScheme.primary)
    }
}
