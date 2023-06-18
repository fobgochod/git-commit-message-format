package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.settings.type.TypeModel
import com.fobgochod.git.commit.settings.type.TypeTable
import com.fobgochod.git.commit.util.GitBundle
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.AnActionButton

class ResetTypeAction(private val typeTable: TypeTable, private val typeModel: TypeModel) : AnActionButton(
    GitBundle.message("action.toolbar.reset.text"),
    GitBundle.message("action.toolbar.reset.description"),
    AllIcons.Actions.Rollback
) {

    override fun actionPerformed(event: AnActionEvent) {
        typeModel.reset(typeTable.selectedRow)
    }

    override fun updateButton(event: AnActionEvent) {
        event.presentation.isEnabled = typeModel.isModified(typeTable.selectedRow)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
