package eu.divyansh.movierec


import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator


import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MovieRecommendationScreen() {
    var movieName by remember { mutableStateOf("") }
    var recommendations by remember { mutableStateOf(listOf<Recommendation>()) }
    var errorMessage by remember { mutableStateOf("") }
    var searchPerformed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Movie Recommender", fontSize = 40.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive
        )

        OutlinedTextField(
            value = movieName,
            onValueChange = { movieName = it },
            label = { Text("Enter Movie Name") },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            if (movieName.isNotEmpty()) {
                errorMessage = "" // Clear previous errors
                isLoading = true // Set loading state

                Retro.instance.getRecommendations(movieName).enqueue(object :
                    Callback<RecommendationResponse> {
                    override fun onResponse(
                        call: Call<RecommendationResponse>,
                        response: Response<RecommendationResponse>
                    ) {
                        if (response.isSuccessful) {
                            recommendations = response.body()?.recommendations ?: emptyList()
                        } else {
                            errorMessage = "Error: ${response.code()} - ${response.message()}"
                        }
                        isLoading = false // Reset loading state
                    }

                    override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                        errorMessage = "Failure: ${t.message ?: "Unknown error"}"
                        isLoading = false // Reset loading state
                    }
                })

                // Start the coroutine to handle delay after initiating the request
                coroutineScope.launch {
                    delay(2000L) // 2 seconds delay
                    if (recommendations.isEmpty() && errorMessage.isEmpty() && !isLoading) {
                        searchPerformed = true
                    }
                }
            }
        }) {
            Text("Get Recommendations")
        }


        Spacer(modifier = Modifier.height(16.dp))


        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            isLoading= false
        }

        // Display movie posters and names in a grid
        if (recommendations.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(recommendations.size) { index ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        val painter = rememberAsyncImagePainter(recommendations[index].poster_url)

                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(0.5f),
                            contentScale = ContentScale.Fit
                        )

                        Text(recommendations[index].title, color = Color.Black)
                    }
                }

                // Add a full-width footer item at the bottom of the grid
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        "made with love by divu",
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }



        // Check if no recommendations and search was performed
        if (recommendations.isEmpty() && searchPerformed) {
            Spacer(modifier = Modifier.height(200.dp))
            Text(
                "Not Present in Database ", fontSize = 20.sp,
                textAlign = TextAlign.Justify,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(323.dp))
            Text(
                "made with love by divu",
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        // Display error message
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(550.dp))
        Text(
            "made with love by divu",
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )




    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MovieRecommendationScreen()
}

