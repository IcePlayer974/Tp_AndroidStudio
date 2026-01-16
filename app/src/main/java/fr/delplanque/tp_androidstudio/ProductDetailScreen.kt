package fr.delplanque.tp_androidstudio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(product: Product, onBack: () -> Unit, onAddToCart: (Product) -> Unit, onGoToCart: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détail du produit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = product.title, style = MaterialTheme.typography.headlineMedium)

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${product.price} €",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(text = "Note: ${product.rating.rate}/5 (${product.rating.count} avis)")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Description", style = MaterialTheme.typography.titleLarge)
            Text(text = product.description, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                                onAddToCart(product)
                                onGoToCart()
                             }, modifier = Modifier.fillMaxWidth()) {
                Text("Ajouter et aller au panier")
            }
        }
    }
}