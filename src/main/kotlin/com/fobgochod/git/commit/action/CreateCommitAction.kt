package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.CommitMessage
import com.fobgochod.git.commit.domain.option.ViewMode
import com.fobgochod.git.commit.settings.GitSettings
import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.util.GitIcons
import com.fobgochod.git.commit.view.CommitPanelDialog
import com.fobgochod.git.commit.view.CommitPanelPopup
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable

class CreateCommitAction : AnAction(), DumbAware {

    private val state: GitSettings = GitSettings.instance

    init {
        templatePresentation.text = GitBundle.message("action.toolbar.create.commit.message.text")
        templatePresentation.icon = GitIcons.COMMIT_MESSAGE_ACTION
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val commitPanel: CommitMessageI = getCommitPanel(event) ?: return
        val commitMessage: CommitMessage = parseCommitMessage(commitPanel)

        when (state.viewMode) {
            ViewMode.Float -> CommitPanelPopup(project, commitPanel, commitMessage).show()
            ViewMode.Window -> CommitPanelDialog(project, commitPanel, commitMessage).show()
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    private fun parseCommitMessage(commitPanel: CommitMessageI): CommitMessage {
        if (commitPanel is CheckinProjectPanel) {
            return CommitMessage.parse(commitPanel.commitMessage)
        }
        return CommitMessage()
    }

    private fun getCommitPanel(event: AnActionEvent): CommitMessageI? {
        val data: Refreshable? = Refreshable.PANEL_KEY.getData(event.dataContext)
        if (data is CommitMessageI) {
            return data
        }
        return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(event.dataContext)
    }
}
