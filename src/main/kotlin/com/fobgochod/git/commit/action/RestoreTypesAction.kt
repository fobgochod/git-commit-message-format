package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.option.CommitType
import com.fobgochod.git.commit.settings.type.TypeModel
import com.fobgochod.git.commit.util.GitBundle.message
import com.fobgochod.git.commit.util.GitIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class RestoreTypesAction(private val typeModel: TypeModel) : AnAction(
    message("action.toolbar.restore.text"),
    message("action.toolbar.restore.description"),
    GitIcons.RESTORE_ACTION
) {

    override fun actionPerformed(event: AnActionEvent) {
        typeModel.restore()
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabled = CommitType.typeRows != typeModel.typeRows
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
