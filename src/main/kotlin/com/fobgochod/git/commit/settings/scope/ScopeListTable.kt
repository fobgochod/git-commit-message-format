package com.fobgochod.git.commit.settings.scope

import com.fobgochod.git.commit.settings.GitSettings
import com.intellij.execution.util.ListTableWithButtons
import com.intellij.ide.ui.laf.darcula.DarculaUIUtil
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import java.util.*
import javax.swing.DefaultCellEditor
import javax.swing.table.TableCellEditor


/**
 *  commit scope table
 *
 * @author fobgochod
 * @date 2023/6/18 21:16
 */
class ScopeListTable : ListTableWithButtons<ScopeListTable.Item>() {

    data class Item(var scope: String)

    companion object {
        private val state = GitSettings.instance
        private val NAME_COLUMN = object : ColumnInfo<Item, String>("") {

            override fun valueOf(item: Item): String {
                return item.scope
            }

            override fun getEditor(item: Item): TableCellEditor {
                val cellEditor = JBTextField()
                cellEditor.putClientProperty(DarculaUIUtil.COMPACT_PROPERTY, true)
                return DefaultCellEditor(cellEditor)
            }

            override fun isCellEditable(item: Item): Boolean {
                return true
            }

            override fun setValue(item: Item, value: String) {
                item.scope = value
            }
        }
    }

    override fun createListModel(): ListTableModel<Item> {
        return ListTableModel<Item>(NAME_COLUMN)
    }

    override fun createElement(): Item {
        return Item("")
    }

    override fun isEmpty(element: Item): Boolean {
        return element.scope.isEmpty()
    }

    override fun isUpDownSupported(): Boolean {
        return true
    }

    override fun cloneElement(variable: Item): Item {
        return Item(variable.scope)
    }

    override fun canDeleteElement(selection: Item): Boolean {
        return true
    }

    fun reset() {
        val rows: MutableList<Item> = LinkedList()
        state.scopeRows.forEach { rows.add(Item(it)) }
        setValues(rows)
    }

    fun apply() {
        state.scopeRows.clear()
        elements.forEach { state.scopeRows.add(it.scope) }
    }

    fun isModified(): Boolean {
        val rows: MutableList<String> = LinkedList()
        elements.forEach { rows.add(it.scope) }
        return state.scopeRows != rows
    }
}
