package com.example.practica_1_moviles.ui.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultScreen(
    victory: Boolean,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (victory) "¡Victoria!" else "Derrota",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            if (victory) "Has derrotado a todos los monstruos"
            else "Los monstruos han podido contigo"
        )
        Button(onClick = onPlayAgain) { Text("Jugar de nuevo") }
        OutlinedButton(onClick = onHome) { Text("Volver al inicio") }
    }
}