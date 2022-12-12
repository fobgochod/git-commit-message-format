package com.fobgochod.git.commit.settings;

import com.fobgochod.git.commit.domain.DataSettings;
import com.fobgochod.git.commit.constant.GitCommitConstant;
import com.fobgochod.git.commit.domain.TypeItem;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.rits.cloning.Cloner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "com.fobgochod.git.commit.settings.GitCommitHelperSettings",
        storages = @Storage("GitCommitHelperSettings.xml")
)
public class GitCommitHelperState implements PersistentStateComponent<GitCommitHelperState> {

    private static final Logger log = Logger.getInstance(GitCommitHelperState.class);
    private DataSettings dataSettings;

    public static GitCommitHelperState getInstance() {
        return ApplicationManager.getApplication().getService(GitCommitHelperState.class);
    }

    @Nullable
    @Override
    public GitCommitHelperState getState() {
        if (this.dataSettings == null) {
            loadDefaultSettings();
        }
        return this;
    }


    @Override
    public void loadState(@NotNull GitCommitHelperState gitCommitHelperState) {
        XmlSerializerUtil.copyBean(gitCommitHelperState, this);
    }

    /**
     * 加载默认配置
     */
    private void loadDefaultSettings() {
        dataSettings = new DataSettings();
        try {
            dataSettings.setTemplate(GitCommitConstant.DEFAULT_TEMPLATE);
            List<TypeItem> typeItems = new LinkedList<>();
            typeItems.add(new TypeItem("feature", "A new feature"));
            typeItems.add(new TypeItem("fix", "A bug fix"));
            typeItems.add(new TypeItem("docs", "Documentation only changes"));
            typeItems.add(new TypeItem("style", "Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)"));
            typeItems.add(new TypeItem("refactor", "A code change that neither fixes a bug nor adds a feature"));
            typeItems.add(new TypeItem("perf", "A code change that improves performance"));
            typeItems.add(new TypeItem("test", "Adding missing tests or correcting existing tests"));
            typeItems.add(new TypeItem("build", "Changes that affect the build system or external dependencies (example scopes: gulp, broccoli, npm)"));
            typeItems.add(new TypeItem("ci", "Changes to our CI configuration files and scripts (example scopes: Travis, Circle, BrowserStack, SauceLabs)"));
            typeItems.add(new TypeItem("chore", "Other changes that don't modify src or test files"));
            typeItems.add(new TypeItem("revert", "Reverts a previous commit"));
            dataSettings.setTypeAliases(typeItems);
        } catch (Exception e) {
            log.error("loadDefaultSettings failed", e);
        }
    }

    /**
     * Getter method for property <tt>codeTemplates</tt>.
     *
     * @return property value of codeTemplates
     */
    public DataSettings getDateSettings() {
        if (dataSettings == null) {
            loadDefaultSettings();
        }
        return dataSettings;
    }


    public void setDateSettings(DataSettings dateSettings) {
        this.dataSettings = dateSettings;
    }


    public void updateTemplate(String template) {
        dataSettings.setTemplate(template);
    }

    public void updateTypeMap(List<TypeItem> typeItems) {
        dataSettings.setTypeAliases(typeItems);
    }


    @Override
    public GitCommitHelperState clone() {
        Cloner cloner = new Cloner();
        cloner.nullInsteadOfClone();
        return cloner.deepClone(this);
    }

}
