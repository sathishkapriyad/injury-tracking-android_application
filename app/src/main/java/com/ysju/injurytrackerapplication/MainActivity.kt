package com.ysju.injurytrackerapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ysju.injurytrackerapplication.ui.theme.InjuryTrackerApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InjuryTrackerApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation() // This is the @Composable function where NavHost is defined
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController() // This is correct in a @Composable function
    NavHost(navController = navController, startDestination = "injuryTracking") { // This setup is also correct
        composable("injuryTracking") { InjuryTrackingScreen(navController) } // Ensure each composable screen accepts NavController
        composable("symptomMonitoring") { SymptomMonitoringScreen(navController) }
        composable("concussionAssessment") { ConcussionAssessmentScreen(navController) }
    }
}

@Composable
fun InjuryTrackingScreen(navController: NavController) {
    Button(onClick = {
        Log.d("Navigation", "Navigating to Symptom Monitoring")
        navController.navigate("symptomMonitoring")
    }) {
        Text("Go to Symptom Monitoring")
    }
}

@Composable
fun SymptomMonitoringScreen(navController: NavController) {
    Button(onClick = { navController.navigate("injuryTracking") }) {
        Text("Back to Injury Tracking")
    }
}

@Composable
fun ConcussionAssessmentScreen(navController: NavController) {
    // Your composable content here
}


// Optional: Preview of your Composable
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InjuryTrackerApplicationTheme {
        Greeting("Android")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}


