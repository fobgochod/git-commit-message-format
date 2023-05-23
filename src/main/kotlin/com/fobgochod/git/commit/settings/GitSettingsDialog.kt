package com.fobgochod.git.commit.settings

import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project

object GitSettingsDialog {

    @JvmStatic
    fun showSettingsDialog(project: Project?) =
        ShowSettingsUtil.getInstance().showSettingsDialog(project, GitSettingsConfigurable::class.java)
}
