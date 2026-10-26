package com.example.practica_1_moviles.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica_1_moviles.R
import com.example.practica_1_moviles.data.Difficulty
import kotlinx.coroutines.delay

private val Green = Color(0xFF4CAF50)
private val Red = Color(0xFFF44336)

// ---------------------------------------------------------------
//  ESCENARIOS
//  Cada monstruo tiene su escenario (con el sprite del monstruo ya
//  dibujado) y su caja de respuesta, del mismo color. El orden de la
//  lista es el orden de los monstruos: Slime, Goblin, Dragón.
//  Para cambiar un dibujo basta con sustituir el PNG de res/drawable-nodpi.
// ---------------------------------------------------------------

// Rectángulo interior transparente de la caja de respuesta (0..1 sobre la caja).
private class Interior(val left: Float, val top: Float, val right: Float, val bottom: Float)

private class Stage(
    val background: Int,     // escenario completo: escena + panel de pregunta y respuestas
    val answerBox: Int,      // caja de cada respuesta
    val interior: Interior,  // hueco de la caja donde va el texto
    val hpX: Float,          // posición de la barra de vida del monstruo (0..1 sobre el escenario)
    val hpY: Float,
    val hpW: Float
)

private val STAGES = listOf(
    Stage(R.drawable.escenario_1, R.drawable.caja_respuesta_1,
        Interior(0.0444f, 0.2069f, 0.9778f, 0.7241f), hpX = 0.58f, hpY = 0.190f, hpW = 0.34f),
    Stage(R.drawable.escenario_2, R.drawable.caja_respuesta_2,
        Interior(0.0444f, 0.2759f, 0.9778f, 0.7931f), hpX = 0.60f, hpY = 0.225f, hpW = 0.32f),
    Stage(R.drawable.escenario_3, R.drawable.caja_respuesta_3,
        Interior(0.0444f, 0.2759f, 0.9778f, 0.7931f), hpX = 0.56f, hpY = 0.190f, hpW = 0.34f)
)

// Proporciones del dibujo: los escenarios miden 1880 x 4072 y las cajas 1440 x 232.
private const val STAGE_ASPECT = 1880f / 4072f
private const val BAR_ASPECT = 1440f / 232f

// Zonas del escenario (fracciones de ancho y alto del dibujo)
private const val PANEL_X = 0.0723f
private const val PANEL_W = 0.8554f
private const val TOP_H = 0.039f          // franja negra superior: vida del jugador y puntos
private const val TIMER_Y = 0.4283f       // franja negra entre la escena y el panel: cronómetro
private const val TIMER_H = 0.039f
private const val QUESTION_Y = 0.4833f    // cabecera del panel: enunciado
private const val QUESTION_H = 0.0904f
private const val ANSWERS_Y = 0.5894f     // zona negra del panel: las 4 respuestas
private const val ANSWERS_H = 0.275f
private const val FEEDBACK_Y = 0.868f     // zona marrón inferior: daño y botón «Siguiente»
private const val FEEDBACK_H = 0.052f

// Tiempo que se ve la pantalla de «Game Over» antes de pasar al resultado
private const val DEFEAT_DELAY_MS = 2200L

// Coloca un elemento en una zona del dibujo dada como fracciones (x, y, ancho, alto)
private fun Modifier.region(
    parentW: Dp, parentH: Dp,
    x: Float, y: Float, w: Float, h: Float
): Modifier = this
    .offset(x = parentW * x, y = parentH * y)
    .size(width = parentW * w, height = parentH * h)

// Barra de progreso rectangular: relleno de `color` sobre fondo gris, sin
// huecos, esquinas redondeadas ni punto final (a diferencia del
// LinearProgressIndicator de Material 3).
@Composable
private fun GameBar(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(Color.DarkGray)) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .background(color)
        )
    }
}

// Caja de respuesta: el hueco se rellena de verde/rojo al responder
@Composable
private fun AnswerBar(
    text: String,
    boxRes: Int,
    interior: Interior,
    fill: Color?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(BAR_ASPECT)
            .clickable(onClick = onClick)
    ) {
        val w = maxWidth
        val h = maxHeight
        val iw = interior.right - interior.left
        val ih = interior.bottom - interior.top

        if (fill != null) {
            Box(
                modifier = Modifier
                    .region(w, h, interior.left, interior.top, iw, ih)
                    .background(fill)
            )
        }
        Image(
            bitmap = ImageBitmap.imageResource(boxRes),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            filterQuality = FilterQuality.None,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .region(w, h, interior.left, interior.top, iw, ih)
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel(),
    onFinish: (victory: Boolean, score: Int, difficulty: Difficulty) -> Unit = { _, _, _ -> }
) {
    LaunchedEffect(viewModel.status) {
        when (viewModel.status) {
            GameStatus.VICTORY -> onFinish(true, viewModel.finalScore, viewModel.difficulty)
            GameStatus.DEFEAT -> {
                delay(DEFEAT_DELAY_MS)   // se deja ver el «Game Over»
                onFinish(false, viewModel.finalScore, viewModel.difficulty)
            }
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
    // Cada monstruo tiene su propio escenario, sprite y caja de respuesta
    val stage = STAGES[viewModel.monsterIndex % STAGES.size]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.TopCenter
    ) {
        // El dibujo mantiene su proporción; todo lo demás se coloca sobre él
        BoxWithConstraints(modifier = Modifier.aspectRatio(STAGE_ASPECT)) {
            val w = maxWidth
            val h = maxHeight

            // ---------- FONDO: escena + panel ----------
            Image(
                bitmap = ImageBitmap.imageResource(stage.background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                filterQuality = FilterQuality.None,
                modifier = Modifier.fillMaxSize()
            )

            // ---------- FRANJA SUPERIOR: vida del jugador y puntos ----------
            Column(
                modifier = Modifier.region(w, h, PANEL_X, 0f, PANEL_W, TOP_H),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Tu vida: ${viewModel.playerHp}/${GameViewModel.PLAYER_MAX_HP}",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    Text(
                        "Puntos: ${viewModel.liveScore}",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
                Spacer(Modifier.height(2.dp))
                GameBar(
                    progress = viewModel.playerHp / GameViewModel.PLAYER_MAX_HP.toFloat(),
                    color = Green,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
            }

            // ---------- ESCENA: vida del monstruo ----------
            Column(
                modifier = Modifier.region(w, h, stage.hpX, stage.hpY, stage.hpW, 0.03f)
            ) {
                Text(
                    "${monster.name}  ${viewModel.monsterHp}/${monster.maxHp}",
                    color = Color.White,
                    fontSize = 11.sp,
                    maxLines = 1
                )
                GameBar(
                    progress = viewModel.monsterHp / monster.maxHp.toFloat(),
                    color = Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
            }

            // ---------- CRONÓMETRO (entre la escena y el panel) ----------
            Box(
                modifier = Modifier.region(w, h, PANEL_X, TIMER_Y, PANEL_W, TIMER_H),
                contentAlignment = Alignment.Center
            ) {
                GameBar(
                    progress = viewModel.timeLeftMs / viewModel.timeLimitMs.toFloat(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
            }

            // ---------- PREGUNTA (cabecera del panel) ----------
            Box(
                modifier = Modifier
                    .region(w, h, PANEL_X, QUESTION_Y, PANEL_W, QUESTION_H)
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    question.text,
                    color = Color.White,
                    fontSize = 15.sp,
                    lineHeight = 19.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // ---------- RESPUESTAS (zona negra del panel) ----------
            Column(
                modifier = Modifier.region(w, h, PANEL_X, ANSWERS_Y, PANEL_W, ANSWERS_H),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                question.answers.forEachIndexed { index, answer ->
                    val fill = when {
                        !answered -> null
                        index == question.correctIndex -> Green
                        index == viewModel.selectedAnswer -> Red
                        else -> null
                    }
                    AnswerBar(
                        text = answer,
                        boxRes = stage.answerBox,
                        interior = stage.interior,
                        fill = fill,
                        onClick = { viewModel.onAnswerSelected(index) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // ---------- FEEDBACK: siempre ocupa la misma zona, haya o no botón ----------
            Row(
                modifier = Modifier.region(w, h, PANEL_X, FEEDBACK_Y, PANEL_W, FEEDBACK_H),
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
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Button(
                        onClick = { viewModel.nextTurn() },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Siguiente", fontSize = 13.sp, maxLines = 1)
                    }
                }
            }

            // ---------- GAME OVER: oscurece la pantalla antes del resultado ----------
            if (viewModel.status == GameStatus.DEFEAT) {
                Image(
                    bitmap = ImageBitmap.imageResource(R.drawable.game_over),
                    contentDescription = "Game over",
                    contentScale = ContentScale.FillBounds,
                    filterQuality = FilterQuality.None,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
