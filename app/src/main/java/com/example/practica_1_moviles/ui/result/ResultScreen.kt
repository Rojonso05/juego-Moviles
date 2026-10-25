package com.example.practica_1_moviles.ui.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica_1_moviles.data.Difficulty
import com.example.practica_1_moviles.data.SettingsRepository

@Composable
fun ResultScreen(
    victory: Boolean,
    score: Int,
    category: String,
    difficulty: String,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit,
    onRanking: () -> Unit,
    viewModel: ResultViewModel = viewModel()
) {
    var name by rememberSaveable { mutableStateOf("") }
    // Marca que ya hemos rellenado el nombre por defecto, para hacerlo una sola vez
    var prefilled by rememberSaveable { mutableStateOf(false) }

    val defaultName = viewModel.defaultName
    LaunchedEffect(defaultName) {
        if (defaultName != null && !prefilled) {
            name = defaultName
            prefilled = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
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
        Text("Temática: $category · ${Difficulty.fromName(difficulty).label}")
        Text("Puntuación: $score", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = {
                if (it.length <= SettingsRepository.MAX_NAME_LENGTH) name = it
            },
            label = { Text("Tu nombre") },
            singleLine = true,
            enabled = !viewModel.saved,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = LocalContentColor.current,
                unfocusedTextColor = LocalContentColor.current,
                disabledTextColor = LocalContentColor.current.copy(alpha = 0.6f),
                focusedLabelColor = LocalContentColor.current,
                unfocusedLabelColor = LocalContentColor.current.copy(alpha = 0.7f),
                cursorColor = LocalContentColor.current
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.saveScore(name, score, category, victory, difficulty) },
            enabled = name.isNotBlank() && !viewModel.saved,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (viewModel.saved) "Puntuación guardada" else "Guardar puntuación")
        }

        Button(onClick = onPlayAgain, modifier = Modifier.fillMaxWidth()) {
            Text("Jugar de nuevo")
        }
        OutlinedButton(onClick = onRanking, modifier = Modifier.fillMaxWidth()) {
            Text("Ver ranking")
        }
        OutlinedButton(onClick = onHome, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al inicio")
        }
    }
}