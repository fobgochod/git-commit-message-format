package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.TypeEnum
import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.util.GitIcons
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.util.*

class ResetAllTypesAction(private val typeTable: TypeTable) :
    AnAction("Restore", "Restore change type to initial.", GitIcons.RESTORE) {

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

    override fun update(event: AnActionEvent) {
        super.update(event)
        event.presentation.isEnabled = typeRows != typeTable.typeRows
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
