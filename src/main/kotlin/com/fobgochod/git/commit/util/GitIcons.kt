package com.fobgochod.git.commit.util

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object GitIcons {

    val COMMIT_MESSAGE_ACTION = load("/icons/load.png")
    val RESTORE = load("/icons/restore.svg")

    private fun load(path: String): Icon {
        return IconLoader.getIcon(path, GitIcons::class.java)
    }
}
