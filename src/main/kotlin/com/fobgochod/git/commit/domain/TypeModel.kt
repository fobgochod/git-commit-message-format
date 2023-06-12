package com.fobgochod.git.commit.domain

import com.fobgochod.git.commit.domain.option.CommitType
import com.fobgochod.git.commit.settings.GitSettings
import com.fobgochod.git.commit.util.GitBundle
import java.util.LinkedList
import javax.swing.table.AbstractTableModel

private class Column(private val key: String, val type: Class<*>, val editable: Boolean) {
    val name: String
        get() = GitBundle.message(key)
}

private val columns = arrayOf(
    Column("settings.type.table.column.1", String::class.java, false),
    Column("settings.type.table.column.2", String::class.java, false)
)

class TypeModel : AbstractTableModel() {

    private val state: GitSettings get() = GitSettings.instance
    val typeRows: MutableList<TypeRow> = LinkedList()

    override fun getRowCount() = typeRows.size

    override fun getColumnCount() = columns.size

    override fun getColumnName(column: Int) = columns[column].name

    override fun getColumnClass(column: Int) = columns[column].type

    override fun isCellEditable(row: Int, column: Int) = columns[column].editable

    override fun getValueAt(row: Int, column: Int): Any? {
        return when (column) {
            TypeTable.NAME_COLUMN -> return typeRows[row].name
            TypeTable.DESCRIPTION_COLUMN -> return typeRows[row].description
            else -> null
        }
    }

    override fun setValueAt(value: Any, row: Int, column: Int) {}

    fun addRow(typeRow: TypeRow) {
        typeRows.add(typeRow)
        val lastRow = typeRows.size - 1
        fireTableRowsInserted(lastRow, lastRow)
    }

    fun removeRows(rows: IntArray) {
        val nonSelected = typeRows.filterIndexed { id, _ -> id !in rows }
        typeRows.clear()
        typeRows.addAll(nonSelected)
        fireTableDataChanged()
    }

    fun editRow(row: Int, typeRow: TypeRow) {
        typeRows[row] = typeRow
        fireTableRowsUpdated(row, row)
    }

    fun isModified(): Boolean {
        return typeRows != state.typeRows
    }

    fun apply() {
        state.typeRows = typeRows
    }

    fun reset() {
        typeRows.clear()
        for (typeRow in state.typeRows) {
            typeRows.add(typeRow.copy())
        }
        fireTableDataChanged()
    }

    fun reset(row: Int) {
        if (state.isValidRow(row)) {
            typeRows[row] = state.typeRows[row]
            fireTableDataChanged()
        }
    }

    fun isModified(row: Int): Boolean {
        if (state.isValidRow(row)) {
            return typeRows[row] != state.typeRows[row]
        }
        return false
    }

    fun restore() {
        typeRows.clear()
        typeRows.addAll(CommitType.typeRows)
        fireTableDataChanged()
    }
}
