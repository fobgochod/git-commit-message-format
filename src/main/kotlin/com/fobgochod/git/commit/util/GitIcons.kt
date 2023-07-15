package com.fobgochod.git.commit.util

import com.intellij.openapi.util.IconLoader
import icons.CollaborationToolsIcons
import javax.swing.Icon

object GitIcons {

    val COMMIT_MESSAGE_ACTION = load("/icons/logo.svg")
    val RESTORE_ACTION = load("/icons/restore.svg")

    fun getNumberIcon(number: Int): Icon {
        return when (number) {
            in 0..9 -> load("/icons/number/$number.svg")
            else -> CollaborationToolsIcons.Review.CommentReadMany
        }
    }

    private fun load(path: String): Icon {
        return IconLoader.getIcon(path, GitIcons::class.java)
    }
}

