package app.yournewsontime.ui.components.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.yournewsontime.data.repository.AuthRepository

@Composable
fun LogoutButton(navController: NavController, authRepository: AuthRepository) {
    Button(
        onClick = {
            val result = authRepository.logout()
            if (result.isSuccess) {
                navController.navigate("startPage") {
                    popUpTo("feed") {
                        inclusive = true
                    }
                }
            } else {
                println("Logout failed: ${result.exceptionOrNull()?.message}")
            }
        },
        //enabled = authRepository.isUserLoggedIn(),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        if (authRepository.isUserLoggedIn()) {
            Text(text = "Logout")
        } else {
            Text(text = "Login")
        }
    }
}