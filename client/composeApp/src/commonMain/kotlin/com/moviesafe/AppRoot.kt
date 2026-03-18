package com.moviesafe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import com.moviesafe.theme.MovieSafeTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.moviesafe.ui.SearchScreen

@Composable
fun AppRoot() {
    MovieSafeTheme {
        val navController = rememberNavController()

        Scaffold { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Route.Search,
                modifier = Modifier.padding(paddingValues),
            ) {
                composable<Route.Search> {
                    SearchScreen(
                        onMovieSelected = { movieId ->
                            navController.navigate(Route.MovieDetail(movieId))
                        },
                    )
                }

                composable<Route.MovieDetail> { backStackEntry ->
                    val route = backStackEntry.toRoute<Route.MovieDetail>()

                    // TODO
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "Movie: ${route.movieId}")
                    }
                }
            }
        }
    }
}
