package com.fobgochod.git.commit.settings.scope

import com.intellij.openapi.actionSystem.ActionToolbarPosition
import com.intellij.ui.ToolbarDecorator

/**
 *  ScopeToolbarDecorator.java
 *
 * @author fobgochod
 * @date 2023/6/14 22:07
 */
class ScopeToolbarDecorator {

    private val scopeModel: ScopeModel = ScopeModel()
    private val scopeTable: ScopeTable = ScopeTable(scopeModel)
    val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(scopeTable)

    init {
        decorator.setToolbarPosition(ActionToolbarPosition.TOP)
        decorator.setAddAction { scopeTable.addRow() }
        decorator.setRemoveAction { scopeTable.removeRow() }
        decorator.setMoveUpAction { scopeTable.moveUp() }
        decorator.setMoveDownAction { scopeTable.moveDown() }
    }

    fun isModified(): Boolean {
        return scopeModel.isModified()
    }

    fun apply() {
        scopeModel.apply()
    }

    fun reset() {
        scopeModel.reset()
    }
}


