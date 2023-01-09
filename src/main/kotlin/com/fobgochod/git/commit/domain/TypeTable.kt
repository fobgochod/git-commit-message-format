package com.fobgochod.git.commit.domain

import com.fobgochod.git.commit.settings.GitState
import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.view.TypeEditor
import com.intellij.ui.JBColor
import com.intellij.ui.table.JBTable
import java.awt.Component
import java.util.*
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableColumn

/**
 * TypeTable.java
 *
 * @author fobgochod
 * @date 2022/12/13 2:13
 */
class TypeTable : JBTable() {

    private var state: GitState = GitState.getInstance()
    private val myTableModel: MyTableModel = MyTableModel()
    val typeRows: MutableList<TypeRow> = LinkedList()

    init {
        model = myTableModel
        val nameColumn = getColumnModel().getColumn(NAME_COLUMN)
        val descriptionColumn = getColumnModel().getColumn(DESCRIPTION_COLUMN)
        descriptionColumn.cellRenderer = object : DefaultTableCellRenderer() {
            override fun getTableCellRendererComponent(
                table: JTable,
                value: Any,
                isSelected: Boolean,
                hasFocus: Boolean,
                row: Int,
                column: Int
            ): Component {
                val component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
                val nameValue = getNameValueAt(row)
                if (nameValue.isEmpty()) {
                    component.foreground = JBColor.RED
                } else if (isSelected) {
                    component.foreground = table.selectionForeground
                } else {
                    component.foreground = table.foreground
                }
                return component
            }
        }
        setColumnSize(nameColumn, 150, 250, 150)
        setColumnSize(descriptionColumn, 550, 750, 550)
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
    }

    fun getNameValueAt(row: Int): String {
        return getValueAt(row, NAME_COLUMN) as String
    }

    fun addRow() {
        val rowEditor = TypeEditor(GitBundle.message("settings.type.dialog.add.title"), "", "")
        if (rowEditor.showAndGet()) {
            val name = rowEditor.name()
            typeRows.add(TypeRow(name, rowEditor.description()))
            val index = indexOfRowWithName(name)
            if (isValidRow(index)) {
                myTableModel.fireTableDataChanged()
                setRowSelectionInterval(index, index)
            }
        }
    }

    fun removeRow() {
        val selectedRows = selectedRows
        if (selectedRows.isEmpty()) return
        Arrays.sort(selectedRows)
        val originalRow = selectedRows[0]
        for (index in selectedRows.indices.reversed()) {
            val selectedRow = selectedRows[index]
            if (isValidRow(selectedRow)) {
                typeRows.removeAt(selectedRow)
            }
        }
        myTableModel.fireTableDataChanged()
        if (originalRow < rowCount) {
            setRowSelectionInterval(originalRow, originalRow)
        } else if (rowCount > 0) {
            val index = rowCount - 1
            setRowSelectionInterval(index, index)
        }
    }

    fun editRow(): Boolean {
        if (selectedRowCount != 1) {
            return false
        }
        val selectedRow = selectedRow
        val typeRow = typeRows[selectedRow]
        val editor = TypeEditor(GitBundle.message("settings.type.dialog.edit.title"), typeRow.name, typeRow.description)
        if (editor.showAndGet()) {
            typeRow.name = editor.name()
            typeRow.description = editor.description()
            myTableModel.fireTableDataChanged()
        }
        return true
    }

    fun moveUp() {
        val selectedRow = selectedRow
        val index = selectedRow - 1
        if (selectedRow != -1) {
            Collections.swap(typeRows, selectedRow, index)
        }
        setRowSelectionInterval(index, index)
    }

    fun moveDown() {
        val selectedRow = selectedRow
        val index = selectedRow + 1
        if (selectedRow != -1) {
            Collections.swap(typeRows, selectedRow, index)
        }
        setRowSelectionInterval(index, index)
    }

    fun isModified(): Boolean {
        if (isValidRow(selectedRow) && state.isValidRow(selectedRow)) {
            val typeRow = typeRows[selectedRow]
            val oldTypeRow = state.typeRows[selectedRow]
            return typeRow != oldTypeRow
        }
        return false
    }

    fun resetRow() {
        val selectedRow = selectedRow
        val typeRow = typeRows[selectedRow]
        val oldTypeRow = state.typeRows[selectedRow]
        typeRow.name = oldTypeRow.name
        typeRow.description = oldTypeRow.description
        myTableModel.fireTableDataChanged()
    }

    private fun isValidRow(selectedRow: Int): Boolean {
        return selectedRow >= 0 && selectedRow < typeRows.size
    }

    fun reset(types: List<TypeRow>) {
        typeRows.clear()
        for (typeRow in types) {
            typeRows.add(typeRow.copy())
        }
        myTableModel.fireTableDataChanged()
    }

    private fun indexOfRowWithName(name: String): Int {
        for (index in typeRows.indices) {
            val typeRow = typeRows[index]
            if (name == typeRow.name) {
                return index
            }
        }
        return -1
    }

    //==========================================================================//
    /**
     * MyTableModel
     */
    private inner class MyTableModel : AbstractTableModel() {
        override fun getRowCount(): Int {
            return typeRows.size
        }

        override fun getColumnCount(): Int {
            return 2
        }

        override fun getColumnName(columnIndex: Int): String? {
            when (columnIndex) {
                NAME_COLUMN -> return GitBundle.message("settings.type.table.column.1")
                DESCRIPTION_COLUMN -> return GitBundle.message("settings.type.table.column.2")
            }
            return null
        }

        override fun getColumnClass(columnIndex: Int): Class<String> {
            return String::class.java
        }

        override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
            return false
        }

        override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
            val row = typeRows[rowIndex]
            when (columnIndex) {
                NAME_COLUMN -> return row.name
                DESCRIPTION_COLUMN -> return row.description
            }
            return null
        }

        override fun setValueAt(aValue: Any, rowIndex: Int, columnIndex: Int) {}
    }

    companion object {
        private const val NAME_COLUMN = 0
        private const val DESCRIPTION_COLUMN = 1

        fun setColumnSize(column: TableColumn, preferredWidth: Int, maxWidth: Int, minWidth: Int) {
            column.preferredWidth = preferredWidth
            column.maxWidth = maxWidth
            column.minWidth = minWidth
        }
    }
}
