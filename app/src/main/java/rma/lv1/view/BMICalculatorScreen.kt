package rma.lv1.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import rma.lv1.viewmodel.BMIViewModel
@Composable
fun BMICalculatorScreen(navController: NavController, viewModel: BMIViewModel) {
    var newTezina by remember { mutableStateOf(0f) }
    var newVisina by remember { mutableStateOf(0f) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = "Pozdrav Antonio!",
            fontSize = 20.sp,
            lineHeight = 56.sp,
            modifier = Modifier
                .padding(top = 8.dp)
        )
        Text(
            text = "Tvoj BMI je:",
            fontSize = 55.sp,
            lineHeight = 61.sp,
            textAlign = TextAlign.Center,


            )
        Button(onClick = { viewModel.calculateBMI()
        }) {
            Text("Izračunaj BMI")
        }

        Text(
            text = String.format("%.2f", viewModel.bmi),
            fontSize = 70.sp,
            lineHeight = 72.sp,
            fontWeight = FontWeight.Bold,
        )


        TextField(
            value = newTezina.toString(),
            onValueChange = { newTezina = it.toFloatOrNull() ?: 0f },
            label = { Text("Nova Tezina:") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Button(onClick = { viewModel.enterWeight(newTezina)
        }) {
            Text("Unesi težinu")
        }

        TextField(
            value = newVisina.toString(),
            onValueChange = { newVisina = it.toFloatOrNull() ?: 0f },
            label = { Text("Nova Visina:") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Button(onClick = { viewModel.enterHeight(newVisina)
        }) {
            Text("Unesi visinu")
        }
        Button(
            onClick = {
                // Navigate to OtherScreen when button clicked
                navController.navigate("step_counter")
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
        ) {
            Text(text = "Step Counter")
        }
    }
}
