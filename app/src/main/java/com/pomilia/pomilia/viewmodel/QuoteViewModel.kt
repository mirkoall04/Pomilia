package com.pomilia.pomilia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomilia.pomilia.data.repository.QuoteRepository
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {

    private val repository = QuoteRepository()

    private val _quote = MutableLiveData<String>()
    val quote: LiveData<String> = _quote

    fun loadQuote() {
        viewModelScope.launch {
            _quote.value = "Caricamento citazione..."
            _quote.value = repository.getRandomQuote()
        }
    }
}