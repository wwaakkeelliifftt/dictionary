package com.example.dictionary.feature_dictionary.data.remote.dto

import com.example.dictionary.feature_dictionary.domain.model.Definition
import com.example.dictionary.feature_dictionary.domain.model.WordInfo

data class WordInfoDto(
    val meanings: List<MeaningDto>,
    val phonetics: List<PhoneticDto>,
    val word: String,
    val origin: String,
    val phonetic: String
) {
    fun toWordInfo(): WordInfo {
        return WordInfo(
            meanings = meanings.map { it.toMeaning() },
            word = word,
            origin = origin,
            phonetic = phonetic
        )
    }
}