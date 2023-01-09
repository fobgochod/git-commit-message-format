package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.domain.CommitMessage
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class CommitDialog(project: Project?, commitMessage: CommitMessage) : DialogWrapper(project) {

    private var panel: CommitPanel

    init {
        panel = CommitPanel(project, commitMessage)
        title = GitBundle.message("action.toolbar.create.commit.message.text")
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel.root;
    }

    fun getCommitMessage(): String {
        return panel.getCommitMessage().toString();
    }
}
