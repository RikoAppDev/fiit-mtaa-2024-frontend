package core.data.database

import account_detail.domain.model.UpdateUser
import com.grabit.GrabItDatabase
import com.grabit.User
import event.domain.model.PresenceStatus
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

object SqlDelightDatabaseClient {
    private val database = GrabItDatabase(SqlDelightDriverFactory().createDriver())

    fun insertFullUser(user: User) {
        database.userQueries.transaction {
            database.userQueries.deleteUser()
            database.userQueries.insertFullUser(user)
        }
    }

    fun selectUserToken(): String =
        database.userQueries.selectUserToken().executeAsOne().token.toString()

    fun selectUser(): User = database.userQueries.selectUser().executeAsOne()

    fun updateUser(updateUserData: UpdateUser, email: String) = database.userQueries.updateUser(
        updateUserData.name,
        updateUserData.phoneNumber,
        email = email
    )

    fun deleteUser() {
        database.userQueries.deleteUser()
    }

    fun insertEvent(id: String, userName: String, name: String) {
        database.eventQueries.deleteEvent()
        return database.eventQueries.insertEvent(id, userName, name)
    }

    fun selectEvent() = database.eventQueries.getEvent().executeAsOne()
    fun deleteEvent() = database.eventQueries.deleteEvent()

    fun deleteAttendance() = database.attendanceQueries.deleteAttendance()

    fun insertAttendanceItem(
        userId: String,
        presenceStatus: PresenceStatus,
        arrivedAt: Instant?,
        leftAt: Instant?,
        name: String
    ) =
        database.attendanceQueries.insertAttendanceItem(
            userID = userId,
            presenceStatus = presenceStatus.toString(),
            arrivedAt = arrivedAt.toString(),
            leftAt = leftAt.toString(),
            updatedAt = Clock.System.now().toString(),
            name = name
        )

    fun selectAttendanceItems() = database.attendanceQueries.selectAttendance().executeAsList()

    fun selectLastUpdated() = database.attendanceQueries.selectLastUpdated().executeAsOne()

    fun updateAttendanceItem(
        userId: String,
        presenceStatus: PresenceStatus,
        arrivedAt: Instant?,
        leftAt: Instant?
    ) = database.attendanceQueries.updateAttendanceItem(
        userID = userId,
        presenceStatus = presenceStatus.toString(),
        arrivedAt = arrivedAt.toString(),
        leftAt = leftAt.toString(),
        updatedAt = Clock.System.now().toString()
    )

}