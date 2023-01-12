package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.util.GitBundle
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.AnActionButton

class ResetTypeAction(private val typeTable: TypeTable) : AnActionButton(
    GitBundle.message("action.toolbar.reset.text"),
    GitBundle.message("action.toolbar.reset.description"),
    AllIcons.Actions.Rollback
) {

    override fun actionPerformed(event: AnActionEvent) {
        typeTable.resetRow()
    }

    override fun updateButton(event: AnActionEvent) {
        // super.update(event)
        event.presentation.isEnabled = typeTable.isModified()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
