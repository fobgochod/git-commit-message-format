package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.constant.GitConstant
import com.fobgochod.git.commit.domain.CommitMessage
import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.settings.GitSettings
import com.fobgochod.git.commit.settings.GitSettingsDialog
import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.util.GitUtil
import com.intellij.icons.AllIcons
import com.intellij.ide.IdeBundle
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.setEmptyState
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.dsl.gridLayout.VerticalAlign
import com.intellij.util.ui.JBEmptyBorder
import java.awt.event.ItemListener
import javax.swing.AbstractButton
import javax.swing.ButtonGroup
import javax.swing.JComponent
import javax.swing.event.ChangeListener

/**
 * Git Commit Format panel
 *
 * See [Kotlin UI DSL Version 2](https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html)
 *
 * @author fobgochod
 * @date 2023/5/30 22:21
 * @see com.intellij.internal.ui.uiDslShowcase.UiDslShowcaseAction
 */
class CommitPanel(val project: Project?, private val commitMessage: CommitMessage) {

    private val state: GitSettings = GitSettings.getInstance()
    private lateinit var changeSubject: JBTextField

    private val root: DialogPanel = panel {
        row(GitBundle.message("dialog.form.label.type")) {
            panel {
                lateinit var comboBox: ComboBox<TypeRow>
                val changeTypeGroup = ButtonGroup()

                panel {
                    buttonsGroup {
                        state.typeRows.forEachIndexed { index, type ->
                            row {
                                val lister = ChangeListener {
                                    val button = it.source as AbstractButton
                                    if (button.isSelected) {
                                        comboBox.selectedItem = type
                                    }
                                }

                                radioButton(type.toString(), type)
                                        .applyToComponent {
                                            changeTypeGroup.add(this)
                                            addChangeListener(lister)
                                        }
                            }.visible(index < state.typeCount)
                        }
                    }.bind(commitMessage::changeType)
                }.visible(!state.hideTypeGroup)

                row {
                    val lister = ItemListener {
                        val item: TypeRow = it?.item as TypeRow
                        for (element in changeTypeGroup.elements) {
                            if (item.toString() == element.text) {
                                element.isSelected = true
                            }
                        }
                    }

                    comboBox(state.typeRows)
                            .applyToComponent {
                                comboBox = this
                                addItemListener(lister)
                            }
                            .horizontalAlign(HorizontalAlign.FILL)
                            .bindItem(commitMessage::changeType.toNullableProperty())
                }.visible(!state.hideType && state.typeRows.size > state.typeCount)
            }
        }

        row(GitBundle.message("dialog.form.label.scope")) {
            val gitUtil = GitUtil(project).logs()

            comboBox(gitUtil.scopes)
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindItem(commitMessage::changeScope.toNullableProperty())
        }.visible(!state.hideScope)

        row(GitBundle.message("dialog.form.label.subject")) {
            textField().horizontalAlign(HorizontalAlign.FILL)
                    .applyToComponent { changeSubject = this }
                    .bindText(commitMessage::changeSubject)
        }.visible(!state.hideSubject)

        row {
            label(GitBundle.message("dialog.form.label.body"))
                    .verticalAlign(VerticalAlign.TOP)
                    .gap(RightGap.SMALL)
            textArea()
                    .rows(6)
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindText(commitMessage::changeBody)
        }.layout(RowLayout.PARENT_GRID).visible(!state.hideBody)

        row(EMPTY_LABEL) {
            checkBox(GitBundle.message("dialog.form.label.wrap.text"))
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindSelected(commitMessage::wrapText)
        }.visible(!state.hideWrapText)

        row {
            label(GitBundle.message("dialog.form.label.breaking"))
                    .verticalAlign(VerticalAlign.TOP)
                    .gap(RightGap.SMALL)
            textArea()
                    .rows(3)
                    .resizableColumn()
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindText(commitMessage::breakingChanges)
        }.layout(RowLayout.PARENT_GRID).visible(!state.hideBreaking)

        row(GitBundle.message("dialog.form.label.issues")) {
            textField()
                    .horizontalAlign(HorizontalAlign.FILL)
                    .bindText(commitMessage::closedIssues)
                    .applyToComponent {
                        setEmptyState("#124,#245")
                    }
        }.visible(!state.hideIssues)

        row(EMPTY_LABEL) {
            checkBox(GitBundle.message("dialog.form.label.skip.ci")).bindSelected(commitMessage::skipCI)

            val action = object : DumbAwareAction(IdeBundle.message("settings.entry.point.tooltip"), null, AllIcons.General.Settings) {
                override fun actionPerformed(e: AnActionEvent) {
                    if (project != null) {
                        GitSettingsDialog.showSettingsDialog(project)
                    }
                }
            }
            actionButton(action).horizontalAlign(HorizontalAlign.RIGHT)
        }.visible(!state.hideSkipCI)
    }.apply {
        withPreferredWidth(GitConstant.PREFERRED_WIDTH)
        border = JBEmptyBorder(10)
    }


    fun createPanel(): JComponent {
        return root
    }

    fun focusComponent(): JComponent {
        return changeSubject
    }

    fun getCommitMessage(): CommitMessage {
        root.apply()
        return commitMessage
    }
}
