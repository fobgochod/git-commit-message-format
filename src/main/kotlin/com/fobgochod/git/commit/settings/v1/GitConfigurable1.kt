package com.fobgochod.git.commit.settings.v1

import com.fobgochod.git.commit.settings.GitSettingsConfigurable
import com.intellij.openapi.options.SearchableConfigurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class GitConfigurable1 : SearchableConfigurable {

    private var myPanel: GitComponent = GitComponent2()

    override fun getId(): String {
        return GitSettingsConfigurable.ID
    }

    override fun createComponent(): JComponent {
        return myPanel.getComponent()
    }

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return GitSettingsConfigurable.DISPLAY_NAME
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
