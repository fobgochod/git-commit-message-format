package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.domain.option.ComponentType
import com.fobgochod.git.commit.domain.option.SkipCI
import com.fobgochod.git.commit.domain.option.ViewMode
import com.fobgochod.git.commit.settings.scope.ScopeListTable
import com.fobgochod.git.commit.settings.type.TypeToolbarDecorator
import com.fobgochod.git.commit.util.GitBundle.message
import com.intellij.application.options.editor.CheckboxDescriptor
import com.intellij.application.options.editor.checkBox
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.*
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
 * @date 2023/5/24 23:43
 */
internal class GitSettingsConfigurable : BoundSearchableConfigurable(
    message("configurable.display.name"),
    "git.commit.message"
) {
    private val state: GitSettings get() = GitSettings.instance

    // @formatter:off
    private val hideTypeGroup get() = CheckboxDescriptor(ComponentType.TypeGroup.name, state::hideTypeGroup)
    private val hideType get() = CheckboxDescriptor(ComponentType.Type.name, state::hideType)
    private val hideScope get() = CheckboxDescriptor(ComponentType.Scope.name, state::hideScope)
    private val hideSubject get() = CheckboxDescriptor(ComponentType.Subject.name, state::hideSubject)
    private val hideBody get() = CheckboxDescriptor(ComponentType.Body.name, state::hideBody)
    private val hideWrapText get() = CheckboxDescriptor(ComponentType.WrapText.name, state::hideWrapText)
    private val hideBreaking get() = CheckboxDescriptor(ComponentType.Breaking.name, state::hideBreaking)
    private val hideIssues get() = CheckboxDescriptor(ComponentType.Issues.name, state::hideIssues)
    private val hideSkipCI get() = CheckboxDescriptor(ComponentType.SkipCI.name, state::hideSkipCI)
    private val scopeEnabled get() = CheckboxDescriptor(message("settings.scope.enabled"), state::scopeEnabled)
    private val wrapTextEnabled get() = CheckboxDescriptor(message("dialog.form.label.wrap.text"), state::wrapTextEnabled)
    // @formatter:on

    override fun createPanel(): DialogPanel {
        return panel {
            row {
                val typeTable = TypeToolbarDecorator()
                cell(typeTable.decorator.createPanel())
                    .align(Align.FILL)
                    .resizableColumn()
                    .onIsModified { typeTable.isModified() }
                    .onApply { typeTable.apply() }
                    .onReset { typeTable.reset() }

                panel {
                    lateinit var scopeCheckBox: Cell<JBCheckBox>
                    row {
                        scopeCheckBox = checkBox(scopeEnabled)
                    }
                    row {
                        val scopeTable = ScopeListTable()
                        cell(scopeTable.component)
                            .align(Align.FILL)
                            .onIsModified { scopeTable.isModified() }
                            .onApply { scopeTable.apply() }
                            .onReset { scopeTable.reset() }
                    }.resizableRow().enabledIf(scopeCheckBox.selected)
                }.align(Align.FILL)
            }.resizableRow().layout(RowLayout.INDEPENDENT)

            group(message("settings.group.hidden.options")) {
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
                }
            }

            group(message("settings.group.common.settings")) {
                row {
                    panel {
                        row(message("settings.common.view.mode")) {
                            comboBox(ViewMode.entries)
                                .bindItem(state::viewMode.toNullableProperty())
                        }

                        row(message("settings.common.skip.ci.word")) {
                            comboBox<SkipCI>(
                                // EnumComboBoxModel(SkipCI::class.java),
                                DefaultComboBoxModel(SkipCI.entries.toTypedArray()),
                                SimpleListCellRenderer.create("", SkipCI::label)
                            ).bindItem(state::skipCI.toNullableProperty())
                        }
                    }.gap(RightGap.COLUMNS).align(AlignY.TOP).resizableColumn()

                    panel {
                        row {
                            checkBox(wrapTextEnabled)
                        }
                    }.align(AlignY.TOP).resizableColumn()
                }
            }
        }
    }
}
