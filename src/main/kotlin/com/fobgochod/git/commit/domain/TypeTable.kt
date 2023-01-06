package com.fobgochod.git.commit.domain

import com.fobgochod.git.commit.settings.GitState
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
    private val myTableModel: MyTableModel = MyTableModel()
    val typeRows: MutableList<TypeRow> = LinkedList()

    /**
     * instantiation TypeTable
     */
    init {
        model = myTableModel
        val titleColumn = getColumnModel().getColumn(TITLE_COLUMN)
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
                val titleValue = getTitleValueAt(row)
                component.foreground =
                    if (titleValue.isEmpty()) JBColor.RED else if (isSelected) table.selectionForeground else table.foreground
                return component
            }
        }
        setColumnSize(titleColumn, 150, 250, 150)
        setColumnSize(descriptionColumn, 550, 750, 550)
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
    }

    fun getTitleValueAt(row: Int): String {
        return getValueAt(row, TITLE_COLUMN) as String
    }

    fun addRow() {
        val rowEditor = TypeEditor("Add Type", "", "")
        if (rowEditor.showAndGet()) {
            val title = rowEditor.title()
            typeRows.add(TypeRow(title, rowEditor.description()))
            val index = indexOfRowWithTitle(title)
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
        val editor = TypeEditor("Edit Type", typeRow.title, typeRow.description)
        if (editor.showAndGet()) {
            typeRow.title = editor.title()
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
        val selectedRow = selectedRow
        val typeRow = typeRows[selectedRow]
        val oldTypeRow = GitState.getInstance().typeRows[selectedRow]
        return typeRow != oldTypeRow
    }

    fun resetRow() {
        val selectedRow = selectedRow
        val typeRow = typeRows[selectedRow]
        val oldTypeRow = GitState.getInstance().typeRows[selectedRow]
        typeRow.title = oldTypeRow.title
        typeRow.description = oldTypeRow.description
        myTableModel.fireTableDataChanged()
    }

    private fun isValidRow(selectedRow: Int): Boolean {
        return selectedRow >= 0 && selectedRow < typeRows.size
    }

    fun reset(state: GitState) {
        typeRows.clear()
        for (typeRow in state.typeRows) {
            typeRows.add(typeRow.copy())
        }
        myTableModel.fireTableDataChanged()
    }

    private fun indexOfRowWithTitle(title: String): Int {
        for (index in typeRows.indices) {
            val typeRow = typeRows[index]
            if (title == typeRow.title) {
                return index
            }
        }
        return -1
    }

    //==========================================================================//
    /**
     * EditValidator
     */
    private class EditValidator : TypeEditor.Validator {
        override fun isOK(name: String, value: String): Boolean {
            return name.isNotEmpty() && value.isNotEmpty()
        }
    }

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
                TITLE_COLUMN -> return "title"
                DESCRIPTION_COLUMN -> return "description"
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
                TITLE_COLUMN -> return row.title
                DESCRIPTION_COLUMN -> return row.description
            }
            return null
        }

        override fun setValueAt(aValue: Any, rowIndex: Int, columnIndex: Int) {}
    }

    companion object {
        private const val TITLE_COLUMN = 0
        private const val DESCRIPTION_COLUMN = 1

        fun setColumnSize(column: TableColumn, preferredWidth: Int, maxWidth: Int, minWidth: Int) {
            column.preferredWidth = preferredWidth
            column.maxWidth = maxWidth
            column.minWidth = minWidth
        }
    }
}