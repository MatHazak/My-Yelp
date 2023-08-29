package me.mathazak.myyelp.utils

data class DataStatus<out T>(val status: Status, val data: T? = null) {

    enum class Status {
        SUCCESS, ERROR
    }

    companion object {

        fun <T> success(data: T): DataStatus<T> =
            DataStatus(Status.SUCCESS, data)

        fun <T> error(): DataStatus<T> =
            DataStatus(Status.ERROR)
    }
}