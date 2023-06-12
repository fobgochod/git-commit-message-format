package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.action.ResetTypeAction
import com.fobgochod.git.commit.action.RestoreTypesAction
import com.fobgochod.git.commit.domain.TypeModel
import com.fobgochod.git.commit.domain.TypeTable
import com.intellij.openapi.actionSystem.ActionToolbarPosition
import com.intellij.ui.ToolbarDecorator

/**
 *  TypeToolbarDecorator.java
 *
 * @author fobgochod
 * @since 2023/6/12 23:45
 */
class TypeToolbarDecorator {

    private val typeModel: TypeModel = TypeModel()
    private val typeTable: TypeTable = TypeTable(typeModel)
    val decorator: ToolbarDecorator = ToolbarDecorator.createDecorator(typeTable)

    init {
        decorator.setToolbarPosition(ActionToolbarPosition.TOP)
        decorator.setAddAction { typeTable.addRow() }
        decorator.setRemoveAction { typeTable.removeRow() }
        decorator.setEditAction { typeTable.editRow() }
        decorator.setMoveUpAction { typeTable.moveUp() }
        decorator.setMoveDownAction { typeTable.moveDown() }
        decorator.addExtraAction(ResetTypeAction(typeTable, typeModel))
        decorator.addExtraAction(RestoreTypesAction(typeModel))
    }

    fun isModified(): Boolean {
        return typeModel.isModified()
    }

    fun apply() {
        typeModel.apply()
    }

    fun reset() {
        typeModel.reset()
    }
}


