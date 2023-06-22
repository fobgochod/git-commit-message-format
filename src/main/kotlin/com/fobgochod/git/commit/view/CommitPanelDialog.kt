package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.domain.CommitMessage
import com.fobgochod.git.commit.util.GitBundle.message
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.CommitMessageI
import javax.swing.JComponent

class CommitPanelDialog(project: Project, private val commitPanel: CommitMessageI, commitMessage: CommitMessage) :
    DialogWrapper(project) {

    private var panel: CommitPanel

    init {
        title = message("action.toolbar.create.commit.message.text")
        panel = CommitPanel(project, commitMessage)
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel.root
    }

    override fun doOKAction() {
        try {
            commitPanel.setCommitMessage(panel.getCommitMessage())
        } finally {
            super.doOKAction()
        }
    }
}
