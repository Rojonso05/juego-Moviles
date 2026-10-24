package com.example.practica_1_moviles.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica_1_moviles.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CircularProgressIndicator

private val CardDark = Color(0xFF2B2420)
private val Green = Color(0xFF4CAF50)
private val Red = Color(0xFFF44336)

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel(),
    onFinish: (victory: Boolean, score: Int) -> Unit = { _, _ -> }
) {
    LaunchedEffect(viewModel.status) {
        when (viewModel.status) {
            GameStatus.VICTORY -> onFinish(true, viewModel.finalScore)
            GameStatus.DEFEAT -> onFinish(false, viewModel.finalScore)
            else -> {}
        }
    }

    // Mientras se cargan las preguntas de la base de datos
    if (viewModel.status == GameStatus.LOADING) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val question = viewModel.currentQuestion
    val monster = viewModel.currentMonster
    val answered = viewModel.status != GameStatus.ANSWERING

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // ---------- FRANJA SUPERIOR: vida del jugador y puntos ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Tu vida: ${viewModel.playerHp}/${GameViewModel.PLAYER_MAX_HP}",
                    color = Color.White
                )
                Text(
                    "Puntos: ${viewModel.totalDamage}",
                    color = Color.White
                )
            }
            LinearProgressIndicator(
                progress = { viewModel.playerHp / GameViewModel.PLAYER_MAX_HP.toFloat() },
                color = Green,
                trackColor = Color.DarkGray,
                gapSize = 0.dp,
                drawStopIndicator = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
        }

        // ---------- ESCENA: fondo + vida del enemigo ----------
        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(R.drawable.fondo_mazmorra_1),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            // Capa que cubre la imagen y reparte el espacio por proporciones
            Column(modifier = Modifier.matchParentSize()) {
                Spacer(Modifier.weight(0.40f))   // sube/baja la barra

                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(Modifier.weight(0.50f))   // mueve la barra a izquierda/derecha
                    Column(modifier = Modifier.weight(0.20f)) {
                        Text(
                            "${monster.name}  ${viewModel.monsterHp}/${monster.maxHp}",
                            color = Color.White,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                        LinearProgressIndicator(
                            progress = { viewModel.monsterHp / monster.maxHp.toFloat() },
                            color = Red,
                            trackColor = Color.DarkGray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                        )
                    }
                    Spacer(Modifier.weight(0.02f))
                }

                Spacer(Modifier.weight(0.60f))
            }
        }

        // ---------- ZONA INFERIOR: tiempo, pregunta y respuestas ----------
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            LinearProgressIndicator(
                progress = { viewModel.timeLeftMs / question.timeLimitMs.toFloat() },
                trackColor = Color.DarkGray,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                question.text,
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                minLines = 2,
                maxLines = 2
            )

            question.answers.forEachIndexed { index, answer ->
                val color = when {
                    !answered -> CardDark
                    index == question.correctIndex -> Green
                    index == viewModel.selectedAnswer -> Red
                    else -> CardDark
                }
                Card(
                    colors = CardDefaults.cardColors(containerColor = color),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clickable { viewModel.onAnswerSelected(index) }
                ) {
                    Text(
                        answer,
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }

            // Fila de feedback: siempre ocupa el mismo alto, haya o no botón
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (viewModel.status == GameStatus.FEEDBACK) {
                    Text(
                        text = when {
                            viewModel.lastDamageDealt > 0 -> "¡${viewModel.lastDamageDealt} de daño!"
                            viewModel.lastDamageTaken > 0 -> "Recibes -${viewModel.lastDamageTaken} HP"
                            else -> ""
                        },
                        color = Color.White
                    )
                    Button(
                        onClick = { viewModel.nextTurn() },
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Siguiente", fontSize = 14.sp, maxLines = 1)
                    }
                }
            }
        }
    }
}