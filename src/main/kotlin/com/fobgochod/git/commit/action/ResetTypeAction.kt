package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.settings.type.TypeModel
import com.fobgochod.git.commit.settings.type.TypeTable
import com.fobgochod.git.commit.util.GitBundle.message
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ResetTypeAction(private val typeTable: TypeTable, private val typeModel: TypeModel) : AnAction(
    message("action.toolbar.reset.text"),
    message("action.toolbar.reset.description"),
    AllIcons.Actions.Rollback
) {

    override fun actionPerformed(event: AnActionEvent) {
        typeModel.reset(typeTable.selectedRow)
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabled = typeModel.isModified(typeTable.selectedRow)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
