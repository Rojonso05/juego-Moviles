package com.example.practica_1_moviles.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica_1_moviles.data.Difficulty
import com.example.practica_1_moviles.data.SettingsEntity
import com.example.practica_1_moviles.data.SettingsRepository

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val current = settings

    // Hasta que Room entregue los ajustes guardados, mostramos una rueda de carga
    if (current == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    SettingsContent(
        settings = current,
        onNameChange = viewModel::setPlayerName,
        onDifficultyChange = viewModel::setDifficulty,
        onSoundChange = viewModel::setSoundEnabled,
        onBack = onBack
    )
}

@Composable
private fun SettingsContent(
    settings: SettingsEntity,
    onNameChange: (String) -> Unit,
    onDifficultyChange: (Difficulty) -> Unit,
    onSoundChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    // El nombre se edita en un estado local (para que el cursor no salte)
    // y se guarda en Room a la vez. Solo se inicializa con el valor guardado.
    var name by rememberSaveable { mutableStateOf(settings.playerName) }
    val difficulty = Difficulty.fromName(settings.difficulty)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Ajustes", style = MaterialTheme.typography.headlineMedium)

        // ---------- Nombre por defecto ----------
        Text("Nombre de jugador", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = name,
            onValueChange = {
                if (it.length <= SettingsRepository.MAX_NAME_LENGTH) {
                    name = it
                    onNameChange(it)
                }
            },
            label = { Text("Nombre por defecto") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // ---------- Dificultad ----------
        Text("Dificultad", style = MaterialTheme.typography.titleMedium)
        Column {
            Difficulty.entries.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = option == difficulty,
                            onClick = { onDifficultyChange(option) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = option == difficulty, onClick = null)
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(option.label)
                        Text(
                            "${option.timeLimitMs / 1000} s por pregunta · puntos ×${option.multiplier}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // ---------- Sonido ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Sonido", style = MaterialTheme.typography.titleMedium)
            Switch(
                checked = settings.soundEnabled,
                onCheckedChange = onSoundChange
            )
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}