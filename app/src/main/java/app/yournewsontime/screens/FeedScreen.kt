package app.yournewsontime.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.yournewsontime.data.repository.FirebaseAuthRepository
import app.yournewsontime.navigation.AppScreens
import app.yournewsontime.ui.components.AlertDialog
import app.yournewsontime.ui.components.auth.LogoutButton
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    navController: NavController,
    authRepository: FirebaseAuthRepository
) {
    Scaffold {
        FeedBodyContent(navController, authRepository)
    }
}

@Composable
fun FeedBodyContent(
    navController: NavController,
    authRepository: FirebaseAuthRepository
) {
    val scope = rememberCoroutineScope()
    val currentUser = authRepository.getCurrentUser()

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO Header

        Text(text = "Welcome, ${currentUser?.email ?: "Guest"}!")

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            // TODO: Implement The New York Times API to get the news
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                /*.clickable(onClick = {
                    scope.launch {
                        navController.navigate(route = AppScreens.ArticleScreen.route)
                    }
                })*/
            ) { }
        }

        LogoutButton(
            onClick = {
                scope.launch {
                    val result = authRepository.logout()
                    if (result.isSuccess) {
                        navController.navigate(route = AppScreens.LoginScreen.route) {
                            popUpTo(AppScreens.FeedScreen.route) { inclusive = true }
                        }
                    } else {
                        errorMessage = "Failed to log out: ${result.exceptionOrNull()?.message}"
                    }
                }
            },
            isLoggedIn = authRepository.isUserLoggedIn()
        )

        errorMessage?.let {
            AlertDialog(message = it)
            errorMessage = null
        }

        // TODO Footer
    }
}