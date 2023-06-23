package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.options.SearchableConfigurable
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

    override fun getDisplayName(): String {
        return GitBundle.message("configurable.display.name")
    }

    override fun reset() {
        component.typeTable.reset(state.typeRows)
        component.typeCountField.text = state.typeCount.toString()
    }

    override fun isModified(): Boolean {
        val typeRows = component.typeTable.typeRows
        val typeCount: Int = component.typeCountField.text.toInt()
        return typeRows != state.typeRows || typeCount != state.typeCount
    }

    override fun apply() {
        state.typeRows = component.typeTable.typeRows
        state.typeCount = component.typeCountField.text.toInt()
    }
}
