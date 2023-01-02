package com.fobgochod.git.commit.settings

import com.fobgochod.git.GitBundle
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.SearchableConfigurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent


/**
 * 这个类Settings 中的属性被创建的时候
 */
class GitCommitHelperConfigurable : SearchableConfigurable {

    private var state: GitCommitHelperState = GitCommitHelperState.getInstance()
    private var component: GitCommitHelperComponent = GitCommitHelperComponent()

    override fun getId(): String {
        return "git.commit.helper"
    }

    override fun createComponent(): JComponent {
        return component.mainPanel
    }

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return GitBundle.message("plugin.name")
    }

    override fun reset() {
        component.typeTable.reset(state)
        ApplicationManager.getApplication().runWriteAction {
            component.templateEditor.document.setText(state.template)
        }
    }

    override fun isModified(): Boolean {
        val template = component.templateEditor.document.text.trim { it <= ' ' }
        val typeRows = component.typeTable.typeRows
        return template != state.template || typeRows != state.typeRows
    }

    override fun apply() {
        state.template = component.templateEditor.document.text
        state.typeRows = component.typeTable.typeRows
    }
}
