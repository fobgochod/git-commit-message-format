package com.fobgochod.git.commit.settings.scope

import com.intellij.ui.table.JBTable
import java.awt.Dimension
import java.util.Collections
import javax.swing.ListSelectionModel
import javax.swing.table.DefaultTableCellRenderer

/**
 *  commit scope table
 *
 * @author fobgochod
 * @date 2023/6/13 23:19
 */
class ScopeTable(private val scopeModel: ScopeModel) : JBTable(scopeModel) {

    init {
        val renderer = DefaultTableCellRenderer()
        renderer.preferredSize = Dimension(0, 0)
        tableHeader.defaultRenderer = renderer
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
    }

    fun addRow() {
        val selectValue = scopeModel.scopeRows[selectedRow - 1]
        if (selectValue.isEmpty()) return

        scopeModel.addRow("")

        val lastRow = rowCount - 1
        addRowSelectionInterval(lastRow, lastRow)
    }

    fun removeRow() {
        val selectedRow = selectedRow
        scopeModel.removeRow(selectedRow)

        if (selectedRow < rowCount) {
            setRowSelectionInterval(selectedRow, selectedRow)
        } else if (rowCount > 0) {
            val index = rowCount - 1
            setRowSelectionInterval(index, index)
        }
    }

    fun moveUp() {
        val selectedRow = selectedRow
        val index = selectedRow - 1
        if (selectedRow != -1) {
            Collections.swap(scopeModel.scopeRows, selectedRow, index)
        }
        setRowSelectionInterval(index, index)
    }

    fun moveDown() {
        val selectedRow = selectedRow
        val index = selectedRow + 1
        if (selectedRow != -1) {
            Collections.swap(scopeModel.scopeRows, selectedRow, index)
        }
        setRowSelectionInterval(index, index)
    }
}
