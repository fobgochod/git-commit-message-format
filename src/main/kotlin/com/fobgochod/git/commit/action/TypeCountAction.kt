package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.settings.GitSettings
import com.fobgochod.git.commit.util.GitBundle.message
import com.fobgochod.git.commit.util.GitIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.bindIntText
import com.intellij.ui.dsl.builder.panel

class TypeCountAction : AnAction(
    message("action.toolbar.type.count.text"),
    message("action.toolbar.type.count.description"),
    GitIcons.getNumberIcon(state.typeCount)
) {

    companion object {
        private val state = GitSettings.instance
    }

    private val dialog = panel {
        row(message("settings.type.count")) {
            intTextField(0..100)
                .align(AlignX.FILL)
                .resizableColumn()
                .bindIntText(state::typeCount)
        }
    }

    override fun actionPerformed(event: AnActionEvent) {
        val builder = DialogBuilder()
        builder.setTitle(message("settings.type.count.title"))

        builder.setCenterPanel(dialog)
        builder.setOkOperation {
            builder.dialogWrapper.close(DialogWrapper.OK_EXIT_CODE)
        }
        builder.showModal(true)
    }

    fun isModified(): Boolean {
        return dialog.isModified()
    }

    fun apply() {
        dialog.apply()
    }

    fun reset() {
        dialog.reset()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
