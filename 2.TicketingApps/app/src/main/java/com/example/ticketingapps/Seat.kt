package com.example.ticketingapps

data class Seat(
    val id: Int,
    var x: Float? = 0f,
    var y: Float? = 0f,
    var name: String,
    var isBooked: Boolean
)

val seats: ArrayList<Seat> = arrayListOf(
    Seat(id = 1, name = "A1", isBooked = false),
    Seat(id = 2, name = "A2", isBooked = false),
    Seat(id = 3, name = "B1", isBooked = false),
    Seat(id = 4, name = "A4", isBooked = false),
    Seat(id = 5, name = "C1", isBooked = false),
    Seat(id = 6, name = "C2", isBooked = false),
    Seat(id = 7, name = "D1", isBooked = false),
    Seat(id = 8, name = "D2", isBooked = false),
)
