package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.domain.CommitMessage
import com.fobgochod.git.commit.domain.option.ViewMode
import com.fobgochod.git.commit.settings.GitSettings
import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.util.GitIcons
import com.fobgochod.git.commit.view.CommitDialog
import com.fobgochod.git.commit.view.CommitPanel
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.JBPopupListener
import com.intellij.openapi.ui.popup.LightweightWindowEvent
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable

class CreateCommitAction : AnAction(), DumbAware {

    private val state: GitSettings = GitSettings.getInstance()

    init {
        templatePresentation.text = GitBundle.message("action.toolbar.create.commit.message.text")
        templatePresentation.icon = GitIcons.COMMIT_MESSAGE_ACTION
    }

    override fun actionPerformed(event: AnActionEvent) {
        val commitPanel: CommitMessageI = getCommitPanel(event) ?: return
        val commitMessage: CommitMessage = parseCommitMessage(commitPanel)


        when (state.viewMode) {
            ViewMode.Float -> openFloat(event, commitPanel, commitMessage)
            ViewMode.Window -> openWindow(event, commitPanel, commitMessage)
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    private fun openWindow(event: AnActionEvent, commitPanel: CommitMessageI, commitMessage: CommitMessage) {
        val dialog = CommitDialog(event.project, commitMessage)
        dialog.show()

        if (dialog.exitCode == DialogWrapper.OK_EXIT_CODE) {
            commitPanel.setCommitMessage(dialog.getCommitMessage())
        }
    }

    private fun openFloat(event: AnActionEvent, commitPanel: CommitMessageI, commitMessage: CommitMessage) {
        val project = event.project ?: return

        val panel = CommitPanel(project, commitMessage)
        JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel.createPanel(), panel.focusComponent())
                .setProject(event.project)
                .setTitle(GitBundle.message("action.toolbar.create.commit.message.text"))
                .setResizable(true)
                .setMovable(true)
                .setFocusable(true)
                .setRequestFocus(true)
                .setShowShadow(true)
                .setCancelOnClickOutside(true)
                .addListener(object : JBPopupListener {
                    override fun onClosed(event: LightweightWindowEvent) {
                        commitPanel.setCommitMessage(panel.getCommitMessage().toString())
                    }
                })
                .createPopup()
                .showCenteredInCurrentWindow(project)
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
