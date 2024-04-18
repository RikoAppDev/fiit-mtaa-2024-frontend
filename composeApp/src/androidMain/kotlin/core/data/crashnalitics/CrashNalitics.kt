package core.data.crashnalitics

import dev.icerock.moko.crashreporting.crashlytics.CrashlyticsLogger

actual class CrashNalitics {
    actual fun useFirebaseError(e:Exception){
        val logger = CrashlyticsLogger()
        logger.setUserId("User_1")
        logger.recordException(e)
    }
}