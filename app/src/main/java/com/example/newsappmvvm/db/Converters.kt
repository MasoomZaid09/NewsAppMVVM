package com.example.newsappmvvm.db

import androidx.room.TypeConverter
import javax.xml.transform.Source

class Converters {

    @TypeConverter
    fun fromSource(source : com.example.newsappmvvm.models.Source) : String{
        return source.name
    }

    @TypeConverter
    fun toSource(name : String) : com.example.newsappmvvm.models.Source{
        return com.example.newsappmvvm.models.Source(name , name)
    }
}