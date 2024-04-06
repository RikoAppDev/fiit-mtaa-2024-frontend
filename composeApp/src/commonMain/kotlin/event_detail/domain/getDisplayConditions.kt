package event_detail.domain

import auth.domain.model.AccountType
import com.grabit.User
import event_detail.data.dto.EventDetailDto

data class UserPermissions(
    val displaySignIn: Boolean,
    val displayCapacityFull: Boolean,
    val displayOrganiserControls: Boolean,
    val displaySignOff: Boolean,
    val displayCannotSignAsOrganizer:Boolean,
)


fun getDisplayConditions(eventData: EventDetailDto, user: User): UserPermissions {
    val isHarvester = user.accountType == AccountType.HARVESTER.toString()
    val isOwnedByUser = eventData.isOwnedByUser
    val isSignedIn = eventData.isUserSignedIn
    val isFreeCapacity = eventData.capacity - eventData.count.eventAssignment > 0

    val displaySignIn =
        isHarvester && isFreeCapacity && !isOwnedByUser && !isSignedIn

    val displayCapacityFull = isHarvester && !isOwnedByUser && !isFreeCapacity

    val displayOrganizerControls = !isHarvester && isOwnedByUser

    val displaySignOff = isHarvester && isSignedIn && !isOwnedByUser

    val displayCannotSignAsOrganizer = !isHarvester && !isOwnedByUser

    return UserPermissions(
        displaySignIn,
        displayCapacityFull,
        displayOrganizerControls,
        displaySignOff,
        displayCannotSignAsOrganizer
    )
}