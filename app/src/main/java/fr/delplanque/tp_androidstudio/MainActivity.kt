package fr.delplanque.tp_androidstudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.ShoppingCart
import fr.delplanque.tp_androidstudio.ui.theme.Tp_AndroidStudioTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tp_AndroidStudioTheme {
                val navController = rememberNavController()
                val productViewModel: ProductViewModel = viewModel()
                val cartViewModel: CartViewModel = viewModel()
                val products by productViewModel.products.collectAsState() // Correction ici

                NavHost(navController = navController, startDestination = "productList") {

                    // 1. Écran Liste
                    composable("productList") {
                        ProductListScreen(
                            viewModel = productViewModel,
                            onProductClick = { productId -> navController.navigate("productDetail/$productId") },
                            onGoToCart = { navController.navigate("cart") }
                        )
                    }

                    // 2. Écran Détail
                    composable(
                        "productDetail/{productId}",
                        arguments = listOf(navArgument("productId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getInt("productId")
                        val product = products.find { it.id == productId }
                        product?.let {
                            ProductDetailScreen(
                                product = it,
                                onBack = { navController.popBackStack() },
                                onAddToCart = { p -> cartViewModel.addToCart(p) },
                                onGoToCart = { navController.navigate("cart") }
                            )
                        }
                    }

                    // 3. Écran Panier (Modifié pour navigation historique)
                    composable("cart") {
                        CartScreen(
                            viewModel = cartViewModel,
                            onBackToHome = { navController.navigate("productList") { popUpTo("productList") { inclusive = true } } },
                            onGoToHistory = { navController.navigate("history") } // Nouvelle navigation !
                        )
                    }

                    // 4. Écran Historique (Nouveau)
                    composable("history") {
                        HistoryScreen(
                            viewModel = cartViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductViewModel,
    onProductClick: (Int) -> Unit,
    onGoToCart: () -> Unit)
{
    val products by viewModel.products.collectAsState()
    val categories by viewModel.categories.collectAsState()
    var selectedCategory by remember { mutableStateOf("all") }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Mon E-commerce",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.clickable {
                                // Action quand on clique sur le titre (ex: recharger tout)
                                viewModel.loadProducts("all")
                            }
                        )
                    },

                    actions = {
                        IconButton(onClick = onGoToCart) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.ShoppingCart,
                                contentDescription = "Panier"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CategorySelector(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { category ->
                            selectedCategory = category
                            viewModel.loadProducts(category)
                        }
                    )
                }
                HorizontalDivider()
            }

        }
    ) { innerPadding ->
        if (products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxSize()) {
                items(products) { product ->
                    ProductItemRow(product = product) { onProductClick(product.id) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(categories: List<String>, selectedCategory: String, onCategorySelected: (String) -> Unit) {
    LazyRow(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories) { category ->
            FilterChip(
                selected = (category == selectedCategory),
                onClick = { onCategorySelected(category) },
                label = { Text(category) }
            )
        }
    }
}

@Composable
fun ProductItemRow(product: Product, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClick() }) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = product.image, contentDescription = null, modifier = Modifier.size(80.dp))
            Spacer(Modifier.width(16.dp))
            Column {
                Text(product.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${product.price} €", fontWeight = FontWeight.Bold)
            }
        }
    }
}