package core.data.database

import app.cash.sqldelight.db.SqlDriver
import com.grabit.GrabItDatabase

expect class SqlDelightDriverFactory() {
    fun createDriver(): SqlDriver
}
