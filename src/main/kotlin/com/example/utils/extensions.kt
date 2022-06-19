package com.example.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.ktorm.jackson.sharedObjectMapper
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.typeOf

inline fun <reified C : Any> BaseTable<*>.json(
    name: String,
    mapper: ObjectMapper = sharedObjectMapper
): Column<C> {
    return registerColumn(name, JsonSqlType(mapper, mapper.constructType(typeOf<C>())))
}


