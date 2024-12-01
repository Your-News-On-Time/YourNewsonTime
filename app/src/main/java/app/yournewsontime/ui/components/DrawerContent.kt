package app.yournewsontime.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.yournewsontime.R
import app.yournewsontime.data.repository.FirebaseAuthRepository
import app.yournewsontime.navigation.AppScreens
import app.yournewsontime.ui.theme.Branding_YourNewsOnTime
import app.yournewsontime.ui.theme.interFontFamily

@Composable
fun DrawerContent(
    navController: NavController,
    authRepository: FirebaseAuthRepository,
    footerHeight: Dp = 56.dp,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val currentUser = authRepository.getCurrentUser()
    val userNickname = if (currentUser?.isAnonymous == false) {
        currentUser.email?.split("@")?.get(0) ?: "Unknown"
    } else {
        "Guest"
    }

    Box(
        modifier = Modifier
            .padding(bottom = footerHeight)
            .fillMaxHeight()
            .background(Color.White)
            .width(300.dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val startX = size.width - strokeWidth / 2
                val startY = 0f
                val endX = size.width - strokeWidth / 2
                val endY = size.height
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.4f),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = strokeWidth
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Hello, $userNickname!",
                    fontSize = 20.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Branding_YourNewsOnTime,
                )

                Spacer(modifier = Modifier.height(16.dp))

                DrawerItem(
                    text = "Feed",
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.today_icon),
                            contentDescription = "Today",
                        )
                    },
                    onClick = {
                        navController.navigate(AppScreens.FeedScreen.route) {
                            popUpTo(AppScreens.FeedScreen.route) { inclusive = true }
                        }
                    }
                )

                DrawerItem(
                    text = "Saved Articles",
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.bookmark_icon),
                            contentDescription = "Saved",
                        )
                    },
                    onClick = {
                        navController.navigate(AppScreens.SavedScreen.route)
                    }
                )

                Splitter()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Following",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // TODO: Get the list of followed topics from the user's profile

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Suggested",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // TODO: Get the list of suggested topics from the categories list
            }

            if (!authRepository.isUserAnonymous()) {
                Text(
                    text = "Log out",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            authRepository.logout()
                            navController.navigate(AppScreens.LoginScreen.route) {
                                popUpTo(AppScreens.StartScreen.route) { inclusive = true }
                            }
                        }
                        .padding(vertical = 16.dp),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start
                )
            } else {
                Text(
                    text = "Sign in",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(AppScreens.LoginScreen.route) {
                                popUpTo(AppScreens.StartScreen.route) { inclusive = true }
                            }
                        }
                        .padding(vertical = 16.dp),
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun DrawerItem(
    text: String,
    icon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.invoke()
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}