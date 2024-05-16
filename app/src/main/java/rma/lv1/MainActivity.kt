package rma.lv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import rma.lv1.ui.theme.LV1Theme
import rma.lv1.view.BMICalculatorScreen
import rma.lv1.view.BackgroundImage
import rma.lv1.viewmodel.BMIViewModel
import rma.lv1.view.StepCounter

import android.content.Context

import android.widget.Toast

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import rma.lv1.view.LoginRegisterScreen


class MainActivity : ComponentActivity() {
    private val bmiViewModel: BMIViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val navController = rememberNavController()
            BackgroundImage(modifier = Modifier)

            LV1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = if (currentUser != null) "main_screen" else "login_screen") {
                        composable("main_screen") {
                            BMICalculatorScreen(navController = navController, bmiViewModel)
                        }
                        composable("step_counter") {
                            StepCounter(navController = navController, bmiViewModel)
                        }
                        composable("login_screen") {
                            LoginRegisterScreen(navController = navController, bmiViewModel)
                        }
                    }
                }
            }
        }
    }
}

