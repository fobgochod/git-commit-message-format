package com.fobgochod.git.commit

import com.fobgochod.git.commit.view.CommitPanel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent

class CommitDialog(project: Project?, commitMessage: CommitMessage?) : DialogWrapper(project) {

    var panel: CommitPanel

    override fun createCenterPanel(): JComponent? {
        return panel.getMainPanel();
    }


    fun getCommitMessage(): CommitMessage {
        return panel.getCommitMessage();
    }

    init {
        panel = CommitPanel(project, commitMessage)
        setTitle("Commit")
        setOKButtonText("OK")
        init()
    }

}
