package me.tomasan7.opinet.util

fun <T> List<T>.replace(old: T, new: T): List<T>
{
    val index = indexOf(old)
    return if (index == -1)
        this
    else
        toMutableList().apply { set(index, new) }
}

fun <T> List<T>.replaceFirst(predicate: (T) -> Boolean, new: T): List<T>
{
    val index = indexOfFirst(predicate)
    return if (index == -1)
        this
    else
        toMutableList().apply { set(index, new) }
}
