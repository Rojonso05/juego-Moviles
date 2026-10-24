package com.example.practica_1_moviles.ui.ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica_1_moviles.data.ScoreEntity

@Composable
fun RankingScreen(
    onBack: () -> Unit,
    viewModel: RankingViewModel = viewModel()
) {
    val scores by viewModel.scores.collectAsState()

    // Qué diálogo de confirmación está abierto (si hay alguno)
    var scoreToDelete by remember { mutableStateOf<ScoreEntity?>(null) }
    var confirmDeleteAll by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ranking", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        if (scores.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no hay puntuaciones")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(scores, key = { _, score -> score.id }) { index, score ->
                    ScoreRow(
                        position = index + 1,
                        score = score,
                        onDelete = { scoreToDelete = score }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (scores.isNotEmpty()) {
            OutlinedButton(
                onClick = { confirmDeleteAll = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Borrar todo")
            }
        }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }

    // Confirmación para borrar una puntuación
    scoreToDelete?.let { score ->
        AlertDialog(
            onDismissRequest = { scoreToDelete = null },
            title = { Text("Borrar puntuación") },
            text = { Text("¿Borrar la puntuación de ${score.playerName} (${score.points} puntos)?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(score)
                    scoreToDelete = null
                }) { Text("Borrar") }
            },
            dismissButton = {
                TextButton(onClick = { scoreToDelete = null }) { Text("Cancelar") }
            }
        )
    }

    // Confirmación para vaciar el ranking
    if (confirmDeleteAll) {
        AlertDialog(
            onDismissRequest = { confirmDeleteAll = false },
            title = { Text("Borrar todo el ranking") },
            text = { Text("Se eliminarán todas las puntuaciones. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAll()
                    confirmDeleteAll = false
                }) { Text("Borrar todo") }
            },
            dismissButton = {
                TextButton(onClick = { confirmDeleteAll = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun ScoreRow(
    position: Int,
    score: ScoreEntity,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$position.", style = MaterialTheme.typography.titleMedium)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(score.playerName, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${score.category} · ${if (score.victory) "Victoria" else "Derrota"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text("${score.points}", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar puntuación")
            }
        }
    }
}