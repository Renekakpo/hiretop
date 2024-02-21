package com.example.hiretop.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.hiretop.R
import com.example.hiretop.ui.screens.entreprise.EnterpriseOffersScreen
import com.example.hiretop.ui.screens.entreprise.dashboard.EnterpriseDashboardScreen
import com.example.hiretop.ui.screens.entreprise.presentation.EnterpriseProfileScreen
import com.example.hiretop.ui.screens.messaging.ChatListScreen
import com.example.hiretop.ui.screens.offers.JobOffersScreen

object EnterpriseBottomNavGraph : NavDestination {
    override val route: String = "enterprise_bottom_nav"
}

sealed class EnterpriseBottomNavScreen(val route: String, val label: String, val iconID: Int) {
    object Home : EnterpriseBottomNavScreen("home", "Accueil", R.drawable.ic_home_menu_icon)
    object Discover :
        EnterpriseBottomNavScreen("discover", "Explorer", R.drawable.ic_discover_menu_icon)

    object Tracking :
        EnterpriseBottomNavScreen("chat", "Discussion", R.drawable.ic_chat_menu_icon)

    object Profile :
        EnterpriseBottomNavScreen("profile", "Profil", R.drawable.ic_profile_menu_icon)
}

@Composable
fun EnterpriseBottomNavHost(modifier: Modifier = Modifier, navController: NavHostController) {
    val screens = listOf(
        EnterpriseBottomNavScreen.Home,
        EnterpriseBottomNavScreen.Discover,
        EnterpriseBottomNavScreen.Tracking,
        EnterpriseBottomNavScreen.Profile
    )

    val currentRoute = remember { mutableStateOf(EnterpriseBottomNavScreen.Home.route) }

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
    screens: List<EnterpriseBottomNavScreen>,
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
                        alwaysShowLabel = false,
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
            EnterpriseBottomNavScreen.Home.route -> {
                EnterpriseDashboardScreen(modifier = Modifier.padding(innerPadding))
            }

            EnterpriseBottomNavScreen.Discover.route -> {
                EnterpriseOffersScreen(modifier = Modifier.padding(innerPadding))
            }

            EnterpriseBottomNavScreen.Tracking.route -> {
                ChatListScreen(modifier = Modifier.padding(innerPadding))
            }

            EnterpriseBottomNavScreen.Profile.route -> {
                EnterpriseProfileScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}