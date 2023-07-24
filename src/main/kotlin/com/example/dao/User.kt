package com.example.dao

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
data class User(val id: Int, val name: String)

object Users : IntIdTable("UserTable") {
    val name = varchar("name", 50)
}