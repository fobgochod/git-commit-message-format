package com.fobgochod.git.commit.domain.option

import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.util.GitBundle.message
import java.util.LinkedList
import java.util.Locale

enum class CommitType {

    FEAT,
    FIX,
    DOCS,
    STYLE,
    REFACTOR,
    PERF,
    TEST,
    BUILD,
    CI,
    CHORE,
    REVERT;

    companion object {
        val typeRows: MutableList<TypeRow> = LinkedList()

        init {
            if (typeRows.isEmpty()) {
                entries.forEach { type ->
                    typeRows.add(TypeRow(type.type(), type.description()))
                }
            }
        }
    }

    fun type(): String {
        return name.lowercase(Locale.getDefault())
    }

    fun description(): String {
        return message("change.type.${this.type()}")
    }

    override fun toString(): String {
        return this.type() + " - " + this.description()
    }
}
