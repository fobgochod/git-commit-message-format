package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import org.jetbrains.annotations.NonNls

class GitSettingsConfigurable : BoundSearchableConfigurable(DISPLAY_NAME, HELP_ID, ID) {

    companion object {
        const val ID: @NonNls String = "git.commit.message"
        private const val HELP_ID: @NonNls String = "git.commit.message.help.topic"
        val DISPLAY_NAME: String = GitBundle.message("configurable.display.name")
    }

    private var panel: GitSettingsPanel = GitSettingsPanel()

    override fun getId(): String {
        return GitSettingsConfigurable::class.java.name
    }

    override fun createPanel(): DialogPanel {
        return panel.createPanel()
    }

    override fun reset() {
        super.reset()
        panel.reset()
    }

    override fun isModified(): Boolean {
        return super.isModified() || panel.isModified()
    }

    override fun apply() {
        super.apply()
        panel.apply()
    }
}
