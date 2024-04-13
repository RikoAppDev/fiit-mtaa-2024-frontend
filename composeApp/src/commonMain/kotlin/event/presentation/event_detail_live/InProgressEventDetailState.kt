package event.presentation.event_detail_live

import event.data.dto.AttendanceDataDto
import event.data.dto.LiveEventDataDto
import event.domain.InProgressEventPermissions

data class InProgressEventDetailState (
    val isLoadingLiveEventData:Boolean,
    val isLoadingAttendanceData:Boolean,
    val liveEventData:LiveEventDataDto?,
    val attendanceData:AttendanceDataDto?,
    val errorLiveEventData:String,
    val errorAttendanceData:String,
    val permissions: InProgressEventPermissions?
)
