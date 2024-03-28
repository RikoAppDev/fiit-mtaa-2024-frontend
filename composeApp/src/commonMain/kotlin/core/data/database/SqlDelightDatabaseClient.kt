package core.data.database

import com.grabit.GrabItDatabase
import com.grabit.User

object SqlDelightDatabaseClient {
    private val database = GrabItDatabase(SqlDelightDriverFactory().createDriver())

    fun insertFullUser(user: User) {
        database.userQueries.transaction {
            database.userQueries.deleteUser()
            database.userQueries.insertFullUser(user)
        }
    }

    fun selectUserToken(email: String): String =
        database.userQueries.selectUserToken(email).toString()

}