package com.fobgochod.git.commit.settings.v1

import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.options.SearchableConfigurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class GitConfigurable1 : SearchableConfigurable {

    private var myPanel: GitComponent = GitComponent2()

    override fun getId(): String {
        return GitConfigurable1::class.java.name
    }

    override fun createComponent(): JComponent {
        return myPanel.getComponent()
    }

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return GitBundle.message("configurable.display.name")
    }

    override fun reset() {
        myPanel.reset()
    }

    override fun isModified(): Boolean {
        return myPanel.isModified()
    }


    override fun apply() {
        myPanel.apply()
    }
}
