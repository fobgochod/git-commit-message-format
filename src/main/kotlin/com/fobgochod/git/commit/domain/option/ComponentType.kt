package com.fobgochod.git.commit.domain.option

import com.fobgochod.git.commit.util.GitBundle
import java.util.Locale

enum class ComponentType() {

    TypeGroup("type"),
    Type,
    Scope,
    Subject,
    Body,
    WrapText("wrap.text"),
    Breaking,
    Issues,
    SkipCI("skip.ci");

    private var label: String = ""

    constructor(label: String) : this() {
        this.label = label
    }

    fun type(): String {
        if (this.label == "") {
            return name.lowercase(Locale.getDefault())
        }
        return label
    }

    fun description(): String {
        return GitBundle.message("dialog.form.label.${this.type()}")
    }

    override fun toString(): String {
        return this.name
    }
}
