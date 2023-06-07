package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.action.ResetTypeAction
import com.fobgochod.git.commit.action.RestoreTypesAction
import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.domain.option.ComponentType
import com.fobgochod.git.commit.domain.option.SkipCI
import com.fobgochod.git.commit.domain.option.ViewMode
import com.fobgochod.git.commit.util.GitBundle
import com.intellij.application.options.editor.CheckboxDescriptor
import com.intellij.application.options.editor.checkBox
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import javax.swing.DefaultComboBoxModel


/**
 * Git Settings Configurable
 *
 * See [Settings](https://plugins.jetbrains.com/docs/intellij/settings.html)
 *
 * See [Kotlin UI DSL Version 2](https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html)
 *
 * See [Sample usages in IntelliJ Platform IDEs](https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl.html#examples)
 *
 * @author fobgochod
 * @since 2023/5/24 23:43
 */
internal class GitSettingsConfigurable : BoundSearchableConfigurable(
    GitBundle.message("configurable.display.name"),
    "git.commit.message"
) {
    private val state: GitSettings get() = GitSettings.instance

    private val hideTypeGroup get() = CheckboxDescriptor(ComponentType.TypeGroup.name, state::hideTypeGroup)
    private val hideType get() = CheckboxDescriptor(ComponentType.Type.name, state::hideType)
    private val hideScope get() = CheckboxDescriptor(ComponentType.Scope.name, state::hideScope)
    private val hideSubject get() = CheckboxDescriptor(ComponentType.Subject.name, state::hideSubject)
    private val hideBody get() = CheckboxDescriptor(ComponentType.Body.name, state::hideBody)
    private val hideWrapText get() = CheckboxDescriptor(ComponentType.WrapText.name, state::hideWrapText)
    private val hideBreaking get() = CheckboxDescriptor(ComponentType.Breaking.name, state::hideBreaking)
    private val hideIssues get() = CheckboxDescriptor(ComponentType.Issues.name, state::hideIssues)
    private val hideSkipCI get() = CheckboxDescriptor(ComponentType.SkipCI.name, state::hideSkipCI)

    override fun createPanel(): DialogPanel {
        return panel {
            row {
                val typeTable = TypeTable()
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
                ).horizontalAlign(HorizontalAlign.FILL)
                    .verticalAlign(VerticalAlign.FILL)
                    .onIsModified { typeTable.typeRows != state.typeRows }
                    .onApply { state.typeRows = typeTable.typeRows }
                    .onReset { typeTable.reset(state.typeRows) }
            }.resizableRow()

            group(GitBundle.message("settings.group.hidden.options")) {
                row {
                    checkBox(hideTypeGroup).applyToComponent {
                        this.toolTipText = ComponentType.TypeGroup.description()
                    }.gap(RightGap.SMALL)

                    checkBox(hideType).applyToComponent {
                        this.toolTipText = ComponentType.Type.description()
                    }.gap(RightGap.SMALL)

                    checkBox(hideScope).applyToComponent {
                        this.toolTipText = ComponentType.Scope.description()
                    }.gap(RightGap.SMALL)

                    checkBox(hideSubject).applyToComponent {
                        this.isEnabled = false
                        this.toolTipText = ComponentType.Subject.description()
                    }.gap(RightGap.SMALL)

                    checkBox(hideBody).applyToComponent {
                        this.toolTipText = ComponentType.Body.description()
                    }.gap(RightGap.SMALL)

                    checkBox(hideWrapText).applyToComponent {
                        this.toolTipText = ComponentType.WrapText.description()
                    }.gap(RightGap.SMALL)

                    checkBox(hideBreaking).applyToComponent {
                        this.toolTipText = ComponentType.Breaking.description()
                    }.gap(RightGap.SMALL)

                    checkBox(hideIssues).applyToComponent {
                        this.toolTipText = ComponentType.Issues.description()
                    }.gap(RightGap.SMALL)

                    checkBox(hideSkipCI).applyToComponent {
                        this.toolTipText = ComponentType.SkipCI.description()
                    }
                }.layout(RowLayout.PARENT_GRID)
            }

            group(GitBundle.message("settings.group.common.settings")) {
                row {
                    intTextField()
                        .label(GitBundle.message("settings.common.type.count"))
                        .bindIntText(state::typeCount).columns(5)
                        .gap(RightGap.COLUMNS)

                    comboBox<SkipCI>(
                        DefaultComboBoxModel(SkipCI.values()),
                        renderer = SimpleListCellRenderer.create("") { it.label })
                        .label(GitBundle.message("settings.common.skip.ci.word"))
                        .bindItem(state::skipCI.toNullableProperty())
                        .gap(RightGap.COLUMNS)

                    comboBox<ViewMode>(
                        DefaultComboBoxModel(ViewMode.values()),
                        renderer = SimpleListCellRenderer.create("") { it.name })
                        .label(GitBundle.message("settings.common.view.mode"))
                        .bindItem(state::viewMode.toNullableProperty())
                }
            }.layout(RowLayout.INDEPENDENT)
        }
    }
}
