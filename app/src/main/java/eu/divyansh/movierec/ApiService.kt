package eu.divyansh.movierec

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/recommend") // Make sure this matches your Flask route
    fun getRecommendations(@Query("movie") movie: String): Call<RecommendationResponse>
}
