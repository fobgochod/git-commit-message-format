package com.fobgochod.git.commit.settings.scope

import com.fobgochod.git.commit.settings.GitSettings
import com.intellij.execution.util.ListTableWithButtons
import com.intellij.ide.ui.laf.darcula.DarculaUIUtil
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import javax.swing.DefaultCellEditor
import javax.swing.table.TableCellEditor

/**
 * Commit scope table
 *
 * @author fobgochod
 */
class ScopeListTable : ListTableWithButtons<ScopeListTable.Item>() {

    data class Item(var scope: String)

    companion object {
        private val state = GitSettings.instance
        private val NAME_COLUMN = object : ColumnInfo<Item, String>("") {

            override fun valueOf(item: Item): String = item.scope

            override fun getEditor(item: Item): TableCellEditor {
                val cellEditor = JBTextField()
                cellEditor.putClientProperty(DarculaUIUtil.COMPACT_PROPERTY, true)
                return DefaultCellEditor(cellEditor)
            }

            override fun isCellEditable(item: Item): Boolean = true

            override fun setValue(item: Item, value: String) {
                item.scope = value
            }
        }
    }

    override fun createListModel(): ListTableModel<Item> {
        return ListTableModel(NAME_COLUMN)
    }

    override fun createElement(): Item = Item("")

    override fun isEmpty(element: Item): Boolean = element.scope.isEmpty()

    override fun isUpDownSupported(): Boolean = true

    override fun cloneElement(variable: Item): Item = Item(variable.scope)

    override fun canDeleteElement(selection: Item): Boolean = true

    fun reset() {
        setValues(state.scopeRows.map { Item(it) }.toMutableList())
    }

    fun apply() {
        state.scopeRows.apply {
            clear()
            addAll(elements.map { it.scope })
        }
    }

    fun isModified(): Boolean {
        return state.scopeRows != elements.map { it.scope }
    }
}
