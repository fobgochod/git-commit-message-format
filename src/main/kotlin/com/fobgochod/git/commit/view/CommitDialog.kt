package com.fobgochod.git.commit.view

import com.fobgochod.git.GitBundle
import com.fobgochod.git.commit.CommitMessage
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class CommitDialog(project: Project?, commitMessage: CommitMessage) : DialogWrapper(project) {

    private var panel: CommitWindow

    override fun createCenterPanel(): JComponent {
        return panel.root;
    }

    fun getCommitMessage(): CommitMessage {
        return panel.commitMessage;
    }

    init {
        panel = CommitWindow(project, commitMessage)
        title = GitBundle.message("create.commit.message")
        init()
    }
}
