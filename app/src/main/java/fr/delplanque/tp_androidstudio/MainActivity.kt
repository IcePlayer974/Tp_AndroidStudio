package fr.delplanque.tp_androidstudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.delplanque.tp_androidstudio.ui.theme.Tp_AndroidStudioTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tp_AndroidStudioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {  innerPadding ->
                    // Greeting(
                    //    name = "Android",
                    //    modifier = Modifier.padding(innerPadding)
                    // )
                    productCard(
                        product = Product(
                            1,
                            "Test",
                            18.5,
                            "Test",
                            "Test",
                            "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_t.png",
                            Rating(1.0,1)),

                        onClick = {},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Composable
fun productCard(product : Product, onClick:(id : Int) -> Unit, modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(product.id) }
    ) {
        Column{
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.description,
                modifier = Modifier.height(150.dp).fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
            Text(text = product.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "${product.price} €", color = Color.Green)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GreetingPreview() {
    Tp_AndroidStudioTheme {
        Greeting("Android")
    }
}