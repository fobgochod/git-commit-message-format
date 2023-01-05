package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.TypeTable
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ResetChangeTypeAction(private val typeTable: TypeTable) :
    AnAction("Reset", "Reset Default Change Type", AllIcons.Actions.Rollback) {

    init {

    }

    override fun actionPerformed(anActionEvent: AnActionEvent) {
        typeTable.resetRow()
    }
}
