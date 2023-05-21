package com.fobgochod.git.commit.settings.v1

import com.fobgochod.git.commit.action.ResetTypeAction
import com.fobgochod.git.commit.action.RestoreTypesAction
import com.fobgochod.git.commit.domain.SkipCIWord
import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.domain.ViewForm
import com.fobgochod.git.commit.domain.ViewMode
import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import java.util.*
import javax.swing.*

/**
 * git setting panel
 * @See com.intellij.ide.browsers.BrowserSettingsPanel
 */
internal class GitComponent2 : GitComponent {

    val state: GitState1 = GitState1.getInstance()

    /**
     * type标签
     */
    private val typeTable: TypeTable = TypeTable()
    private lateinit var typeTablePanel: JComponent

    /**
     * ButtonGroup显示type数目
     */
    private val typeCountField: JTextField = JTextField()

    /**
     *  skip workflow keywords
     */
    private lateinit var skipCI: ComboBox<SkipCIWord>

    /**
     * 弹窗方式
     */
    private lateinit var viewMode: ComboBox<ViewMode>

    /**
     * 窗口组件
     */
    private val viewForm: MutableList<JCheckBox> = LinkedList()

    private val root: JPanel = panel {
        row {
            val typeTableToolbar: ToolbarDecorator = ToolbarDecorator.createDecorator(typeTable)
                .setAddAction { typeTable.addRow() }
                .setRemoveAction { typeTable.removeRow() }
                .setEditAction { typeTable.editRow() }
                .setMoveUpAction { typeTable.moveUp() }
                .setMoveDownAction { typeTable.moveDown() }
                .addExtraAction(ResetTypeAction(typeTable))
                .addExtraAction(RestoreTypesAction(typeTable))

            cell(isFullWidth = true) {
                component(typeTableToolbar.createPanel()).constraints(grow, pushY)
                    .applyToComponent {
                        // this.border = IdeBorderFactory.createTitledBorder(GitBundle.message("settings.desc.text"), false, JBUI.insetsTop(8)).setShowLine(false)
                        typeTablePanel = this
                    }
            }
        }


        titledRow(GitBundle.message("settings.commit.panel.behavior")) {
            row(GitBundle.message("settings.common.type.count")) {
                typeCountField()
            }
            row(GitBundle.message("settings.common.hidden.ui")) {
                ViewForm.values()
                    .filter(ViewForm::isEnabled)
                    .forEach { item ->
                        cell {
                            checkBox(item.name).applyToComponent {
                                this.isEnabled = item.isEnabled()
                                this.toolTipText = item.description()
                                this.isSelected = state.isViewFormHidden(item)
                                viewForm.add(this)
                            }
                        }
                    }
            }
            row(GitBundle.message("settings.common.skip.ci.word")) {
                component(ComboBox(DefaultComboBoxModel(SkipCIWord.values()), 140)).applyToComponent {
                    this.renderer = SimpleListCellRenderer.create("") { it.label }
                    skipCI = this
                }
            }
            row(GitBundle.message("settings.common.view.mode")) {
                component(ComboBox(DefaultComboBoxModel(ViewMode.values()), 140)).applyToComponent {
                    this.renderer = SimpleListCellRenderer.create("") { it.name }
                    viewMode = this
                }
            }
        }
    }


    override fun getComponent(): JPanel {
        return root
    }

    override fun isModified(): Boolean {

        val typeRows = typeTable.typeRows
        val typeCount: Int = typeCountField.text.toInt()
        val skipCI = getSkipCI()
        val viewMode = getViewMode()
        return typeRows != state.typeRows
                || typeCount != state.typeCount
                || skipCI != state.skipCI
                || viewMode != state.viewMode
                || isViewFormModified(state)
    }

    override fun apply() {
        state.typeRows = typeTable.typeRows
        state.typeCount = typeCountField.text.toInt()
        state.skipCI = getSkipCI()
        state.viewMode = getViewMode()

        for (checkBox in viewForm) {
            val formItem = ViewForm.valueOf(checkBox.text)
            state.viewForm[formItem] = checkBox.isSelected
        }
    }

    override fun reset() {
        typeTable.reset(state.typeRows)
        typeCountField.text = state.typeCount.toString()
        skipCI.selectedItem = state.skipCI
        viewMode.selectedItem = state.viewMode

        for (checkBox in viewForm) {
            val viewForm = ViewForm.valueOf(checkBox.text)
            checkBox.isSelected = state.isViewFormHidden(viewForm)
        }
    }

    private fun getSkipCI(): SkipCIWord {
        val selectedItem = skipCI.selectedItem
        return selectedItem as SkipCIWord
    }

    private fun getViewMode(): ViewMode {
        val selectedItem = viewMode.selectedItem
        return selectedItem as ViewMode
    }

    private fun isViewFormModified(state: GitState1): Boolean {
        for (checkBox in viewForm) {
            val viewForm = ViewForm.valueOf(checkBox.text)
            if (state.isViewFormHidden(viewForm) != checkBox.isSelected) {
                return true
            }
        }
        return false
    }
}
