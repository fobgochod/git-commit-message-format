package com.fobgochod.git.commit.settings.type

import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.view.TypeDialog
import com.intellij.ui.table.JBTable
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.Collections
import javax.swing.ListSelectionModel
import javax.swing.SwingUtilities

/**
 * commit type table
 *
 * @author fobgochod
 * @date 2022/12/13 2:13
 */
class TypeTable(private val typeModel: TypeModel) : JBTable(typeModel) {

    companion object {
        const val NAME_COLUMN = 0
        const val DESCRIPTION_COLUMN = 1
    }

    init {
        val nameColumn = getColumnModel().getColumn(NAME_COLUMN)
        nameColumn.maxWidth = 120
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(mouseEvent: MouseEvent) {
                if (mouseEvent.clickCount == 2 && SwingUtilities.isLeftMouseButton(mouseEvent)) {
                    editRow()
                }
            }
        })
    }

    fun addRow() {
        val typeRow = TypeRow()
        val typeDialog = TypeDialog(GitBundle.message("settings.type.dialog.add.title"), typeRow)
        if (typeDialog.showAndGet()) {
            typeModel.addRow(typeRow)
        }
    }

    fun removeRow() {
        val selectedRow = selectedRow
        typeModel.removeRow(selectedRow)

        if (selectedRow < rowCount) {
            setRowSelectionInterval(selectedRow, selectedRow)
        } else if (rowCount > 0) {
            val index = rowCount - 1
            setRowSelectionInterval(index, index)
        }
    }

    fun editRow(): Boolean {
        if (selectedRowCount != 1) {
            return false
        }
        val typeRow = typeModel.typeRows[selectedRow].copy()
        val typeDialog = TypeDialog(GitBundle.message("settings.type.dialog.edit.title"), typeRow)
        if (typeDialog.showAndGet()) {
            typeModel.editRow(selectedRow, typeRow)
        }
        return true
    }

    fun moveUp() {
        val selectedRow = selectedRow
        val index = selectedRow - 1
        if (selectedRow != -1) {
            Collections.swap(typeModel.typeRows, selectedRow, index)
        }
        setRowSelectionInterval(index, index)
    }

    fun moveDown() {
        val selectedRow = selectedRow
        val index = selectedRow + 1
        if (selectedRow != -1) {
            Collections.swap(typeModel.typeRows, selectedRow, index)
        }
        setRowSelectionInterval(index, index)
    }
}
