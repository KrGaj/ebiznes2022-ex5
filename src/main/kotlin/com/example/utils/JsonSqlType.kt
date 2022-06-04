package com.example.utils

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import org.ktorm.schema.SqlType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

class JsonSqlType<T : Any>(
    val objectMapper: ObjectMapper,
    val javaType: JavaType
) : SqlType<T>(Types.VARCHAR, "json") {

    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: T) {
        ps.setString(index, objectMapper.writeValueAsString(parameter))
    }

    override fun doGetResult(rs: ResultSet, index: Int): T? {
        val json = rs.getString(index)

        return if (json.isNullOrBlank()) {
            null
        } else {
            objectMapper.readValue(json, javaType)
        }
    }
}
