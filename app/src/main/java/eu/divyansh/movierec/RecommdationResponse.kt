package eu.divyansh.movierec

import android.os.Parcelable



data class Recommendation(
    val poster_url: String,  // Assuming the API returns a poster URL for each recommendation
    val title: String  // Assuming the API returns a title for each recommendation

)

data class RecommendationResponse(
    val recommendations: List<Recommendation>  // Change List<String> to List<Recommendation>
)

