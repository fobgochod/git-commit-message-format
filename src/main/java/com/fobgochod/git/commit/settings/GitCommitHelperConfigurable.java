package com.fobgochod.git.commit.settings;

import com.fobgochod.git.commit.domain.TypeRow;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * 这个类Settings 中的属性被创建的时候
 */
public class GitCommitHelperConfigurable implements SearchableConfigurable {

    private final GitCommitHelperState settings;
    private GitCommitHelperComponent component;


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
        if (component == null) {
            return false;
        }
        String template = component.getSettings().getTemplate().trim();
        List<TypeRow> typeRows = component.getSettings().getTypeRows();
        return !template.equals(settings.getTemplate()) || typeRows != settings.getTypeRows();
    }


    @Override
    public void apply() {
        if (component != null) {
            settings.setTemplate(component.getSettings().getTemplate());
            settings.setTypeRows(component.getSettings().getTypeRows());
        }
    }
}
