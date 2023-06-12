package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.TypeModel
import com.fobgochod.git.commit.domain.option.CommitType
import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.util.GitIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.AnActionButton

class RestoreTypesAction(private val typeModel: TypeModel) : AnActionButton(
    GitBundle.message("action.toolbar.restore.text"),
    GitBundle.message("action.toolbar.restore.description"),
    GitIcons.RESTORE_ACTION
) {

    override fun actionPerformed(event: AnActionEvent) {
        typeModel.restore()
    }

    override fun updateButton(event: AnActionEvent) {
        // super.update(event)
        event.presentation.isEnabled = CommitType.typeRows != typeModel.typeRows
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
