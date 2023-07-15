package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.domain.CommitMessage
import com.fobgochod.git.commit.util.GitBundle.message
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.JBPopupListener
import com.intellij.openapi.ui.popup.LightweightWindowEvent
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.VcsApplicationSettings
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class CommitPanelPopup(
    private val project: Project,
    private val commitPanel: CommitMessageI,
    commitMessage: CommitMessage
) {

    private var popup: JBPopup

    init {
        val panel = CommitPanel(project, commitMessage)

        val listener = object : JBPopupListener {
            override fun onClosed(event: LightweightWindowEvent) {
                commitPanel.setCommitMessage(panel.getCommitMessage())
            }
        }

        popup = JBPopupFactory.getInstance()
            .createComponentPopupBuilder(panel.root, panel.changeSubject)
            .setProject(project)
            .setTitle(message("action.toolbar.create.commit.message.text"))
            .setResizable(true)
            .setMovable(true)
            .setFocusable(true)
            .setRequestFocus(true)
            .setShowShadow(true)
            .setCancelOnClickOutside(true)
            .addListener(listener)
            .createPopup()
            .also { popup ->
                panel.changeSubject.addKeyListener(object : KeyAdapter() {
                    override fun keyPressed(e: KeyEvent?) {
                        if (e?.keyCode == KeyEvent.VK_ENTER && e.isAltDown) {
                            popup.closeOk(null)
                        }
                    }
                })
            }
    }

    fun show() {
        if (VcsApplicationSettings.getInstance().COMMIT_FROM_LOCAL_CHANGES) {
            popup.showCenteredInCurrentWindow(project)
        } else {
            popup.showInFocusCenter()
        }
    }
}
