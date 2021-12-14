package com.example.dictionary.feature_dictionary.domain.model

data class WordInfo(
    val meanings: List<Meaning>,
    val word: String,
    val origin: String,
    val phonetic: String
)
