package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.TypeEnum
import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.util.GitIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.AnActionButton
import java.util.*

class RestoreTypesAction(private val typeTable: TypeTable) : AnActionButton(
    GitBundle.message("action.toolbar.restore.text"),
    GitBundle.message("action.toolbar.restore.description"),
    GitIcons.RESTORE_ACTION
) {

    private val typeRows: MutableList<TypeRow> = LinkedList()

    init {
        if (typeRows.isEmpty()) {
            for (type in TypeEnum.values()) {
                typeRows.add(TypeRow(type.type(), type.description()))
            }
        }
    }

    override fun actionPerformed(event: AnActionEvent) {
        typeTable.reset(typeRows)
    }

    override fun updateButton(event: AnActionEvent) {
        // super.update(event)
        event.presentation.isEnabled = typeRows != typeTable.typeRows
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
