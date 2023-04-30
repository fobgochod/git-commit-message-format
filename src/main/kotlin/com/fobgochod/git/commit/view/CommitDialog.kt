package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.domain.CommitMessage
import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.JBUI
import javax.swing.JComponent

class CommitDialog(project: Project?, commitMessage: CommitMessage) : DialogWrapper(project) {

    private var panel: CommitPanel

    init {
        panel = CommitPanel(project, commitMessage)
        title = GitBundle.message("action.toolbar.create.commit.message.text")
        init()

        // val rootPane = peer.rootPane
        // if (rootPane != null) {
        //     rootPane.minimumSize = JBUI.size(600, 420)
        // }
    }

    override fun createCenterPanel(): JComponent {
        return panel
    }

    fun getCommitMessage(): String {
        return panel.getCommitMessage().toString()
    }
}
