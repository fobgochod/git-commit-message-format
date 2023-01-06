package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.util.GitBundle
import com.fobgochod.git.commit.constant.GitConstant
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.SearchableConfigurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/**
 * 这个类Settings 中的属性被创建的时候
 */
class GitConfigurable : SearchableConfigurable {

    private var state: GitState = GitState.getInstance()
    private var component: GitComponent = GitComponent()

    override fun getId(): String {
        return GitConstant.PLUGIN_ID
    }

    override fun createComponent(): JComponent {
        return component.mainPanel
    }

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return GitBundle.message("plugin.name")
    }

    override fun reset() {
        component.typeTable.reset(state)
        component.commonCountField.text = state.commonCount.toString()
        ApplicationManager.getApplication().runWriteAction {
            component.templateEditor.document.setText(state.template)
        }
    }

    override fun isModified(): Boolean {
        val typeRows = component.typeTable.typeRows
        val commonCount: Int = component.commonCountField.text.toInt()
        val template = component.templateEditor.document.text.trim { it <= ' ' }
        return typeRows != state.typeRows || commonCount != state.commonCount || template != state.template
    }

    override fun apply() {
        state.typeRows = component.typeTable.typeRows
        state.commonCount = component.commonCountField.text.toInt()
        state.template = component.templateEditor.document.text
    }
}
