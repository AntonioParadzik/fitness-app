package rma.lv1.view

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import rma.lv1.viewmodel.BMIViewModel
import rma.lv1.viewmodel.StepSensorEventListener

@Composable
fun StepCounter(navController: NavController, viewModel: BMIViewModel) {

    val sensorManager = (LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val sensorEventListener = remember { StepSensorEventListener() }


    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(modifier = Modifier.fillMaxSize())
        Column {
            Text(
                text = "Step Count",
                fontSize = 20.sp
            )
            Text(text = "Step Count: ${viewModel.stepCount}", fontSize = 24.sp)
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
        viewModel.getSnapshot()

    /*    val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("steps").document("AA80j9faqlbibctRgfJN")
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
        }*/
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }

    }
}