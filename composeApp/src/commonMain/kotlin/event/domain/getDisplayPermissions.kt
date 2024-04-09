package event.domain

import auth.domain.model.AccountType
import com.grabit.User
import core.domain.event.EventStatus
import event.data.dto.EventDetailDto

data class UserPermissions(
    val displaySignIn: Boolean,
    val displayCapacityFull: Boolean,
    val displayOrganiserControls: Boolean,
    val displaySignOff: Boolean,
    val displayCannotSignAsOrganizer: Boolean,
    val displayOrganizerActions: Boolean,
)


fun getDisplayConditions(eventData: EventDetailDto, user: User): UserPermissions {
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

    val ret = UserPermissions(
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