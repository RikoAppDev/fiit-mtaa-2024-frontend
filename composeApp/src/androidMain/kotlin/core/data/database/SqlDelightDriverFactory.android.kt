package core.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.grabit.GrabItDatabase
import com.grabit.applicationContext

actual class SqlDelightDriverFactory {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = GrabItDatabase.Schema,
            context = applicationContext,
            name = "GrabItDatabase.db"
        )
    }
}