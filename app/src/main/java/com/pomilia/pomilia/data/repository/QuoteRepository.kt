package com.pomilia.pomilia.data.repository

import com.pomilia.pomilia.data.remote.RetrofitClient

class QuoteRepository {

    suspend fun getRandomQuote(): String {
        return try {
            val quote = RetrofitClient.quoteApiService.getRandomQuote()
            "\"${quote.quote}\" - ${quote.author}"
        } catch (exception: Exception) {
            "Impossibile caricare la citazione"
        }
    }
}