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

    override fun actionPerformed(e: AnActionEvent) {
        val commitPanel: CommitMessageI? = getCommitPanel(e);
        if (commitPanel == null) return;

        val commitMessage: CommitMessage? = parseExistingCommitMessage(commitPanel);
        val dialog = CommitDialog(e.getProject(), commitMessage);
        dialog.show();

        if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            commitPanel.setCommitMessage(dialog.getCommitMessage().toString());
        }
    }

    fun parseExistingCommitMessage(commitPanel: CommitMessageI): CommitMessage? {
        if (commitPanel is CheckinProjectPanel) {
            val commitMessageString: String = commitPanel.getCommitMessage();
            return CommitMessage.parse(commitMessageString);
        }
        return null;
    }

    fun getCommitPanel(e: AnActionEvent): CommitMessageI? {
        if (e == null) {
            return null;
        }
        val data: Refreshable? = Refreshable.PANEL_KEY.getData(e.getDataContext());
        if (data is CommitMessageI) {
            return data;
        }
        return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.getDataContext());
    }
}
