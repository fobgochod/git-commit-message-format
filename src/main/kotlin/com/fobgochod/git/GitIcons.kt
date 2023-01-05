package com.fobgochod.git

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object GitIcons {

    val COMMIT_MESSAGE_ACTION = load("/icons/load.png")
    val COMMIT_MESSAGE_TITLE = load("/icons/git.svg")

    private fun load(path: String): Icon {
        return IconLoader.getIcon(path, GitIcons::class.java)
    }
}
