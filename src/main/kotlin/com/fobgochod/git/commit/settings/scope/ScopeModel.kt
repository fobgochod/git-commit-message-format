package com.fobgochod.git.commit.settings.scope

import com.fobgochod.git.commit.settings.GitSettings
import java.util.LinkedList
import javax.swing.table.AbstractTableModel

class ScopeModel : AbstractTableModel() {

    private val state: GitSettings get() = GitSettings.instance
    val scopeRows: MutableList<String> = LinkedList()

    override fun getRowCount() = scopeRows.size

    override fun getColumnCount() = 1

    override fun getColumnName(column: Int) = ""

    override fun getColumnClass(column: Int) = String::class.java

    override fun isCellEditable(row: Int, column: Int) = true

    override fun getValueAt(row: Int, column: Int): Any {
        return scopeRows[row]
    }

    override fun setValueAt(value: Any, row: Int, column: Int) {
        scopeRows[row] = value.toString()
    }

    fun addRow(scope: String) {
        scopeRows.add(scope)
        val lastRow = scopeRows.size - 1
        fireTableRowsInserted(lastRow, lastRow)
    }

    fun removeRow(row: Int) {
        val nonSelected = scopeRows.filterIndexed { id, _ -> id != row }
        scopeRows.clear()
        scopeRows.addAll(nonSelected)
        fireTableDataChanged()
    }

    fun isModified(): Boolean {
        return scopeRows != state.scopeRows
    }

    fun apply() {
        state.scopeRows = scopeRows
    }

    fun reset() {
        scopeRows.clear()
        for (typeRow in state.scopeRows) {
            scopeRows.add(typeRow)
        }
        fireTableDataChanged()
    }
}
