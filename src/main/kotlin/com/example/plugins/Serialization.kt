package com.example.plugins

import com.example.dao.Users
import io.ktor.serialization.jackson.*
import com.fasterxml.jackson.databind.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

import org.jetbrains.exposed.dao.exceptions.EntityNotFoundException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureSerialization() {
    Database.connect(url = "jdbc:postgresql://localhost:5432/users",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "Jasmin@20")
    transaction {
        SchemaUtils.createMissingTablesAndColumns(Users)
    }
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }



    }




