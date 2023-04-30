package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.domain.ViewForm
import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.options.SearchableConfigurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class GitConfigurable : SearchableConfigurable {

    private var state: GitState = GitState.getInstance()
    private var component: GitComponent = GitComponent()

    override fun getId(): String {
        return GitConfigurable::class.java.name
    }

    override fun createComponent(): JComponent {
        return component.mainPanel
    }

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return GitBundle.message("plugin.name")
    }

    override fun reset() {
        component.typeTable.reset(state.typeRows)
        component.typeCountField.text = state.typeCount.toString()
        component.viewMode.selectedItem = state.viewMode

        for (checkBox in component.viewForm) {
            val viewForm = ViewForm.valueOf(checkBox.text)
            checkBox.isSelected = state.isViewFormHidden(viewForm)
        }
    }

    override fun isModified(): Boolean {
        val typeRows = component.typeTable.typeRows
        val typeCount: Int = component.typeCountField.text.toInt()
        val viewMode = component.getViewMode()
        return typeRows != state.typeRows
                || typeCount != state.typeCount
                || viewMode != state.viewMode
                || isViewFormModified()
    }

    private fun isViewFormModified(): Boolean {
        for (checkBox in component.viewForm) {
            val viewForm = ViewForm.valueOf(checkBox.text)
            if (state.isViewFormHidden(viewForm) != checkBox.isSelected) {
                return true
            }
        }
        return false
    }

    override fun apply() {
        state.typeRows = component.typeTable.typeRows
        state.typeCount = component.typeCountField.text.toInt()
        state.viewMode = component.getViewMode()

        for (checkBox in component.viewForm) {
            val formItem = ViewForm.valueOf(checkBox.text)
            state.viewForm[formItem] = checkBox.isSelected
        }
    }
}
