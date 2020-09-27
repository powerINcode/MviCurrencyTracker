package com.example.core.mvi

interface Change

data class ChangeContainer(val changes: List<Change>) : Change

operator fun Change.plus(other: Change): Change {
    return when {
        this is ChangeContainer -> ChangeContainer(
            this.changes + listOf(other)
        )
        other is ChangeContainer -> {
            ChangeContainer(listOf(this) + other.changes)
        }
        else -> {
            ChangeContainer(listOf(this, other))
        }
    }
}