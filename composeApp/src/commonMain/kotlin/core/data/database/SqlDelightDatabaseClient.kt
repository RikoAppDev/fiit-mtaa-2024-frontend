package core.data.database

import com.grabit.GrabItDatabase
import com.grabit.User

object SqlDelightDatabaseClient {
    private val database = GrabItDatabase(SqlDelightDriverFactory().createDriver())

    fun insertFullUser(user: User) = database.userQueries.insertFullUser(user)

    fun deleteUser() = database.userQueries.deleteUser()
}