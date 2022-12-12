package com.fobgochod.git.commit.settings;

import com.fobgochod.git.commit.domain.TypeTable;
import com.intellij.codeInsight.template.HtmlContextType;
import com.intellij.codeInsight.template.impl.TemplateEditorUtil;
import com.intellij.codeInsight.template.impl.TemplateImpl;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;


public class GitCommitHelperComponent {

    private static final TemplateImpl MOCK_TEMPLATE = new TemplateImpl("mockTemplate-xxx", "mockTemplateGroup-yyy");
    private GitCommitHelperState settings = GitCommitHelperState.getInstance();

    private JPanel mainPanel;
    private JBTabs tabbedPane;
    /**
     * type标签
     */
    private transient TabInfo typeTab;
    private JPanel typePanel;
    private TypeTable typeTable;
    /**
     * template标签
     */
    private transient TabInfo templateTab;
    private JBSplitter templatePanel;
    private Editor templateEditor;

    public GitCommitHelperComponent() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JLabel("Personalize your git commit types and templates ."), BorderLayout.NORTH);

        tabbedPane = new JBTabsImpl(null, null, ApplicationManager.getApplication());
        mainPanel.add(tabbedPane.getComponent(), BorderLayout.CENTER);


        typePanel = new JPanel(new BorderLayout());
        typeTable = new TypeTable();
        typePanel.add(
                ToolbarDecorator.createDecorator(typeTable)
                        .setAddAction(action -> typeTable.addAlias())
                        .setRemoveAction(action -> typeTable.removeSelectedAliases())
                        .setEditAction(action -> typeTable.editAlias())
                        .setMoveUpAction(action -> typeTable.moveUp())
                        .setMoveDownAction(action -> typeTable.moveDown())
                        .addExtraAction(
                                new AnAction("Reset", "Reset Default Aliases", AllIcons.Actions.Rollback) {
                                    @Override
                                    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                                        typeTable.resetDefaultAliases();
                                    }
                                })
                        .createPanel(), BorderLayout.CENTER);
        new DoubleClickListener() {
            @Override
            protected boolean onDoubleClick(MouseEvent event) {
                return typeTable.editAlias();
            }
        }.installOn(typeTable);


        String template = Optional.of(settings.getDateSettings().getTemplate()).orElse("");
        templateEditor = TemplateEditorUtil.createEditor(false, template, MOCK_TEMPLATE.createContext());
        TemplateEditorUtil.setHighlighter(templateEditor, new HtmlContextType());

        JPanel upPanel = new JPanel(new BorderLayout());
        upPanel.add(new JLabel("Template variables:"), BorderLayout.NORTH);
        JPanel templateVariables = new JPanel(new GridBagLayout());
        upPanel.add(templateVariables, BorderLayout.CENTER);

        List<JLabel> labels = new ArrayList<>();
        labels.add(new JLabel("${type} corresponds to the submission menu 'Type of chang'"));
        labels.add(new JLabel("${scope} corresponds to the submission menu 'Scope of this change'"));
        labels.add(new JLabel("${subject} corresponds to the submission menu 'Short description'"));
        labels.add(new JLabel("${body} corresponds to the submission menu 'Long description'"));
        labels.add(new JLabel("${changes} corresponds to the submission menu 'Breaking changes'"));
        labels.add(new JLabel("${closes} corresponds to the submission menu 'Closed issues'"));
        labels.add(new JLabel("${newLine} is '\\n'"));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = JBUI.insets(2, 10, 0, 0);
        IntStream.range(0, labels.size()).forEach(i -> {
            constraints.weightx = 1;
            constraints.gridx = 0;
            constraints.gridy = i;
            templateVariables.add(labels.get(i), constraints);
        });


        JPanel downPanel = new JPanel(new BorderLayout());
        downPanel.add(new JLabel("Template text:"), BorderLayout.NORTH);
        downPanel.add(templateEditor.getComponent(), BorderLayout.CENTER);

        templatePanel = new JBSplitter(true, "", 0.3F);
        templatePanel.setFirstComponent(upPanel);
        templatePanel.setSecondComponent(downPanel);


        typeTab = new TabInfo(typePanel);
        typeTab.setText("type");
        tabbedPane.addTab(typeTab);

        templateTab = new TabInfo(templatePanel);
        templateTab.setText("template");
        tabbedPane.addTab(templateTab);
    }


    public GitCommitHelperState getSettings() {
        typeTable.commit(settings);
        settings.getDateSettings().setTemplate(templateEditor.getDocument().getText());
        return settings;
    }

    public void reset(GitCommitHelperState settings) {
        this.settings = settings.clone();
        typeTable.reset(settings);
        ApplicationManager.getApplication().runWriteAction(() -> templateEditor.getDocument().setText(settings.getDateSettings().getTemplate()));
    }


    public boolean isSettingsModified(GitCommitHelperState settings) {
        if (typeTable.isModified(settings)) return true;
        return isModified(settings);
    }

    public boolean isModified(GitCommitHelperState data) {
        if (!StringUtil.equals(settings.getDateSettings().getTemplate(), templateEditor.getDocument().getText())) {
            return true;
        }
        if (settings.getDateSettings().getTypeAliases() == data.getDateSettings().getTypeAliases()) {
            return true;
        }
        return false;
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}
