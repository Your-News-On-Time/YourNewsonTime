package app.yournewsontime.ui.view.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.yournewsontime.data.repository.AuthRepository
import app.yournewsontime.ui.components.PrincipalButton
import app.yournewsontime.ui.theme.GoogleButtonColor
import app.yournewsontime.ui.theme.interFontFamily
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun RegisterView(
    navController: NavController,
    authRepository: AuthRepository = AuthRepository(FirebaseAuth.getInstance())
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(top = 45.dp)
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .height(50.dp)
        )

        Text(
            text = "Register with email",
            fontSize = 30.sp,
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier
                .height(50.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        PrincipalButton(
            "Sign up",
            onClick = {
                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    errorMessage = "Please fill in all fields"
                    return@PrincipalButton
                }
                if (password == confirmPassword) {
                    scope.launch {
                        val result = authRepository.registerWithEmailAndPassword(email, password)
                        if (result.isSuccess) {
                            navController.navigate("feed") // Navegar a Feed en éxito
                        } else {
                            errorMessage = result.exceptionOrNull()?.message
                        }
                    }
                } else {
                    errorMessage = "Passwords do not match"
                }
            }
        )

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text(
                text = "Already have an account? Log in",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                .padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* TODO Handle Google Sign In */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoogleButtonColor,
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Google Sign In")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    navController.navigate("feed")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Anonymous Sign In")
            }
        }
    }
}