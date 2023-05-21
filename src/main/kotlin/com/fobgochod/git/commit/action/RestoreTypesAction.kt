package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.CommitType
import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.util.GitIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.AnActionButton

class RestoreTypesAction(private val typeTable: TypeTable) : AnActionButton(
    GitBundle.message("action.toolbar.restore.text"),
    GitBundle.message("action.toolbar.restore.description"),
    GitIcons.RESTORE_ACTION
) {

    override fun actionPerformed(event: AnActionEvent) {
        typeTable.reset(CommitType.typeRows)
    }

    override fun updateButton(event: AnActionEvent) {
        // super.update(event)
        event.presentation.isEnabled = CommitType.typeRows != typeTable.typeRows
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
