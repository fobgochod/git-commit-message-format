package com.fobgochod.git.commit

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.ui.Refreshable

class CreateCommitAction : AnAction(), DumbAware {

    override fun actionPerformed(event: AnActionEvent) {
        val commitPanel: CommitMessageI = getCommitPanel(event) ?: return;
        val commitMessage: CommitMessage? = parseExistingCommitMessage(commitPanel);
        val dialog = CommitDialog(event.project, commitMessage);
        dialog.show();

        if (dialog.exitCode == DialogWrapper.OK_EXIT_CODE) {
            commitPanel.setCommitMessage(dialog.getCommitMessage().toString());
        }
    }

    private fun parseExistingCommitMessage(commitPanel: CommitMessageI): CommitMessage? {
        if (commitPanel is CheckinProjectPanel) {
            val commitMessageString: String = commitPanel.commitMessage;
            return CommitMessage.parse(commitMessageString);
        }
        return null;
    }

    private fun getCommitPanel(event: AnActionEvent): CommitMessageI? {
        val data: Refreshable? = Refreshable.PANEL_KEY.getData(event.dataContext);
        if (data is CommitMessageI) {
            return data;
        }
        return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(event.dataContext);
    }
}
