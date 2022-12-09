package com.fobgochod.git.commit

import com.fobgochod.git.GitBundle
import com.fobgochod.git.commit.view.CommitPanel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class CommitDialog(project: Project?, commitMessage: CommitMessage?) : DialogWrapper(project) {

    private var panel: CommitPanel

    override fun createCenterPanel(): JComponent? {
        return panel.mainPanel;
    }


    fun getCommitMessage(): CommitMessage {
        return panel.commitMessage;
    }

    init {
        panel = CommitPanel(project, commitMessage)
        title = GitBundle.message("create.commit.message")
        setOKButtonText("OK")
        init()
    }
}
