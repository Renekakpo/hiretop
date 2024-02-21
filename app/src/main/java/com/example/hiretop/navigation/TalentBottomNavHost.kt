package com.example.hiretop.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hiretop.R
import com.example.hiretop.ui.screens.candidate.bookmark.BookmarkOffersScreen
import com.example.hiretop.ui.screens.candidate.dashboard.TalentDashboardScreen
import com.example.hiretop.ui.screens.candidate.profile.TalentProfileScreen
import com.example.hiretop.ui.screens.candidate.tracking.JobApplicationsScreen
import com.example.hiretop.ui.screens.messaging.ChatListScreen
import com.example.hiretop.ui.screens.offers.JobOffersScreen

object TalentBottomNavGraph : NavDestination {
    override val route: String = "talent_bottom_nav"
}

sealed class TalentBottomNavScreen(val route: String, val label: String, val iconID: Int) {
    object Home : TalentBottomNavScreen("home", "Accueil", R.drawable.ic_home_menu_icon)
    object Discover :
        TalentBottomNavScreen("discover", "Explorer", R.drawable.ic_discover_menu_icon)

    object Tracking : TalentBottomNavScreen("tracking", "Suivi", R.drawable.ic_tracking_menu_icon)
    object Chat : TalentBottomNavScreen("chat", "Discussion", R.drawable.ic_chat_menu_icon)
    object Bookmark : TalentBottomNavScreen("bookmark", "Signet", R.drawable.ic_bookmark_menu_icon)
    object Profile : TalentBottomNavScreen("profile", "Profil", R.drawable.ic_profile_menu_icon)
}

@Composable
fun TalentBottomNavHost(modifier: Modifier = Modifier, navController: NavHostController) {
    val screens = listOf(
        TalentBottomNavScreen.Home,
        TalentBottomNavScreen.Discover,
        TalentBottomNavScreen.Tracking,
        TalentBottomNavScreen.Chat,
        TalentBottomNavScreen.Bookmark,
        TalentBottomNavScreen.Profile
    )

    val currentRoute = remember { mutableStateOf(TalentBottomNavScreen.Home.route) }

    BottomNav(
        modifier = modifier,
        screens = screens,
        currentRoute = currentRoute,
        navController = navController
    )
}

@Composable
fun BottomNav(
    modifier: Modifier = Modifier,
    screens: List<TalentBottomNavScreen>,
    currentRoute: MutableState<String>,
    navController: NavHostController
) {
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                screens.forEach { screen ->
                    val selected = currentRoute.value == screen.route
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = screen.iconID),
                                contentDescription = screen.label,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        selected = selected,
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        ),
                        onClick = { currentRoute.value = screen.route }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Content of the selected screen
        when (currentRoute.value) {
            TalentBottomNavScreen.Home.route -> {
                TalentDashboardScreen(modifier = Modifier.padding(innerPadding))
            }

            TalentBottomNavScreen.Discover.route -> {
                JobOffersScreen(modifier = Modifier.padding(innerPadding))
            }

            TalentBottomNavScreen.Tracking.route -> {
                JobApplicationsScreen(modifier = Modifier.padding(innerPadding))
            }

            TalentBottomNavScreen.Chat.route -> {
                ChatListScreen(modifier = Modifier.padding(innerPadding))
            }

            TalentBottomNavScreen.Bookmark.route -> {
                BookmarkOffersScreen(modifier = Modifier.padding(innerPadding))
            }

            TalentBottomNavScreen.Profile.route -> {
                TalentProfileScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}