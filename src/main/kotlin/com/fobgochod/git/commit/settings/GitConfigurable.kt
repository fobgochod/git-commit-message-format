package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel

class GitConfigurable :
    BoundSearchableConfigurable(GitBundle.message("configurable.display.name"), GitConfigurable::class.java.name) {

    private var myPanel: GitPanel = GitPanel()

    override fun getId(): String {
        return GitConfigurable::class.java.name
    }

    override fun createPanel(): DialogPanel {
        return myPanel.createPanel()
    }

    override fun reset() {
        super.reset()
        myPanel.reset()
    }

    override fun isModified(): Boolean {
        return super.isModified() || myPanel.isModified()
    }

    override fun apply() {
        super.apply()
        myPanel.apply()
    }
}
