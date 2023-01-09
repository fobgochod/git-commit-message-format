package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.util.GitBundle
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ResetTypeAction(private val typeTable: TypeTable) : AnAction(
    GitBundle.message("action.toolbar.reset.text"),
    GitBundle.message("action.toolbar.reset.description"),
    AllIcons.Actions.Rollback
) {

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
