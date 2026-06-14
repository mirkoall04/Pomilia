package com.pomilia.pomilia.data.remote

import retrofit2.http.GET

interface QuoteApiService {

    @GET("quotes/random")
    suspend fun getRandomQuote(): QuoteDto
}