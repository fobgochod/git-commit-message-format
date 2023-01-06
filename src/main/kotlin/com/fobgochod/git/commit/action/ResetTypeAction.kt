package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.TypeTable
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ResetTypeAction(private val typeTable: TypeTable) :
    AnAction("Reset", "Reset default change type.", AllIcons.Actions.Rollback) {

    override fun actionPerformed(event: AnActionEvent) {
        typeTable.resetRow()
    }

    override fun update(event: AnActionEvent) {
        super.update(event)
        event.presentation.isEnabled = typeTable.isModified()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
