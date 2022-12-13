package com.fobgochod.git.commit.settings;

import com.fobgochod.git.commit.domain.TypeTable;
import com.intellij.codeInsight.template.HtmlContextType;
import com.intellij.codeInsight.template.impl.ConstantNode;
import com.intellij.codeInsight.template.impl.TemplateEditorUtil;
import com.intellij.codeInsight.template.impl.TemplateImpl;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
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

    private static final TemplateImpl GIT_COMMIT_TEMPLATE = new TemplateImpl("commit-template", "Git");
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


        String template = Optional.of(settings.getTemplate()).orElse("");

        GIT_COMMIT_TEMPLATE.setString(template);
        GIT_COMMIT_TEMPLATE.addVariable("type", new ConstantNode("type"), true);
        GIT_COMMIT_TEMPLATE.addVariable("scope", "", "scope", true);
        GIT_COMMIT_TEMPLATE.addVariable("subject", "", "subject", true);
        GIT_COMMIT_TEMPLATE.addVariable("body", "", "body", true);
        GIT_COMMIT_TEMPLATE.addVariable("changes", "", "changes", true);

        templateEditor = TemplateEditorUtil.createEditor(false, GIT_COMMIT_TEMPLATE.getString(), GIT_COMMIT_TEMPLATE.createContext());
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
        settings.setTemplate(templateEditor.getDocument().getText());
        return settings;
    }

    public void reset(GitCommitHelperState settings) {
        this.settings.setTemplate(settings.getTemplate());
        this.settings.setTypeItems(settings.getTypeItems());
        typeTable.reset(settings);
        ApplicationManager.getApplication().runWriteAction(() -> templateEditor.getDocument().setText(settings.getTemplate()));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
