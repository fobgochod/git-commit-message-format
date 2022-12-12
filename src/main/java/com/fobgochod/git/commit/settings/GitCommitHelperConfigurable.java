package com.fobgochod.git.commit.settings;

import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 这个类Settings 中的属性被创建的时候
 */
public class GitCommitHelperConfigurable implements SearchableConfigurable {

    private GitCommitHelperComponent component;
    private GitCommitHelperState settings;


    public GitCommitHelperConfigurable() {
        settings = GitCommitHelperState.getInstance();
    }

    @NotNull
    @Override
    public String getId() {
        return "plugin.git.commit.helper";
    }


    @Nullable
    @Override
    public JComponent createComponent() {
        if (component == null) {
            component = new GitCommitHelperComponent();
        }
        return component.getMainPanel();
    }


    @Nullable
    @Override
    public String getHelpTopic() {
        return "help.git.commit.helper.configuration";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "GitCommitHelper";
    }


    public void reset() {
        if (component != null) {
            component.reset(settings);
        }
    }

    @Override
    public boolean isModified() {
        return component != null && component.isSettingsModified(settings);
    }


    @Override
    public void apply() {
        settings.setDateSettings(component.getSettings().getDateSettings());
        settings = component.getSettings().clone();
    }
}
