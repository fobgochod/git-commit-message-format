package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.settings.GitSettings
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

    private val state: GitSettings = GitSettings.instance

    override fun actionPerformed(event: AnActionEvent) {
        val selectedRow = typeTable.selectedRow
        if (state.isValidRow(selectedRow)) {
            val oldTypeRow = state.typeRows[selectedRow]
            typeTable.resetRow(oldTypeRow)
        }
    }

    override fun updateButton(event: AnActionEvent) {
        val selectedRow = typeTable.selectedRow
        if (state.isValidRow(selectedRow)) {
            val oldTypeRow = state.typeRows[selectedRow]
            event.presentation.isEnabled = typeTable.isModified(oldTypeRow)
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
