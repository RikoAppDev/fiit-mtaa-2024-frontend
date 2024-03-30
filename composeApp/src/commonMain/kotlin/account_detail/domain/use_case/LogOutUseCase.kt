package account_detail.domain.use_case

import core.data.database.SqlDelightDatabaseClient

class LogOutUseCase(val database:SqlDelightDatabaseClient) {
    operator fun invoke(){
        database.deleteUser()
    }
}