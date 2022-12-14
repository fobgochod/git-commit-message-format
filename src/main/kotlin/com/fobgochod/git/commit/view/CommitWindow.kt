package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.GitLogQuery
import com.fobgochod.git.commit.domain.ChangeType
import com.fobgochod.git.commit.CommitMessage
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.util.ui.FormBuilder
import java.awt.Dimension
import java.io.File
import java.util.function.Consumer
import javax.swing.*

class CommitWindow(project: Project?, commitMessage: CommitMessage?) {
    val mainPanel: JPanel
    private val changeType: JComboBox<ChangeType>
    private val changeScope: JComboBox<String>
    private val shortDescription = JTextField()
    private val longDescription = JTextArea()
    private val wrapTextCheckBox = JCheckBox("Wrap text at 72 characters?", true)
    private val breakingChanges = JTextArea()
    private val closedIssues = JTextField()
    private val skipCICheckBox = JCheckBox("Skip CI?")

    init {
        changeType = ComboBox(DefaultComboBoxModel(ChangeType.values()))
        changeScope = ComboBox()
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
        commitMessage?.let { restoreValuesFromParsedCommitMessage(it) }
        longDescription.preferredSize = Dimension(150, 100)
        longDescription.lineWrap = true
        breakingChanges.preferredSize = Dimension(150, 50)
        breakingChanges.lineWrap = true
        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JLabel("Type of change"), changeType)
            .addLabeledComponent(JLabel("Scope of this change"), changeScope)
            .addLabeledComponent(JLabel("Short description"), shortDescription)
            .addLabeledComponent(JLabel("Long description"), longDescription)
            .addComponentToRightColumn(wrapTextCheckBox)
            .addLabeledComponent(JLabel("Breaking changes"), breakingChanges)
            .addLabeledComponent(JLabel("Closed issues"), closedIssues)
            .addComponentToRightColumn(skipCICheckBox)
            .panel
    }

    val commitMessage: CommitMessage
        get() = CommitMessage(
            selectedChangeType,
            selectedChangeScope,
            shortDescription.text.trim { it <= ' ' },
            longDescription.text.trim { it <= ' ' },
            breakingChanges.text.trim { it <= ' ' },
            closedIssues.text.trim { it <= ' ' },
            wrapTextCheckBox.isSelected,
            skipCICheckBox.isSelected
        )
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
        shortDescription.text = commitMessage.shortDescription
        longDescription.text = commitMessage.longDescription
        breakingChanges.text = commitMessage.breakingChanges
        closedIssues.text = commitMessage.closedIssues
        skipCICheckBox.isSelected = commitMessage.isSkipCI()
    }
}
