package com.albin.demo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.albin.demo.data.Comida
import com.albin.demo.data.RetrofitClient

// Representa los estados de la pantalla: cargando, éxito o error.
sealed interface ComidasUiState {
    data object Loading : ComidasUiState
    data class Success(val comidas: List<Comida>) : ComidasUiState
    data class Error(val message: String) : ComidasUiState
}

@Composable
fun HomeScreen() {
    var uiState by remember { mutableStateOf<ComidasUiState>(ComidasUiState.Loading) }
    var reloadKey by remember { mutableIntStateOf(0) }

    // LaunchedEffect ejecuta la carga una sola vez al entrar a la pantalla
    // y nuevamente si presionamos el botón "Reintentar".
    LaunchedEffect(reloadKey) {
        uiState = ComidasUiState.Loading
        uiState = try {
            // Retrofit hace la petición GET al endpoint /api-comidas/comidas.
            val comidas = RetrofitClient.comidasService.getComidas()
            ComidasUiState.Success(comidas)
        } catch (exception: Exception) {
            ComidasUiState.Error(
                message = exception.message ?: "No se pudo cargar la información"
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Listado de comidas peruanas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Esta pantalla consume MockAPI con Retrofit y muestra comida peruana.",
                style = MaterialTheme.typography.bodyLarge
            )

            when (val state = uiState) {
                ComidasUiState.Loading -> {
                    LoadingContent()
                }

                is ComidasUiState.Error -> {
                    ErrorContent(
                        message = state.message,
                        onRetry = { reloadKey++ }
                    )
                }

                is ComidasUiState.Success -> {
                    ComidasList(comidas = state.comidas)
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Cargando datos desde MockAPI...")
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Ocurrió un problema al consumir la API",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = message)
            Button(onClick = onRetry) {
                Text(text = "Reintentar")
            }
        }
    }
}

@Composable
private fun ComidasList(comidas: List<Comida>) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(comidas) { comida ->
            ComidaCard(comida = comida)
        }
    }
}

@Composable
private fun ComidaCard(comida: Comida) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = comida.nombre,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = comida.descripcion,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Precio: S/ ${comida.precio}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

