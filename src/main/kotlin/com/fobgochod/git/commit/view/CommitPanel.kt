package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.constant.GitConstant
import com.fobgochod.git.commit.domain.CommitMessage
import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.domain.option.ViewMode
import com.fobgochod.git.commit.settings.GitSettings
import com.fobgochod.git.commit.util.GitBundle.message
import com.fobgochod.git.commit.util.GitUtil
import com.intellij.icons.AllIcons
import com.intellij.ide.IdeBundle
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.setEmptyState
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import com.intellij.util.ui.JBUI
import java.awt.Container
import java.awt.FocusTraversalPolicy
import java.awt.KeyboardFocusManager
import java.awt.event.ItemListener
import java.util.LinkedList
import javax.swing.AbstractButton
import javax.swing.ButtonGroup
import javax.swing.LayoutFocusTraversalPolicy
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
class CommitPanel(private val project: Project, private val commitMessage: CommitMessage) {

    private val state: GitSettings = GitSettings.instance
    lateinit var changeSubject: JBTextField

    val root: DialogPanel = panel {
        row(message("dialog.form.label.type")) {
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
                        }.align(AlignX.FILL)
                        .bindItem(commitMessage::changeType.toNullableProperty())
                }.visible(!state.hideType && state.typeRows.size > state.typeCount)
            }
        }.visible(!state.hideTypeGroup || !state.hideType)

        row(message("dialog.form.label.scope")) {
            val scopes: MutableList<String> = LinkedList()
            scopes.add("")
            if (state.scopeEnabled) {
                scopes.addAll(state.scopeRows)
            } else {
                val gitUtil = GitUtil(project).logs()
                scopes.addAll(gitUtil.scopes)
            }
            comboBox(scopes)
                .align(AlignX.FILL)
                .bindItem(commitMessage::changeScope.toNullableProperty())
        }.visible(!state.hideScope)

        row(message("dialog.form.label.subject")) {
            textField()
                .align(AlignX.FILL)
                .applyToComponent {
                    changeSubject = this
                    if (state.viewMode == ViewMode.Float) {
                        setEmptyState("Press Alt + Enter or click outside to close")
                    }
                }
                .bindText(commitMessage::changeSubject)
        }.visible(!state.hideSubject)

        row {
            label(message("dialog.form.label.body"))
                .align(AlignY.TOP)
                .gap(RightGap.SMALL)
            textArea()
                .rows(6)
                .align(Align.FILL)
                .bindText(commitMessage::changeBody)
        }.layout(RowLayout.PARENT_GRID).resizableRow().visible(!state.hideBody)

        row("") {
            checkBox(message("dialog.form.label.wrap.text"))
                .align(AlignX.FILL)
                .bindSelected(commitMessage::wrapText)
        }.visible(!state.hideWrapText)

        row {
            label(message("dialog.form.label.breaking"))
                .align(AlignY.TOP)
                .gap(RightGap.SMALL)
            textArea()
                .rows(3)
                .align(Align.FILL)
                .bindText(commitMessage::breakingChanges)
        }.layout(RowLayout.PARENT_GRID).resizableRow().visible(!state.hideBreaking)

        row(message("dialog.form.label.issues")) {
            textField()
                .align(AlignX.FILL)
                .bindText(commitMessage::closedIssues)
                .applyToComponent {
                    setEmptyState("#124,#245")
                }
        }.visible(!state.hideIssues)

        row("") {
            checkBox(message("dialog.form.label.skip.ci")).bindSelected(commitMessage::skipCI)

            val action = object : DumbAwareAction(
                IdeBundle.message("settings.entry.point.tooltip"), null, AllIcons.General.Settings
            ) {
                override fun actionPerformed(e: AnActionEvent) {
                    ShowSettingsUtil.getInstance()
                        .showSettingsDialog(project, message("configurable.display.name"))
                }
            }
            actionButton(action).align(AlignX.RIGHT)
        }.visible(!state.hideSkipCI)
    }.apply {
        preferredFocusedComponent = changeSubject
        border = JBUI.Borders.empty(8, 8, 0, 8)
        withPreferredWidth(GitConstant.PREFERRED_WIDTH)
    }

    init {
        installFocusTraversalPolicy(root, LayoutFocusTraversalPolicy())
    }

    private fun installFocusTraversalPolicy(container: Container, policy: FocusTraversalPolicy) {
        container.isFocusCycleRoot = true
        container.isFocusTraversalPolicyProvider = true
        container.focusTraversalPolicy = policy
        resetDefaultFocusTraversalKeys(container)
    }

    private fun resetDefaultFocusTraversalKeys(container: Container) {
        val focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager()
        listOf(
            KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
            KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
            KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS,
            KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS
        ).forEach { each ->
            container.setFocusTraversalKeys(each, focusManager.getDefaultFocusTraversalKeys(each))
        }
    }

    fun getCommitMessage(): String {
        root.apply()
        return commitMessage.toString()
    }
}
