package com.fobgochod.git.commit.view

import com.fobgochod.git.GitBundle
import com.fobgochod.git.commit.CommitMessage
import com.fobgochod.git.commit.GitLogQuery
import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.settings.GitCommitHelperState
import com.intellij.icons.AllIcons
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ItemEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.util.function.Consumer
import javax.swing.*

class CommitWindow(val project: Project?, private val oldCommitMessage: CommitMessage) {

    private var state: GitCommitHelperState = GitCommitHelperState.getInstance()

    private val formBuilder = FormBuilder.createFormBuilder();

    private val changeTypePanel = JPanel(GridLayout(state.count + 1, 1))
    private val changeTypeGroup = ButtonGroup();
    private val changeType: JComboBox<TypeRow> = ComboBox()

    private val changeScopePanel = JPanel(BorderLayout())
    private val changeScope: JComboBox<String> = ComboBox()

    private val changeSubject = JTextField()
    private val changeBody = JTextArea(6, 0)
    private val wrapText = JCheckBox("Wrap text at 72 characters?", true)
    private val breakingChanges = JTextArea(3, 0)
    private val closedIssues = JTextField()

    private val bottomPanel: JPanel = JPanel(BorderLayout())
    private val skipCI = JCheckBox("Skip CI?")
    private val editSettings = JLabel(AllIcons.General.Settings)

    init {
        initView()
        initEvent()
        initData()
    }

    val root: JPanel get() = formBuilder.panel

    val commitMessage: CommitMessage
        get() = CommitMessage(
            selectedChangeType,
            selectedChangeScope,
            changeSubject.text.trim { it <= ' ' },
            changeBody.text.trim { it <= ' ' },
            wrapText.isSelected,
            breakingChanges.text.trim { it <= ' ' },
            closedIssues.text.trim { it <= ' ' },
            skipCI.isSelected
        )

    private fun initView() {
        for ((index, type) in state.typeRows.withIndex()) {
            if (index < state.count) {
                val jRadioButton = JRadioButton(type.toString())
                changeTypeGroup.add(jRadioButton)
                changeTypePanel.add(jRadioButton)
            }
        }
        changeTypePanel.add(changeType);

        changeScope.isEditable = true
        changeScopePanel.add(changeScope, BorderLayout.CENTER)

        changeBody.lineWrap = true
        breakingChanges.lineWrap = true

        bottomPanel.add(skipCI, BorderLayout.CENTER)
        bottomPanel.add(editSettings, BorderLayout.EAST)

        formBuilder.panel.minimumSize = Dimension(600, 0)
        formBuilder.addLabeledComponent(JLabel("Type of change"), changeTypePanel)
            .addLabeledComponent(JLabel("Scope of change"), changeScopePanel)
            .addLabeledComponent(JLabel("Subject of change"), changeSubject)
            .addLabeledComponent(JLabel("Body of change"), changeBody)
            .addComponentToRightColumn(wrapText)
            .addLabeledComponent(JLabel("Breaking Changes"), breakingChanges)
            .addLabeledComponent(JLabel("Closed Issues"), closedIssues)
            .addComponentToRightColumn(bottomPanel)
    }

    private fun initEvent() {
        for (element in changeTypeGroup.elements) {
            element.addChangeListener {
                if (element.isSelected) {
                    changeType.selectedItem = GitCommitHelperState.getInstance().getTypeFromName(element.text)
                }
            }
        }

        changeType.addItemListener { e: ItemEvent ->
            val item: TypeRow = e.item as TypeRow
            if (state.typeRows.indexOf(item) > state.count) {
                changeTypeGroup.clearSelection()
            } else {
                for (element in changeTypeGroup.elements) {
                    if (item == GitCommitHelperState.getInstance().getTypeFromName(element.text)) {
                        element.isSelected = true;
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

    private fun initData() {
        // 恢复数据
        for (typeRow in GitCommitHelperState.getInstance().typeRows) {
            changeType.addItem(typeRow)
        }

        val workingDirectory = File(project?.basePath)
        val result = GitLogQuery(workingDirectory).execute()
        if (result.isSuccess()) {
            changeScope.addItem("") // no value by default
            result.getScopes().forEach(Consumer { item: String ->
                changeScope.addItem(
                    item
                )
            })
        }
        restoreValuesFromParsedCommitMessage(oldCommitMessage)
    }


    private val selectedChangeType: String
        get() {
            val selectedItem = changeType.selectedItem
            return (selectedItem as TypeRow).title
        }
    private val selectedChangeScope: String
        get() {
            val selectedItem = changeScope.selectedItem
            return selectedItem as String
        }

    private fun restoreValuesFromParsedCommitMessage(commitMessage: CommitMessage) {
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
