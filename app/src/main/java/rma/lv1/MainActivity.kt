package rma.lv1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import rma.lv1.ui.theme.LV1Theme
import java.security.AllPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.firestore.DocumentReference


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LV1Theme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "main_screen") {
                        composable("main_screen") {
                            MainScreen(navController = navController)
                        }
                        composable("step_counter") {
                            StepCounter(navController = navController)
                        }
                    }
                }
            }
        }

    }

    @Preview(showBackground = false)
    @Composable
    fun UserPreview() {
        var formattedBmi = ""
        val db = FirebaseFirestore.getInstance()
        var newTezina by remember { mutableStateOf(0f) }
        var newVisina by remember { mutableStateOf(0f) }
        var calculatedBmi by remember { mutableStateOf("") }

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
            Button(onClick = {
                val docRef = db.collection("BMI").document("Bw6FjWdcPMuJbqRAsCgO")

                docRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val mojaTezina = documentSnapshot.getDouble("tezina")
                        val mojaVisina = documentSnapshot.getDouble("visina")

                        if (mojaTezina != null && mojaVisina != null) {
                            val bmi = (mojaTezina / (mojaVisina * mojaVisina)) * 10000
                            val formattedBmi = String.format("%.2f", bmi)

                            calculatedBmi = formattedBmi
                            Log.d("MainActivity", "BMI: $formattedBmi")
                        } else {
                            Log.d("MainActivity", "Weight or height is null or height is zero")
                        }
                    } else {
                        Log.d("MainActivity", "Document does not exist")
                    }
                }
                    .addOnFailureListener { e ->
                        Log.e("MainActivity", "Error getting document: $e")
                    }
            }) {
                Text("Izračunaj BMI")
            }

            Text(
                text = calculatedBmi,
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

            Button(onClick = {
                val docRef = db.collection("BMI").document("Bw6FjWdcPMuJbqRAsCgO")
                docRef.update("tezina", newTezina)
                    .addOnSuccessListener {
                        // Update successful (optional: show a success message)
                    }
                    .addOnFailureListener { e ->
                        // Update failed (handle error, e.g., show an error message)
                        Log.e("MainActivity", "Error updating tezina: $e")
                    }
            }) {
                Text("Unesi težinu")
            }

            TextField(
                value = newVisina.toString(),
                onValueChange = { newVisina = it.toFloatOrNull() ?: 0f },
                label = { Text("Nova Visina:") },
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Button(onClick = {
                val docRef = db.collection("BMI").document("Bw6FjWdcPMuJbqRAsCgO")
                docRef.update("visina", newVisina)
                    .addOnSuccessListener {
                        // Update successful (optional: show a success message)
                    }
                    .addOnFailureListener { e ->
                        // Update failed (handle error, e.g., show an error message)
                        Log.e("MainActivity", "Error updating visina: $e")
                    }
            }) {
                Text("Unesi visinu")
            }
        }
    }

    @Composable
    fun BackgroundImage(modifier: Modifier) {
        Box(modifier) {
            Image(
                painter = painterResource(id = R.drawable.fitness),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.1F
            )
        }
    }

    @Composable
    fun MainScreen(navController: NavController) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment =
            Alignment.Center
        ) {
            BackgroundImage(modifier = Modifier.fillMaxSize())
            UserPreview()
            // Button to navigate to StepCounter
            Button(
                onClick = {
                    // Navigate to OtherScreen when button clicked
                    navController.navigate("step_counter")
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text(text = "Step Counter")
            }
        }
    }

    @Composable
    fun StepCounter(navController: NavController) {
        val db = Firebase.firestore
        val docRef = db.collection("steps").document("AA80j9faqlbibctRgfJN")

        val sensorManager = (LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensorEventListener = remember { StepSensorEventListener() }

        var stepCount by remember { mutableStateOf(0) }

        Box(modifier = Modifier.fillMaxSize()) {
            BackgroundImage(modifier = Modifier.fillMaxSize())
            Column {
                Text(
                    text = "Step Count",
                    fontSize = 20.sp
                )
                Text(text = "Step Count: $stepCount", fontSize = 24.sp)
            }
            // Back button
            Button(
                onClick = { navController.popBackStack()
                            },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text("User Info")
            }
        }

        DisposableEffect(Unit) {
            sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
            docRef.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("MainActivity", "Error listening to step count: ", exception)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    stepCount = snapshot.getDouble("step_count")?.toInt() ?: 0
                } else {
                    Log.d("MainActivity", "No such document")
                }
            }
            onDispose {
                sensorManager.unregisterListener(sensorEventListener)
            }

        }
    }

    class StepSensorEventListener : SensorEventListener {
        private val db = Firebase.firestore
        private var stepCount = 0
        private val docRef = db.collection("steps").document("AA80j9faqlbibctRgfJN")
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                val accelerationX = event.values[0]
                val accelerationY = event.values[1]
                val accelerationZ = event.values[2]

                val threshold = kotlin.math.sqrt(
                    accelerationX * accelerationX +
                            accelerationY * accelerationY +
                            accelerationZ * accelerationZ
                )

                val STEP_THRESHOLD = 13.0f

                val docRef = db.collection("steps").document("AA80j9faqlbibctRgfJN")
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            stepCount = document.getDouble("step_count")?.toInt() ?: 0
                        } else {
                            Log.d("MainActivity", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("MainActivity", "Error getting documents: ", exception)
                    }

                if (threshold > STEP_THRESHOLD) {
                    stepCount++
                    updateStepCountInFirestore(stepCount)
                }
            }
        }
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

        }

        private fun updateStepCountInFirestore(stepCount: Int) {
            docRef.update("step_count", stepCount)
                .addOnSuccessListener {
                    Log.d("MainActivtiy", "Success updating steps")
                }
                .addOnFailureListener { e ->
                    Log.e("MainActivity", "Error updating steps: $e")
                }
        }
    }

    fun resetStepCount(docRef: DocumentReference) {
        docRef.update("step_count", 0)
            .addOnSuccessListener {
                Log.d("MainActivtiy", "Success updating steps")
            }
            .addOnFailureListener { e ->
                Log.e("MainActivity", "Error updating steps: $e")
            }
    }
}