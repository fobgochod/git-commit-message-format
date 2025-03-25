package com.fobgochod.git.commit.settings.type

import com.fobgochod.git.commit.action.ResetTypeAction
import com.fobgochod.git.commit.action.RestoreTypesAction
import com.fobgochod.git.commit.action.TypeCountAction
import com.intellij.openapi.actionSystem.ActionToolbarPosition
import com.intellij.ui.ToolbarDecorator

/**
 *  TypeToolbarDecorator.java
 *
 * @author fobgochod
 */
class TypeToolbarDecorator {

    private val typeModel: TypeModel = TypeModel()
    private val typeTable: TypeTable = TypeTable(typeModel)
    private val typeCountAction = TypeCountAction(typeModel)
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
        decorator.addExtraAction(typeCountAction)
    }

    fun isModified(): Boolean {
        return typeModel.isModified() || typeCountAction.isModified()
    }

    fun apply() {
        typeModel.apply()
        typeCountAction.apply()
    }

    fun reset() {
        typeModel.reset()
        typeCountAction.reset()
    }
}


