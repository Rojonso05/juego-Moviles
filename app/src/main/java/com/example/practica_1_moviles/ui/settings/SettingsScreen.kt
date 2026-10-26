package com.example.practica_1_moviles.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica_1_moviles.R
import com.example.practica_1_moviles.data.Difficulty
import com.example.practica_1_moviles.data.SettingsEntity
import com.example.practica_1_moviles.data.SettingsRepository

// ---------------------------------------------------------------
//  PANTALLA DE AJUSTES
//  Ilustración de 1880 x 4072 con el título, las etiquetas, el marco del nombre,
//  las tres casillas de dificultad y la casilla de sonido ya dibujados. Encima se
//  colocan el campo de texto, los rellenos de «seleccionado» y las zonas pulsables,
//  como fracciones del dibujo.
// ---------------------------------------------------------------
private const val SETTINGS_ASPECT = 1880f / 4072f

// Marco del nombre
private const val NAME_X = 0.1532f
private const val NAME_Y = 0.2809f
private const val NAME_W = 0.7016f
private const val NAME_H = 0.0685f

// Casillas (cuadrado dibujado) y fila pulsable de cada opción
private const val BOX_X = 0.1528f
private const val BOX_W = 0.0715f
private const val BOX_H = 0.0290f
private const val ROW_X = 0.1400f
private const val ROW_W = 0.5000f
private const val ROW_H = 0.0480f

private val EASY_Y = 0.4242f
private val NORMAL_Y = 0.4887f
private val HARD_Y = 0.5538f
private val SOUND_Y = 0.6683f
private const val SOUND_BOX_W = 0.1105f

private val PixelGreen = Color(0xFF4CAF50)

private fun Modifier.region(
    parentW: Dp, parentH: Dp,
    x: Float, y: Float, w: Float, h: Float
): Modifier = this
    .offset(x = parentW * x, y = parentH * y)
    .size(width = parentW * w, height = parentH * h)

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val current = settings

    // Hasta que Room entregue los ajustes guardados, muestra una rueda de carga
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
        onSoundChange = viewModel::setSoundEnabled
    )
}

@Composable
private fun SettingsContent(
    settings: SettingsEntity,
    onNameChange: (String) -> Unit,
    onDifficultyChange: (Difficulty) -> Unit,
    onSoundChange: (Boolean) -> Unit
) {
    // El nombre se edita en un estado local y se guarda en Room a la vez.
    // Solo se inicializa con el valor guardado.
    var name by rememberSaveable { mutableStateOf(settings.playerName) }
    val difficulty = Difficulty.fromName(settings.difficulty)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints(modifier = Modifier.aspectRatio(SETTINGS_ASPECT)) {
            val w = maxWidth
            val h = maxHeight

            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.pantalla_ajustes),
                contentDescription = "Ajustes",
                contentScale = ContentScale.FillBounds,
                filterQuality = FilterQuality.None,
                modifier = Modifier.fillMaxSize()
            )

            // ---------- Nombre por defecto ----------
            Box(
                modifier = Modifier
                    .region(w, h, NAME_X, NAME_Y, NAME_W, NAME_H)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = name,
                    onValueChange = {
                        if (it.length <= SettingsRepository.MAX_NAME_LENGTH) {
                            name = it
                            onNameChange(it)
                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 18.sp
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ---------- Dificultad ----------
            DifficultyOption(w, h, EASY_Y, Difficulty.EASY, difficulty, onDifficultyChange)
            DifficultyOption(w, h, NORMAL_Y, Difficulty.NORMAL, difficulty, onDifficultyChange)
            DifficultyOption(w, h, HARD_Y, Difficulty.HARD, difficulty, onDifficultyChange)

            // ---------- Sonido ----------
            Box(
                modifier = Modifier
                    .region(w, h, ROW_X, SOUND_Y - 0.0095f, ROW_W, ROW_H)
                    .clickable(role = Role.Switch) { onSoundChange(!settings.soundEnabled) }
            )
            if (settings.soundEnabled) {
                Box(
                    modifier = Modifier
                        .region(w, h, BOX_X, SOUND_Y, SOUND_BOX_W, BOX_H)
                        .padding(4.dp)
                        .background(PixelGreen)
                )
            }
        }
    }
}

// Casilla de una dificultad: zona pulsable y, si está elegida, relleno verde en la casilla
@Composable
private fun DifficultyOption(
    w: Dp, h: Dp, y: Float,
    option: Difficulty,
    selected: Difficulty,
    onSelect: (Difficulty) -> Unit
) {
    Box(
        modifier = Modifier
            .region(w, h, ROW_X, y - 0.0095f, ROW_W, ROW_H)
            .clickable(role = Role.RadioButton) { onSelect(option) }
    )
    if (option == selected) {
        Box(
            modifier = Modifier
                .region(w, h, BOX_X, y, BOX_W, BOX_H)
                .padding(4.dp)
                .background(PixelGreen)
        )
    }
}
