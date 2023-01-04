package com.fobgochod.git.commit.view

import com.fobgochod.git.GitBundle
import com.fobgochod.git.commit.CommitMessage
import com.fobgochod.git.commit.GitLogQuery
import com.fobgochod.git.commit.domain.ChangeType
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
import java.util.*
import java.util.function.Consumer
import javax.swing.*

class CommitWindow(val project: Project?, private val oldCommitMessage: CommitMessage?) {

    private val changeTypeGroup = ButtonGroup();
    private val changeType: JComboBox<ChangeType> = ComboBox(DefaultComboBoxModel(ChangeType.values()))
    private val changeScope: JComboBox<String> = ComboBox()
    private val changeSubject = JTextField()
    private val changeBody = JTextArea()
    private val wrapText = JCheckBox("Wrap text at 72 characters?", true)
    private val breakingChanges = JTextArea()
    private val closedIssues = JTextField()
    private val skipCI = JCheckBox("Skip CI?")

    private val editSettings = JLabel(AllIcons.General.Settings)

    private val formBuilder = FormBuilder.createFormBuilder();
    private val changeTypePanel = JPanel(GridLayout(MAX_USE_COUNT + 1, 1))
    private val bottomPanel: JPanel = JPanel(BorderLayout())

    companion object {
        const val MAX_USE_COUNT = 3;
    }

    init {
        initView()
        initEvent()
        initData()
    }

    val root: JPanel get() = formBuilder.panel

    val commitMessage: CommitMessage
        get() = CommitMessage(
            selectedChangeType.title,
            selectedChangeScope,
            changeSubject.text.trim { it <= ' ' },
            changeBody.text.trim { it <= ' ' },
            breakingChanges.text.trim { it <= ' ' },
            closedIssues.text.trim { it <= ' ' },
            wrapText.isSelected,
            skipCI.isSelected
        )

    private fun initView() {
        for ((index, type) in ChangeType.values().withIndex()) {
            if (index < MAX_USE_COUNT) {
                val jRadioButton = JRadioButton(type.toString())
                changeTypeGroup.add(jRadioButton)
                changeTypePanel.add(jRadioButton)
            }
        }
        changeTypePanel.add(changeType);

        changeBody.preferredSize = Dimension(150, 100)
        changeBody.lineWrap = true
        breakingChanges.preferredSize = Dimension(150, 50)
        breakingChanges.lineWrap = true

        bottomPanel.add(skipCI, BorderLayout.CENTER)
        bottomPanel.add(editSettings, BorderLayout.EAST)

        formBuilder.addLabeledComponent(JLabel("Type of change"), changeTypePanel)
            .addLabeledComponent(JLabel("Scope of change"), changeScope)
            .addLabeledComponent(JLabel("Subject of change"), changeSubject)
            .addLabeledComponent(JLabel("Body of change"), changeBody)
            .addComponentToRightColumn(wrapText)
            .addLabeledComponent(JLabel("Breaking Changes"), breakingChanges)
            .addLabeledComponent(JLabel("Closed Issues"), closedIssues).addComponentToRightColumn(bottomPanel)
    }

    private fun initEvent() {
        for (element in changeTypeGroup.elements) {
            element.addChangeListener {
                if (element.isSelected) {
                    changeType.selectedItem = getTypeFromJRadioButton(element)
                }
            }
        }

        changeType.addItemListener { e: ItemEvent ->
            val item: ChangeType = e.item as ChangeType
            if (item.ordinal > MAX_USE_COUNT) {
                changeTypeGroup.clearSelection()
            } else {
                for (element in changeTypeGroup.elements) {
                    if (item == getTypeFromJRadioButton(element)) {
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
        oldCommitMessage?.let { restoreValuesFromParsedCommitMessage(it) }
    }


    private val selectedChangeType: ChangeType
        get() {
            val selectedItem = changeType.selectedItem
            return selectedItem as ChangeType
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
        breakingChanges.text = commitMessage.breakingChanges
        closedIssues.text = commitMessage.closedIssues
        skipCI.isSelected = commitMessage.skipCI
    }

    private fun getTypeFromJRadioButton(button: AbstractButton): ChangeType {
        val text: String = button.text.split("-".toRegex())[0].trim { it <= ' ' }
        return ChangeType.valueOf(text.uppercase(Locale.getDefault()))
    }
}
