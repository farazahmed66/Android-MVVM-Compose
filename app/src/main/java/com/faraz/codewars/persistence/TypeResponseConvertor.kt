package com.faraz.codewars.persistence

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

@ProvidedTypeConverter
class TypeResponseConvertor @Inject constructor(
    moshi: Moshi
) {
    private val listType =
        Types.newParameterizedType(List::class.java, String::class.java)

    private val adapter = moshi.adapter<List<String>>(listType)


    @TypeConverter
    fun fromString(value: String?): List<String> {
        return value?.let { adapter.fromJson(it) }.orEmpty()
    }

    @TypeConverter
    fun toString(list: List<String>): String {
        return adapter.toJson(list)
    }

}