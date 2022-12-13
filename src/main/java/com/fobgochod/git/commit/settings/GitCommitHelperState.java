package com.fobgochod.git.commit.settings;

import com.fobgochod.git.commit.constant.GitCommitConstant;
import com.fobgochod.git.commit.domain.TypeEnum;
import com.fobgochod.git.commit.domain.TypeRow;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
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

    private static final Logger logger = Logger.getInstance(GitCommitHelperState.class);
    private String template;
    private List<TypeRow> typeRows;

    public static GitCommitHelperState getInstance() {
        return ApplicationManager.getApplication().getService(GitCommitHelperState.class);
    }

    @Nullable
    @Override
    public GitCommitHelperState getState() {
        loadDefaultSettings();
        return this;
    }


    @Override
    public void loadState(@NotNull GitCommitHelperState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    /**
     * 加载默认配置
     */
    private void loadDefaultSettings() {
        if (this.template == null) {
            template = GitCommitConstant.DEFAULT_TEMPLATE;
        }
        if (this.typeRows == null) {
            List<TypeRow> typeRows = new LinkedList<>();
            for (TypeEnum type : TypeEnum.values()) {
                typeRows.add(new TypeRow(type.title(), type.description()));
            }
            this.typeRows = typeRows;
        }
    }

    public String getTemplate() {
        loadDefaultSettings();
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<TypeRow> getTypeRows() {
        loadDefaultSettings();
        return typeRows;
    }

    public void setTypeRows(List<TypeRow> typeRows) {
        this.typeRows = typeRows;
    }
}
