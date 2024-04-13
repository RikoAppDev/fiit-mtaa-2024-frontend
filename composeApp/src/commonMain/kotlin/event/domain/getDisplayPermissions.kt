package event.domain

import auth.domain.model.AccountType
import com.grabit.User
import core.domain.event.EventStatus
import event.data.dto.EventDetailDto

data class EventDetailPermissions(
    val displaySignIn: Boolean,
    val displayCapacityFull: Boolean,
    val displayOrganiserControls: Boolean,
    val displaySignOff: Boolean,
    val displayCannotSignAsOrganizer: Boolean,
    val displayOrganizerActions: Boolean,
)

data class InProgressEventPermissions(
    val displayWorkers:Boolean,
    val displayEndEvent:Boolean,
)


fun getEventDetailDisplayConditions(eventData: EventDetailDto, user: User): EventDetailPermissions {
    val isHarvester = user.accountType == AccountType.HARVESTER.toString()
    val isOwnedByUser = eventData.isOwnedByUser
    val isSignedIn = eventData.isUserSignedIn
    val isFreeCapacity = eventData.capacity - eventData.count.eventAssignment > 0

    val displaySignIn =
        isHarvester && isFreeCapacity && !isOwnedByUser && !isSignedIn && eventData.status == EventStatus.CREATED

    val displayCapacityFull = isHarvester && !isOwnedByUser && !isFreeCapacity && !isSignedIn

    val displayOrganizerControls = !isHarvester && isOwnedByUser

    val displayOrganizerActions =
        displayOrganizerControls && eventData.status == EventStatus.CREATED

    val displaySignOff =
        isHarvester && isSignedIn && !isOwnedByUser && eventData.status == EventStatus.CREATED

    val displayCannotSignAsOrganizer = !isHarvester && !isOwnedByUser

    val ret = EventDetailPermissions(
        displaySignIn,
        displayCapacityFull,
        displayOrganizerControls,
        displaySignOff,
        displayCannotSignAsOrganizer,
        displayOrganizerActions
    )
    println(ret)
    return ret
}

fun getInProgressEventDisplayConditions(user:User):InProgressEventPermissions{
    return InProgressEventPermissions(
        displayEndEvent = user.accountType == AccountType.ORGANISER.toString(),
        displayWorkers = user.accountType == AccountType.ORGANISER.toString(),
    )
}