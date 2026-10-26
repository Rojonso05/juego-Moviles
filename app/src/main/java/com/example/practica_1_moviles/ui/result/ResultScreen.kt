package com.example.practica_1_moviles.ui.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica_1_moviles.R
import com.example.practica_1_moviles.data.Difficulty
import com.example.practica_1_moviles.data.SettingsRepository

// ---------------------------------------------------------------
//  PANTALLA DE VICTORIA
//  Ilustración de 1880 x 4072 (oscurecida, semitransparente) con el título, el
//  subtítulo y los cuatro botones ya dibujados. Se coloca sobre el último escenario
//  y encima se ponen las zonas pulsables y los datos (puntos, temática y nombre),
//  como fracciones del dibujo.
// ---------------------------------------------------------------
private const val VICTORY_ASPECT = 1880f / 4072f

private const val SCORE_Y = 0.432f
private const val SCORE_H = 0.028f
private const val INFO_Y = 0.460f
private const val INFO_H = 0.020f
private const val NAME_X = 0.2766f
private const val NAME_W = 0.4676f
private const val NAME_Y = 0.484f
private const val NAME_H = 0.030f

private const val SAVE_Y = 0.5265f
private const val SAVE_H = 0.0312f
private const val BTN_X = 0.2426f
private const val BTN_W = 0.5527f
private const val BTN_H = 0.0724f
private const val PLAY_Y = 0.5972f
private const val RANKING_Y = 0.6935f
private const val HOME_Y = 0.7898f

private val PixelGreen = Color(0xFF4CAF50)
private val SaveBg = Color(0xFF3B1B1B)

private fun Modifier.region(
    parentW: Dp, parentH: Dp,
    x: Float, y: Float, w: Float, h: Float
): Modifier = this
    .offset(x = parentW * x, y = parentH * y)
    .size(width = parentW * w, height = parentH * h)

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
    // Marca que ya se ha rellenado el nombre por defecto, para hacerlo una sola vez
    var prefilled by rememberSaveable { mutableStateOf(false) }

    val defaultName = viewModel.defaultName
    LaunchedEffect(defaultName) {
        if (defaultName != null && !prefilled) {
            name = defaultName
            prefilled = true
        }
    }

    val onNameChange: (String) -> Unit = {
        if (it.length <= SettingsRepository.MAX_NAME_LENGTH) name = it
    }
    val onSave = { viewModel.saveScore(name, score, category, victory, difficulty) }
    val difficultyLabel = Difficulty.fromName(difficulty).label

    if (victory) {
        VictoryContent(
            score = score,
            info = "$category · $difficultyLabel",
            name = name,
            onNameChange = onNameChange,
            saved = viewModel.saved,
            onSave = onSave,
            onPlayAgain = onPlayAgain,
            onRanking = onRanking,
            onHome = onHome
        )
        return
    }

    // ---------- DERROTA: diseño normal ----------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Derrota",
            style = MaterialTheme.typography.headlineLarge
        )
        Text("Los monstruos han podido contigo")
        Text("Temática: $category · $difficultyLabel")
        Text("Puntuación: $score", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
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
            onClick = onSave,
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

@Composable
private fun VictoryContent(
    score: Int,
    info: String,
    name: String,
    onNameChange: (String) -> Unit,
    saved: Boolean,
    onSave: () -> Unit,
    onPlayAgain: () -> Unit,
    onRanking: () -> Unit,
    onHome: () -> Unit
) {
    val canSave = name.isNotBlank() && !saved

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints(modifier = Modifier.aspectRatio(VICTORY_ASPECT)) {
            val w = maxWidth
            val h = maxHeight

            // Fondo: el último escenario, y encima la ilustración de victoria
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.escenario_3),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                filterQuality = FilterQuality.None,
                modifier = Modifier.fillMaxSize()
            )
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.pantalla_victoria),
                contentDescription = "Victoria",
                contentScale = ContentScale.FillBounds,
                filterQuality = FilterQuality.None,
                modifier = Modifier.fillMaxSize()
            )

            // Puntos y temática
            Box(
                modifier = Modifier.region(w, h, 0.1915f, SCORE_Y, 0.6165f, SCORE_H),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Puntuación: $score",
                    color = PixelGreen,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )
            }
            Box(
                modifier = Modifier.region(w, h, 0.1f, INFO_Y, 0.8f, INFO_H),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    info,
                    color = Color(0xFFA5C9A8),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Campo de nombre
            Box(
                modifier = Modifier
                    .region(w, h, NAME_X, NAME_Y, NAME_W, NAME_H)
                    .background(Color(0x99000000))
                    .border(1.dp, PixelGreen)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = name,
                    onValueChange = onNameChange,
                    enabled = !saved,
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { inner ->
                        if (name.isEmpty()) {
                            Text(
                                "Tu nombre",
                                color = Color(0x99FFFFFF),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        inner()
                    }
                )
            }

            // Guardar puntuación: la zona pulsable va sobre el botón dibujado.
            // Sin nombre se oscurece; una vez guardada se tapa con «Puntuación guardada».
            Box(
                modifier = Modifier
                    .region(w, h, NAME_X, SAVE_Y, NAME_W, SAVE_H)
                    .clickable(enabled = canSave, onClick = onSave),
                contentAlignment = Alignment.Center
            ) {
                if (saved) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(SaveBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "PUNTUACIÓN GUARDADA",
                            color = PixelGreen,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }
                } else if (!canSave) {
                    Box(modifier = Modifier.fillMaxSize().background(Color(0x99000000)))
                }
            }

            // Botones dibujados: zonas pulsables transparentes
            Box(
                modifier = Modifier
                    .region(w, h, BTN_X, PLAY_Y, BTN_W, BTN_H)
                    .clickable(onClickLabel = "Volver a jugar", onClick = onPlayAgain)
            )
            Box(
                modifier = Modifier
                    .region(w, h, BTN_X, RANKING_Y, BTN_W, BTN_H)
                    .clickable(onClickLabel = "Ver ranking", onClick = onRanking)
            )
            Box(
                modifier = Modifier
                    .region(w, h, BTN_X, HOME_Y, BTN_W, BTN_H)
                    .clickable(onClickLabel = "Ir al inicio", onClick = onHome)
            )
        }
    }
}
