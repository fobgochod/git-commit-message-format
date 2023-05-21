package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.action.ResetTypeAction
import com.fobgochod.git.commit.action.RestoreTypesAction
import com.fobgochod.git.commit.domain.SkipCIWord
import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.domain.ViewForm
import com.fobgochod.git.commit.domain.ViewMode
import com.fobgochod.git.commit.util.GitBundle
import com.intellij.application.options.editor.CheckboxDescriptor
import com.intellij.application.options.editor.checkBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.*

private val state: GitState get() = GitState.getInstance()

private val hideTypeGroup get() = CheckboxDescriptor(ViewForm.TypeGroup.name, state::hideTypeGroup)
private val hideType get() = CheckboxDescriptor(ViewForm.Type.name, state::hideType)
private val hideScope get() = CheckboxDescriptor(ViewForm.Scope.name, state::hideScope)
private val hideSubject get() = CheckboxDescriptor(ViewForm.Subject.name, state::hideSubject)
private val hideBody get() = CheckboxDescriptor(ViewForm.Body.name, state::hideBody)
private val hideWrapText get() = CheckboxDescriptor(ViewForm.WrapText.name, state::hideWrapText)
private val hideBreaking get() = CheckboxDescriptor(ViewForm.Breaking.name, state::hideBreaking)
private val hideIssues get() = CheckboxDescriptor(ViewForm.Issues.name, state::hideIssues)
private val hideSkipCI get() = CheckboxDescriptor(ViewForm.SkipCI.name, state::hideSkipCI)


/**
 * git setting panel
 * @see ApplicationBundle.properties
 * @see PlatformExtensions.xml
 * @See com.intellij.application.options.editor.EditorOptionsPanel
 */
internal class GitPanel {

    /**
     * type标签
     */
    private val typeTable: TypeTable = TypeTable()

    private val root: DialogPanel = panel {
        row {
            typeTable.reset(state.typeRows)
            cell(
                ToolbarDecorator.createDecorator(typeTable)
                    .setAddAction { typeTable.addRow() }
                    .setRemoveAction { typeTable.removeRow() }
                    .setEditAction { typeTable.editRow() }
                    .setMoveUpAction { typeTable.moveUp() }
                    .setMoveDownAction { typeTable.moveDown() }
                    .addExtraAction(ResetTypeAction(typeTable))
                    .addExtraAction(RestoreTypesAction(typeTable))
                    .createPanel()
            ).apply {
                object : DoubleClickListener() {
                    override fun onDoubleClick(event: MouseEvent): Boolean {
                        return typeTable.editRow()
                    }
                }.installOn(typeTable)
            }.horizontalAlign(HorizontalAlign.FILL)
                .verticalAlign(VerticalAlign.FILL)
        }.resizableRow()

        group(GitBundle.message("settings.commit.panel.ui.hidden")) {
            row {
                checkBox(hideTypeGroup).applyToComponent {
                    this.toolTipText = ViewForm.TypeGroup.description()
                }.gap(RightGap.SMALL)

                checkBox(hideType).applyToComponent {
                    this.isEnabled = false
                    this.toolTipText = ViewForm.Type.description()
                }.gap(RightGap.SMALL)

                checkBox(hideScope).applyToComponent {
                    this.toolTipText = ViewForm.Scope.description()
                }.gap(RightGap.SMALL)

                checkBox(hideSubject).applyToComponent {
                    this.isEnabled = false
                    this.toolTipText = ViewForm.Subject.description()
                }.gap(RightGap.SMALL)

                checkBox(hideBody).applyToComponent {
                    this.toolTipText = ViewForm.Body.description()
                }.gap(RightGap.SMALL)

                checkBox(hideWrapText).applyToComponent {
                    this.toolTipText = ViewForm.WrapText.description()
                }.gap(RightGap.SMALL)

                checkBox(hideBreaking).applyToComponent {
                    this.toolTipText = ViewForm.Breaking.description()
                }.gap(RightGap.SMALL)

                checkBox(hideIssues).applyToComponent {
                    this.toolTipText = ViewForm.Issues.description()
                }.gap(RightGap.SMALL)

                checkBox(hideSkipCI).applyToComponent {
                    this.toolTipText = ViewForm.SkipCI.description()
                }
            }.layout(RowLayout.PARENT_GRID)
        }

        group(GitBundle.message("settings.commit.panel.behavior")) {
            row {
                intTextField()
                    .label(GitBundle.message("settings.common.type.count"))
                    .bindIntText(state::typeCount).columns(5)
                    .gap(RightGap.COLUMNS)

                comboBox<SkipCIWord>(DefaultComboBoxModel(SkipCIWord.values()),
                    renderer = SimpleListCellRenderer.create("") { it.label })
                    .label(GitBundle.message("settings.common.skip.ci.word"))
                    .bindItem(state::skipCI.toNullableProperty())
                    .gap(RightGap.COLUMNS)

                comboBox<ViewMode>(DefaultComboBoxModel(ViewMode.values()),
                    renderer = SimpleListCellRenderer.create("") { it.name })
                    .label(GitBundle.message("settings.common.view.mode"))
                    .bindItem(state::viewMode.toNullableProperty())
            }
        }.layout(RowLayout.INDEPENDENT)
    }

    fun createPanel(): DialogPanel {
        return root
    }

    fun isModified(): Boolean {
        return typeTable.typeRows != state.typeRows
    }

    fun apply() {
        state.typeRows = typeTable.typeRows
    }

    fun reset() {
        typeTable.reset(state.typeRows)
    }
}
