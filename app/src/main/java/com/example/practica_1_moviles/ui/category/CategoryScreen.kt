package com.example.practica_1_moviles.ui.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practica_1_moviles.data.QuestionRepository

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = viewModel(),
    onCategorySelected: (String) -> Unit
) {
    val categories by viewModel.categories.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text("Elige una temática", style = MaterialTheme.typography.headlineMedium)

        // Una lista por si algún día hay muchas temáticas
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                Button(
                    onClick = { onCategorySelected(category) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(category)
                }
            }
        }

        // Temática especial: mezcla todas
        OutlinedButton(
            onClick = { onCategorySelected(QuestionRepository.RANDOM) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(QuestionRepository.RANDOM)
        }
    }
}