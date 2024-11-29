package app.yournewsontime.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.yournewsontime.data.model.Article
import app.yournewsontime.data.repository.FirebaseAuthRepository
import app.yournewsontime.ui.components.AlertDialog
import app.yournewsontime.ui.components.Footer
import app.yournewsontime.viewmodel.NewYorkTimesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    navController: NavController,
    authRepository: FirebaseAuthRepository,
    viewModel: NewYorkTimesViewModel,
    apiKey: String
) {
    val currentUser = authRepository.getCurrentUser()

    val userNickname = if (currentUser?.isAnonymous == false) {
        currentUser.email?.split("@")?.get(0) ?: "Unknown"
    } else {
        "Guest"
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hello, $userNickname!") },
                colors = TopAppBarColors(
                    titleContentColor = Color.Black,
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = Color.Transparent,
                    actionIconContentColor = Color.Transparent,
                )
            )
        },
    ) { padding ->
        FeedBodyContent(
            navController,
            authRepository,
            viewModel,
            apiKey,
            padding
        )
    }
}

@Composable
fun FeedBodyContent(
    navController: NavController,
    authRepository: FirebaseAuthRepository,
    viewModel: NewYorkTimesViewModel,
    apiKey: String,
    padding: PaddingValues
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val articles by viewModel.articles
    val error by viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.fetchArticles("technology", apiKey)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (error != null) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(articles) { article ->
                        ArticleItem(article)
                    }
                }
            }

            Footer(
                navController = navController,
                authRepository = authRepository,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }

        errorMessage?.let {
            AlertDialog(message = it)
            errorMessage = null
        }
    }
}

@Composable
fun ArticleItem(article: Article) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = article.headline.main, style = MaterialTheme.typography.titleMedium)
        Text(text = article.snippet, style = MaterialTheme.typography.bodySmall)
    }
}