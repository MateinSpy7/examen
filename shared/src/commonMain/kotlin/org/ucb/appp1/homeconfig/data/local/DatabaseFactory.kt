package org.ucb.appp1.homeconfig.data.local

expect class DatabaseFactory() {
    fun create(): HomeConfigDatabase
}
