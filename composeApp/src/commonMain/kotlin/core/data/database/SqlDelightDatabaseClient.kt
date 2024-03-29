package core.data.database

import account_detail.domain.model.UpdateUser
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
        database.userQueries.selectUserToken(email).executeAsOne().token.toString()

    fun selectUser(): User = database.userQueries.selectUser().executeAsOne()

    fun updateUser(updateUserData: UpdateUser, email: String) = database.userQueries.updateUser(
        updateUserData.name,
        updateUserData.phoneNumber,
        email = email
    )
}