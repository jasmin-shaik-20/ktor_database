package com.example.plugins

import com.example.dao.User
import com.example.dao.Users
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting() {
    routing {
        route("/users") {
            get {
                val users = transaction {
                    Users.selectAll().map {
                        mapOf("id" to it[Users.id], "name" to it[Users.name])
                    }
                }
                call.respond(users)
            }

            post("/{name}") {
                val user= call.receive<User>()
                val userId = transaction {
                    val insertedId = Users.insertAndGetId {
                        it[name] = user.name
                    }
                    insertedId.value
                }
                call.respond(HttpStatusCode.Created, userId)
            }

            get("/{id?}") {
                val userId = call.parameters["id"]?.toIntOrNull()
                if (userId != null) {
                    val user = transaction {
                        Users.select { Users.id eq userId }.singleOrNull()?.let { mapOf("id" to it[Users.id], "name" to it[Users.name])
                            }
                    }
                    if (user != null) {
                        call.respond(user)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "User with id $userId not found")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid user id")
                }
            }

        }
    }
}
