package com.example.practica_1_moviles.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.practica_1_moviles.R

// Flecha de «volver atrás» (pixel art) para la esquina superior izquierda.
@Composable
fun BackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Image(
        bitmap = ImageBitmap.imageResource(R.drawable.boton_atras),
        contentDescription = "Volver",
        contentScale = ContentScale.FillBounds,
        filterQuality = FilterQuality.None,
        modifier = modifier
            .width(40.dp)
            .aspectRatio(144f / 176f)
            .clickable(role = Role.Button, onClick = onClick)
    )
}

// Pone el botón de volver sobre cualquier pantalla. El contenido se baja
// `topPadding` para que el botón no tape títulos ni controles.
@Composable
fun ScreenWithBack(
    onBack: () -> Unit,
    topPadding: Dp = 56.dp,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().padding(top = topPadding)) { content() }
        BackButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 8.dp)
        )
    }
}
