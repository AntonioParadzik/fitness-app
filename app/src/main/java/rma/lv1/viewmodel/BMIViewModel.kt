package rma.lv1.viewmodel

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import rma.lv1.model.BMIModel
class BMIViewModel : ViewModel() {
    val bmiModel = BMIModel(weight = null, height = null, bmiResult = null)
    val db = FirebaseFirestore.getInstance()

    var bmi by mutableStateOf(0f)
    var stepCount by mutableStateOf(0)

    fun calculateBMI() {
        val docRef = db.collection("BMI").document("Bw6FjWdcPMuJbqRAsCgO")

        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val mojaTezina = documentSnapshot.getDouble("tezina")
                if (mojaTezina != null) {
                    bmiModel.weight=mojaTezina.toFloat()
                }

                val mojaVisina = documentSnapshot.getDouble("visina")
                if (mojaVisina != null) {
                    bmiModel.height=mojaVisina.toFloat()
                }

                if (mojaTezina != null && mojaVisina != null) {
                    bmi = ((mojaTezina / (mojaVisina * mojaVisina)) * 10000).toFloat()
                    bmiModel.bmiResult=bmi
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
    }

    fun enterWeight(weight : Float) {
        val docRef = db.collection("BMI").document("Bw6FjWdcPMuJbqRAsCgO")
        docRef.update("tezina", weight)
            .addOnSuccessListener {
                // Update successful (optional: show a success message)
            }
            .addOnFailureListener { e ->
                // Update failed (handle error, e.g., show an error message)
                Log.e("MainActivity", "Error updating tezina: $e")
            }
    }

    fun enterHeight(height: Float?) {
        val docRef = db.collection("BMI").document("Bw6FjWdcPMuJbqRAsCgO")
        docRef.update("visina", height)
            .addOnSuccessListener {
                // Update successful (optional: show a success message)
            }
            .addOnFailureListener { e ->
                // Update failed (handle error, e.g., show an error message)
                Log.e("MainActivity", "Error updating visina: $e")
            }

    }

    fun getSnapshot() {
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
        }

    }

    fun signIn(context: Context, email: String, password: String, navController: NavController) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Prijavljeno uspješno
                    Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate("main_screen")
                } else {
                    // Prijavljivanje neuspješno
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun register(context: Context, email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,
            password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registracija uspješna
                    Toast.makeText(context, "Registered successfully",
                        Toast.LENGTH_SHORT).show()
                } else {
                    // Registracija neuspješna
                    Toast.makeText(context, "Registration failed",
                        Toast.LENGTH_SHORT).show()
                }
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


