package core.data.helpers.event

import LocationDto
import core.data.remote.dto.EventCardDto

fun printifyEventLocation(location: LocationDto):String{
    val ret = ArrayList<String>()

    if(location.name !== null){
        ret.add(location.name)
    }
    ret.add(location.city)
    return ret.joinToString(separator = ", ")
}
