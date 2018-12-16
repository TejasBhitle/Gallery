package com.tejasbhitle.gallery.enums

enum class Sort private constructor(private val sort: String) {
    DATE_ASCEND("DATE_ASCEND"),
    DATE_DESCEND("DATE_DESCEND"),
    NAME_ASCEND("NAME_ASCEND"),
    NAME_DESCEND("NAME_DESCEND");

    override fun toString(): String {
        return sort
    }

    companion object {

        fun getSort(sort: String): Sort {
            return when(sort){
                DATE_ASCEND.toString() ->  DATE_ASCEND
                DATE_DESCEND.toString() -> DATE_DESCEND
                NAME_DESCEND.toString() -> NAME_DESCEND
                else -> return NAME_ASCEND
            }
        }
    }
}
