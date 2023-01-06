package com.fobgochod.git.commit.domain

import com.fobgochod.git.commit.util.GitBundle
import java.util.*

enum class TypeEnum {

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

    fun title(): String {
        return name.lowercase(Locale.getDefault())
    }

    fun description(): String {
        return GitBundle.message("change.type.${this.title()}");
    }

    override fun toString(): String {
        return this.title() + " - " + this.description()
    }
}
