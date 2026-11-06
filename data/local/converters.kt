package com.alborihi.myapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.alborihi.myapp.data.local.entity.SubjectGradeEntity

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",").orEmpty()
    }

    @TypeConverter
    fun toString(list: List<String>?): String {
        return list?.joinToString(",").orEmpty()
    }

    @TypeConverter
    fun fromSubjectGradeMap(map: Map<String, SubjectGradeEntity>?): String {
        return map?.let { gson.toJson(it) }.orEmpty()
    }

    @TypeConverter
    fun toSubjectGradeMap(json: String): Map<String, SubjectGradeEntity> {
        if (json.isEmpty()) return emptyMap()
        val type = object : TypeToken<Map<String, SubjectGradeEntity>>() {}.type
        return gson.fromJson(json, type)
    }
}
