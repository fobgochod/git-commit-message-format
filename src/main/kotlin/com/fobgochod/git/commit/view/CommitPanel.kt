package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.domain.CommitMessage
import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.settings.GitState
import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.util.GitLog
import com.intellij.icons.AllIcons
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.*
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.ItemEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.ButtonGroup
import javax.swing.JPanel
import kotlin.math.min

class CommitPanel(val project: Project?, private val commitMessage: CommitMessage) : JPanel() {

    private var state: GitState = GitState.getInstance()

    private val formBuilder = FormBuilder.createFormBuilder()

    private val changeTypePanel = JPanel()
    private val changeTypeGroup = ButtonGroup()
    private val changeType: ComboBox<TypeRow> = ComboBox()

    private val changeScopePanel = JPanel(BorderLayout())
    private val changeScope: ComboBox<String> = ComboBox()

    private val changeSubject = JBTextField()
    private val changeBody = JBTextArea(6, 0)
    private val wrapText = JBCheckBox("Wrap text at 72 characters?", true)
    private val breakingChanges = JBTextArea(3, 0)
    private val closedIssues = JBTextField()

    private val bottomPanel: JPanel = JPanel(BorderLayout())
    private val skipCI = JBCheckBox("Skip CI?")
    private val editSettings = JBLabel(AllIcons.General.Settings)

    init {
        layout = BorderLayout()
        add(formBuilder.panel, BorderLayout.CENTER)
        initView()
        initEvent()
        initData()
    }

    private fun initView() {
        val minCount = min(state.typeCount, state.typeRows.size)
        changeTypePanel.layout = GridLayout(minCount + 1, 1)
        for ((index, type) in state.typeRows.withIndex()) {
            if (index < state.typeCount) {
                val radioButton = JBRadioButton(type.toString())
                radioButton.toolTipText = type.toString()
                changeTypeGroup.add(radioButton)
                changeTypePanel.add(radioButton)
            }
        }
        changeTypePanel.add(changeType)

        changeScope.isEditable = true
        changeScopePanel.add(changeScope, BorderLayout.CENTER)

        changeBody.lineWrap = true
        breakingChanges.lineWrap = true

        bottomPanel.add(skipCI, BorderLayout.CENTER)
        bottomPanel.add(editSettings, BorderLayout.EAST)

        formBuilder.addLabeledComponent(GitBundle.message("dialog.form.label.type"), changeTypePanel)
            .addLabeledComponent(GitBundle.message("dialog.form.label.scope"), changeScopePanel)
            .addLabeledComponent(GitBundle.message("dialog.form.label.subject"), changeSubject)
            .addLabeledComponent(GitBundle.message("dialog.form.label.body"), changeBody, 10, false)
            .addComponentToRightColumn(wrapText)
            .addLabeledComponent(GitBundle.message("dialog.form.label.breaking"), breakingChanges)
            .addLabeledComponent(GitBundle.message("dialog.form.label.issues"), closedIssues)
            .addComponentToRightColumn(bottomPanel)
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

        editSettings.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, GitBundle.message("plugin.name"))
            }
        })
    }

    // 恢复数据
    private fun initData() {
        for (typeRow in state.typeRows) {
            changeType.addItem(typeRow)
        }

        val result = GitLog(project).execute()
        if (result.isSuccess()) {
            changeScope.addItem("") // no value by default
            result.getScopes().forEach(changeScope::addItem)
        }

        restoreFromParsedCommitMessage(commitMessage)
    }

    fun getCommitMessage(): CommitMessage {
        return CommitMessage(
            getChangeType(),
            getChangeScope(),
            changeSubject.text.trim { it <= ' ' },
            changeBody.text.trim { it <= ' ' },
            wrapText.isSelected,
            breakingChanges.text.trim { it <= ' ' },
            closedIssues.text.trim { it <= ' ' },
            skipCI.isSelected
        )
    }

    private fun getChangeType(): String {
        val selectedItem = changeType.selectedItem
        return (selectedItem as TypeRow).name
    }

    private fun getChangeScope(): String {
        val selectedItem = changeScope.selectedItem
        return selectedItem as String
    }

    private fun restoreFromParsedCommitMessage(commitMessage: CommitMessage) {
        changeType.selectedItem = commitMessage.changeType
        changeScope.selectedItem = commitMessage.changeScope
        changeSubject.text = commitMessage.changeSubject
        changeBody.text = commitMessage.changeBody
        wrapText.isSelected = commitMessage.wrapText
        breakingChanges.text = commitMessage.breakingChanges
        closedIssues.text = commitMessage.closedIssues
        skipCI.isSelected = commitMessage.skipCI
    }
}
