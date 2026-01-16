package fr.delplanque.tp_androidstudio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: CartViewModel) {
    val history by viewModel.orderHistory.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Historique des commandes",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (history.isEmpty()) {
            Text("Aucune commande passée.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(history) { order ->
                    OrderRow(order)
                }
            }
        }
    }
}

@Composable
fun OrderRow(order: OrderEntity) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(order.date))

    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Commande n°${order.id}", style = MaterialTheme.typography.titleMedium)
                Text(text = dateString, style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                Text(text = "${String.format("%.2f", order.totalAmount)} €", style = MaterialTheme.typography.titleMedium)
                Text(text = "${order.itemCount} articles", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}