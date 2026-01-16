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
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

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
                            viewModel = viewModel,
                            cartViewModel = cartViewModel,
                            onProductClick = { productId ->
                                navController.navigate("productDetail/$productId")
                            },
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
    cartViewModel: CartViewModel,
    onProductClick: (Int) -> Unit,
    onGoToCart: () -> Unit)
{
    val products by viewModel.sortedProducts.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val currentSort by viewModel.sortOption.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val cartCount by cartViewModel.cartCount.collectAsState()
    var selectedCategory by remember { mutableStateOf("all") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Catégories",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider()
                // Liste des catégories dans le burger
                categories.forEach { category ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = getCategoryIcon(category),
                                contentDescription = null
                            )
                        },
                        label = { Text(category.replaceFirstChar { it.uppercase() }) },
                        selected = (category == selectedCategory),
                        onClick = {
                            selectedCategory = category
                            viewModel.loadProducts(category)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    ) {
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
                                BadgedBox(
                                    badge = {
                                        if (cartCount > 0) {
                                            Badge {
                                                Text(text = cartCount.toString())
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        contentDescription = "Panier"
                                    )
                                }
                                /*Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.ShoppingCart,
                                    contentDescription = "Panier"
                                )*/
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }

                            Box(modifier = Modifier.weight(1f)) {
                                SortDropdown(
                                    currentSort = currentSort,
                                    onSortSelected = { viewModel.setSortOption(it) }
                                )
                            }
                        }

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
}

@Composable
fun getCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "electronics" -> Icons.Default.Settings
        "jewelery" -> Icons.Default.Star
        "men's clothing" -> Icons.Default.Person
        "women's clothing" -> Icons.Default.Face
        "all" -> Icons.Default.List
        else -> Icons.Default.List
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortSelector(currentSort: SortOption, onSortSelected: (SortOption) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(SortOption.entries) { option ->
            FilterChip(
                selected = (option == currentSort),
                onClick = { onSortSelected(option) },
                label = { Text(option.label, style = MaterialTheme.typography.bodySmall) }
            )
        }
    }
}

@Composable
fun SortDropdown(currentSort: SortOption, onSortSelected: (SortOption) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Trier par : ${currentSort.label}")
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f) // Largeur du menu
        ) {
            SortOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onSortSelected(option)
                        expanded = false
                    },
                    leadingIcon = {
                        if (option == currentSort) Icon(Icons.Default.Check, contentDescription = null)
                    }
                )
            }
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
                Row {
                    Text("${product.price} €", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Text("⭐ ${product.rating.rate}", color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}
