package com.example.practica_1_moviles.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import com.example.practica_1_moviles.R

// La pantalla de inicio es una ilustración (1880 x 4072) con el logo y los tres
// textos «JUGAR», «RANKING» y «AJUSTES» ya dibujados. Sobre cada texto se coloca
// una zona transparente pulsable, definida como fracciones del dibujo.
private const val HOME_ASPECT = 1880f / 4072f

private const val BTN_X = 0.22f
private const val BTN_W = 0.56f
private const val BTN_H = 0.065f
private const val PLAY_Y = 0.5855f - 0.0169f      // centrada sobre cada texto
private const val RANKING_Y = 0.6739f - 0.0169f
private const val SETTINGS_Y = 0.7682f - 0.0169f

private fun Modifier.region(
    parentW: Dp, parentH: Dp,
    x: Float, y: Float, w: Float, h: Float
): Modifier = this
    .offset(x = parentW * x, y = parentH * y)
    .size(width = parentW * w, height = parentH * h)

@Composable
fun HomeScreen(
    onPlay: () -> Unit,
    onRanking: () -> Unit,
    onSettings: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints(modifier = Modifier.aspectRatio(HOME_ASPECT)) {
            val w = maxWidth
            val h = maxHeight

            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.pantalla_inicio),
                contentDescription = "Gueast",
                contentScale = ContentScale.FillBounds,
                filterQuality = FilterQuality.None,
                modifier = Modifier.fillMaxSize()
            )

            HomeButton(w, h, PLAY_Y, "Jugar", onPlay)
            HomeButton(w, h, RANKING_Y, "Ranking", onRanking)
            HomeButton(w, h, SETTINGS_Y, "Ajustes", onSettings)
        }
    }
}

@Composable
private fun HomeButton(w: Dp, h: Dp, y: Float, label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .region(w, h, BTN_X, y, BTN_W, BTN_H)
            .clickable(onClickLabel = label, onClick = onClick)
    )
}
