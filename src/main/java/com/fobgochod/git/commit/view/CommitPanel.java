package com.fobgochod.git.commit.view;

import com.fobgochod.git.commit.ChangeType;
import com.fobgochod.git.commit.CommitMessage;
import com.fobgochod.git.commit.GitLogQuery;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.io.File;

public class CommitPanel {

    private JPanel mainPanel;
    private JComboBox<ChangeType> changeType;
    private JComboBox<String> changeScope;
    private JTextField shortDescription;
    private JTextArea longDescription;
    private JCheckBox wrapTextCheckBox;
    private JTextArea breakingChanges;
    private JTextField closedIssues;
    private JCheckBox skipCICheckBox;

    public CommitPanel(Project project, CommitMessage commitMessage) {

        changeType.setModel(new DefaultComboBoxModel<>(ChangeType.values()));

        File workingDirectory = new File(project.getBasePath());
        GitLogQuery.Result result = new GitLogQuery(workingDirectory).execute();
        if (result.isSuccess()) {
            changeScope.addItem(""); // no value by default
            result.getScopes().forEach(changeScope::addItem);
        }

        if (commitMessage != null) {
            restoreValuesFromParsedCommitMessage(commitMessage);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public CommitMessage getCommitMessage() {
        return new CommitMessage(
                getSelectedChangeType(),
                getSelectedChangeScope(),
                shortDescription.getText().trim(),
                longDescription.getText().trim(),
                breakingChanges.getText().trim(),
                closedIssues.getText().trim(),
                wrapTextCheckBox.isSelected(),
                skipCICheckBox.isSelected()
        );
    }

    private ChangeType getSelectedChangeType() {
        Object selectedItem = changeType.getSelectedItem();
        return (ChangeType) selectedItem;
    }

    private String getSelectedChangeScope() {
        Object selectedItem = changeScope.getSelectedItem();
        return (String) selectedItem;
    }

    private void restoreValuesFromParsedCommitMessage(CommitMessage commitMessage) {
        changeType.setSelectedItem(commitMessage.getChangeType());
        changeScope.setSelectedItem(commitMessage.getChangeScope());
        shortDescription.setText(commitMessage.getShortDescription());
        longDescription.setText(commitMessage.getLongDescription());
        breakingChanges.setText(commitMessage.getBreakingChanges());
        closedIssues.setText(commitMessage.getClosedIssues());
        skipCICheckBox.setSelected(commitMessage.isSkipCI());
    }
}
