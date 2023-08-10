package com.fobgochod.git.commit.action

import com.fobgochod.git.commit.settings.GitSettings
import com.fobgochod.git.commit.settings.type.TypeModel
import com.fobgochod.git.commit.util.GitBundle.message
import com.fobgochod.git.commit.util.GitIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.bindValue
import com.intellij.ui.dsl.builder.panel
import javax.swing.JSlider
import kotlin.math.ceil

class TypeCountAction(private val typeModel: TypeModel) : AnAction(
    message("action.toolbar.type.count.text"),
    message("action.toolbar.type.count.description"),
    GitIcons.getNumberIcon(state.typeCount)
) {

    companion object {
        private val state = GitSettings.instance
    }

    private var slider = JSlider()
    private val dialog = panel {
        row {
            slider(0, state.typeRows.size, 1, 2)
                .applyToComponent { slider = this }
                .align(AlignX.FILL)
                .resizableColumn()
                .bindValue(state::typeCount)
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(event: AnActionEvent) {
        event.presentation.icon = GitIcons.getNumberIcon(slider.value)
    }

    override fun actionPerformed(event: AnActionEvent) {
        val builder = DialogBuilder()
        builder.setTitle(message("settings.type.count.title"))

        slider.maximum = typeModel.rowCount
        slider.labelTable = null
        slider.majorTickSpacing = ceil(slider.maximum / 6.0).toInt()
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
}
