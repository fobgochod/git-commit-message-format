package com.fobgochod.git.commit.domain

enum class ViewMode {

    Float,
    Window;

    override fun toString(): String {
        return this.name
    }
}
