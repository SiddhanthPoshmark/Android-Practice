package com.example.recyclerpractice.presentation.recipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recyclerpractice.data.remote.model.Meal
import com.example.recyclerpractice.presentation.main.MainActivity
import com.example.recyclerpractice.ui.theme.RecyclerPracticeTheme
import kotlinx.coroutines.launch

class RecipeFinder : ComponentActivity() {
    private val viewModel: RecipeViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val showSheet = remember { mutableStateOf(false) }
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "composeScreen1") {
                composable("composeScreen1") {
                    Screen1(navController, viewModel, context, showSheet, sheetState)
                }
                composable("composeScreen2") { Screen2() }
                composable("composeScreen3") { Screen3() }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen1(
    navController: NavController,
    viewModel: RecipeViewModel,
    context: Context,
    showSheet: MutableState<Boolean>,
    sheetState: SheetState
) {
    RecyclerPracticeTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            HomeScreen(viewModel)
            Row(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text("Go to Main Activity")
                }
                Button(
                    onClick = { showSheet.value = true },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text("Open Bottom Bar")
                }
            }
            if (showSheet.value) {
                BottomSheet(
                    sheetState = sheetState,
                    onDismiss = { showSheet.value = false },
                    navController
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.Black,
            contentColor = Color.White,
            dragHandle = null,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Choose an Option", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                ElevatedButton(
                    onClick = {
                        scope.launch { sheetState.hide() }
                            .invokeOnCompletion {
                                if (!sheetState.isVisible) onDismiss()
                                navController.navigate("composeScreen3")
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("✨ Go to Screen 3")
                }

                Spacer(modifier = Modifier.height(12.dp))

                ElevatedButton(
                    onClick = {
                        scope.launch { sheetState.hide() }
                            .invokeOnCompletion {
                                if (!sheetState.isVisible) onDismiss()
                                navController.navigate("composeScreen2")
                            }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🚀 Go to Screen 2")
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
            color = MaterialTheme.colorScheme.error,
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
