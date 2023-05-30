package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.domain.CommitMessage
import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.domain.option.ComponentType
import com.fobgochod.git.commit.settings.GitSettings
import com.fobgochod.git.commit.settings.GitSettingsDialog
import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.util.GitUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.setEmptyState
import com.intellij.ui.components.*
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.ItemEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.ButtonGroup
import javax.swing.JComponent
import javax.swing.JPanel

class CommitPanel1(val project: Project?, private val commitMessage: CommitMessage) : JPanel() {

    private val state: GitSettings = GitSettings.getInstance()

    private val mainBuilder = FormBuilder.createFormBuilder()

    private val changeTypePanel = JPanel(GridLayout(0, 1))
    private val changeTypeGroup = ButtonGroup()
    private val changeType: ComboBox<TypeRow> = ComboBox()

    private val changeScopePanel = JPanel(BorderLayout())
    private val changeScope: ComboBox<String> = ComboBox()

    private val changeSubject = JBTextField()
    private val changeBody = JBTextArea(6, 0)
    private val wrapText = JBCheckBox(GitBundle.message("dialog.form.label.wrap.text"), true)
    private val breakingChanges = JBTextArea(3, 0)
    private val closedIssues = JBTextField()

    private val bottomPanel: JPanel = JPanel(BorderLayout())
    private val skipCI = JBCheckBox(GitBundle.message("dialog.form.label.skip.ci"))
    private val settings = JBLabel(AllIcons.General.Settings)

    init {
        layout = BorderLayout()
        add(mainBuilder.panel, BorderLayout.CENTER)
        initView()
        initEvent()
        initData()
    }

    fun createPanel(): JComponent {
        return this
    }

    fun focusComponent(): JComponent {
        return changeSubject
    }

    private fun initView() {
        if (state.isComponentHidden(ComponentType.TypeGroup)) {
            changeTypePanel.add(changeType)
        } else {
            for ((index, type) in state.typeRows.withIndex()) {
                if (index < state.typeCount) {
                    val radioButton = JBRadioButton(type.toString())
                    radioButton.toolTipText = type.toString()
                    changeTypeGroup.add(radioButton)
                    changeTypePanel.add(radioButton)
                }
            }
            if (state.typeCount < state.typeRows.size) {
                if (state.isComponentShow(ComponentType.Type)) {
                    changeTypePanel.add(changeType)
                }
            } else {
                changeTypePanel.layout = GridLayout(0, 1, 0, 5)
            }
        }

        changeScope.isEditable = true
        changeScopePanel.add(changeScope, BorderLayout.CENTER)

        changeBody.lineWrap = true
        breakingChanges.lineWrap = true

        closedIssues.setEmptyState("#124,#245")

        bottomPanel.add(skipCI, BorderLayout.CENTER)
        bottomPanel.add(settings, BorderLayout.EAST)

        mainBuilder.addLabeledComponent(GitBundle.message("dialog.form.label.type"), changeTypePanel)
        if (state.isComponentShow(ComponentType.Scope)) {
            mainBuilder.addLabeledComponent(GitBundle.message("dialog.form.label.scope"), changeScopePanel)
        }
        mainBuilder.addLabeledComponent(GitBundle.message("dialog.form.label.subject"), changeSubject)
        if (state.isComponentShow(ComponentType.Body)) {
            mainBuilder.addLabeledComponent(GitBundle.message("dialog.form.label.body"), changeBody)
        }
        if (state.isComponentShow(ComponentType.WrapText)) {
            mainBuilder.addComponentToRightColumn(wrapText)
        }
        if (state.isComponentShow(ComponentType.Breaking)) {
            mainBuilder.addLabeledComponent(GitBundle.message("dialog.form.label.breaking"), breakingChanges)
        }
        if (state.isComponentShow(ComponentType.Issues)) {
            mainBuilder.addLabeledComponent(GitBundle.message("dialog.form.label.issues"), closedIssues)
        }
        if (state.isComponentShow(ComponentType.SkipCI)) {
            mainBuilder.addComponentToRightColumn(bottomPanel)
        }
    }

    private fun initEvent() {
        for (element in changeTypeGroup.elements) {
            element.addChangeListener {
                if (element.isSelected) {
                    changeType.selectedItem = state.getTypeFromName(element.text)
                }
            }
        }

        changeType.addItemListener { e: ItemEvent ->
            val item: TypeRow = e.item as TypeRow
            if (state.typeRows.indexOf(item) > state.typeCount) {
                changeTypeGroup.clearSelection()
            } else {
                for (element in changeTypeGroup.elements) {
                    if (item == state.getTypeFromName(element.text)) {
                        element.isSelected = true
                    }
                }
            }
        }

        settings.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (project != null) {
                    GitSettingsDialog.showSettingsDialog(project)
                }
            }
        })
    }

    // 恢复数据
    private fun initData() {
        for (typeRow in state.typeRows) {
            changeType.addItem(typeRow)
        }

        if (state.isComponentShow(ComponentType.Scope)) {
            val gitUtil = GitUtil(project).logs()
            gitUtil.scopes.forEach(changeScope::addItem)
        }

        restoreCommitMessage(commitMessage)
    }

    fun getCommitMessage(): CommitMessage {
        return CommitMessage(
                getChangeTypeName(),
                getChangeScope(),
                changeSubject.text.trim { it <= ' ' },
                changeBody.text.trim { it <= ' ' },
                wrapText.isSelected,
                breakingChanges.text.trim { it <= ' ' },
                closedIssues.text.trim { it <= ' ' },
                skipCI.isSelected
        )
    }

    private fun getChangeTypeName(): TypeRow {
        val selectedItem = changeType.selectedItem
        return selectedItem as TypeRow
    }

    private fun getChangeScope(): String {
        val selectedItem = changeScope.selectedItem
        return selectedItem as String
    }

    private fun restoreCommitMessage(commitMessage: CommitMessage) {
        changeType.selectedItem = commitMessage.changeType
        changeScope.selectedItem = commitMessage.changeScope
        changeSubject.text = commitMessage.changeSubject
        changeBody.text = commitMessage.changeBody
        wrapText.isSelected = commitMessage.wrapText
        breakingChanges.text = commitMessage.breakingChanges
        closedIssues.text = commitMessage.closedIssues
        skipCI.isSelected = commitMessage.skipCI
    }

    private fun getChangeTypeByName(name: String): TypeRow {
        for (typeRow in state.typeRows) {
            if (name == typeRow.name) {
                return typeRow
            }
        }
        return state.typeRows[0]
    }
}