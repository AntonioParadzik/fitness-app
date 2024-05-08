package rma.lv1

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LV1Theme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background

                ) {
                    BackgroundImage(modifier = Modifier)
                }
            }
        }
    }
}



@Composable
fun UserPreview(name: String, visina: Float, tezina: Float, modifier: Modifier = Modifier) {



    var formattedBmi = ""
    val db = FirebaseFirestore.getInstance()
    var newTezina by remember { mutableStateOf(0f)}
    var newVisina by remember { mutableStateOf(0f)}
    var calculatedBmi by remember { mutableStateOf("") }

        Text(
        text = "Pozdrav $name!",
        fontSize = 20.sp,
        lineHeight = 56.sp,
        modifier= Modifier
            .padding(top= 8.dp)
            .padding(start =10.dp)

    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

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
                        val bmi = (mojaTezina / (mojaVisina * mojaVisina))*10000
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

    Box (modifier){ Image(
        painter = painterResource(id = R.drawable.fitness),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = 0.1F
    )
        UserPreview(name = "Antonio", visina = 172f, tezina = 70f, modifier = Modifier.fillMaxSize())
    }

}
@Preview(showBackground = false)
@Composable
fun UserPreview() {
    LV1Theme {
        BackgroundImage(modifier = Modifier)   }
}