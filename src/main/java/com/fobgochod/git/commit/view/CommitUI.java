package com.fobgochod.git.commit.view;

import com.fobgochod.git.commit.domain.CommitMessage;
import com.fobgochod.git.commit.domain.TypeRow;
import com.fobgochod.git.commit.settings.GitState;
import com.fobgochod.git.commit.util.GitBundle;
import com.fobgochod.git.commit.util.GitLog;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBRadioButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

public class CommitUI {

    private final transient Project project;
    private final GitState state = GitState.getInstance();
    private final ButtonGroup changeTypeGroup = new ButtonGroup();

    private JPanel root;
    private JComboBox<TypeRow> changeType;
    private JPanel changeTypePanel;
    private JComboBox<String> changeScope;
    private JTextField changeSubject;
    private JTextArea changeBody;
    private JCheckBox wrapText;
    private JTextArea breakingChanges;
    private JTextField closedIssues;
    private JCheckBox skipCI;
    private JLabel editSettings;

    public CommitUI(Project project, CommitMessage commitMessage) {
        this.project = project;
        initView();
        initEvent();
        initData(commitMessage);
    }

    private void initView() {
        int minCount = Math.min(state.getTypeCount(), state.getTypeRows().size());
        changeTypePanel.setLayout(new GridLayout(minCount, 1));

        for (int i = 0; i < state.getTypeRows().size(); i++) {
            if (i < minCount) {
                TypeRow typeRow = state.getTypeRows().get(i);
                JBRadioButton radioButton = new JBRadioButton(typeRow.toString());
                radioButton.setToolTipText(typeRow.toString());
                changeTypeGroup.add(radioButton);
                changeTypePanel.add(radioButton);
            }
        }

        editSettings.setIcon(AllIcons.General.Settings);
    }

    private void initEvent() {
        Enumeration<AbstractButton> elements = changeTypeGroup.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton element = elements.nextElement();
            if (element.isSelected()) {
                changeType.setSelectedItem(state.getTypeFromName(element.getText()));
            }
        }

        changeType.addItemListener(e -> {
            TypeRow item = (TypeRow) e.getItem();
            if (state.getTypeRows().indexOf(item) > state.getTypeCount()) {
                changeTypeGroup.clearSelection();
            } else {
                Enumeration<AbstractButton> elements1 = changeTypeGroup.getElements();
                while (elements1.hasMoreElements()) {
                    AbstractButton element = elements1.nextElement();
                    if (item == state.getTypeFromName(element.getText())) {
                        element.setSelected(true);
                    }
                }
            }
        });

        editSettings.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                ShowSettingsUtil.getInstance().showSettingsDialog(project, GitBundle.message("plugin.name"));
            }
        });
    }

    private void initData(CommitMessage commitMessage) {
        for (TypeRow typeRow : state.getTypeRows()) {
            changeType.addItem(typeRow);
        }

        GitLog.Result result = new GitLog(project).execute();
        result.getScopes().forEach(changeScope::addItem);

        restoreFromParsedCommitMessage(commitMessage);
    }

    public JPanel getRoot() {
        return root;
    }

    public CommitMessage getCommitMessage() {
        return new CommitMessage(
                getChangeType(),
                getChangeScope(),
                changeSubject.getText().trim(),
                changeBody.getText().trim(),
                wrapText.isSelected(),
                breakingChanges.getText().trim(),
                closedIssues.getText().trim(),
                skipCI.isSelected()
        );
    }

    private String getChangeType() {
        Object selectedItem = changeType.getSelectedItem();
        return selectedItem == null ? "" : ((TypeRow) selectedItem).getName();
    }

    private String getChangeScope() {
        Object selectedItem = changeScope.getSelectedItem();
        return selectedItem == null ? "" : selectedItem.toString();
    }

    private void restoreFromParsedCommitMessage(CommitMessage commitMessage) {
        changeType.setSelectedItem(commitMessage.getChangeType());
        changeScope.setSelectedItem(commitMessage.getChangeScope());
        changeSubject.setText(commitMessage.getChangeSubject());
        changeBody.setText(commitMessage.getChangeBody());
        wrapText.setSelected(commitMessage.getWrapText());
        breakingChanges.setText(commitMessage.getBreakingChanges());
        closedIssues.setText(commitMessage.getClosedIssues());
        skipCI.setSelected(commitMessage.getSkipCI());
    }
}
