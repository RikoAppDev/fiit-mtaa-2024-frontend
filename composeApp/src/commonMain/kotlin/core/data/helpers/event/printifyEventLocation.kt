package core.data.helpers.event

import core.data.remote.dto.EventCardDto

fun printifyEventLocation(event: EventCardDto):String{
    val ret = ArrayList<String>()

    if(event.location.name !== null){
        ret.add(event.location.name)
    }
    ret.add(event.location.city)
    return ret.joinToString(separator = ", ")
}
