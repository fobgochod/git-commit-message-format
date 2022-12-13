package com.fobgochod.git.commit.view.settings;

import com.fobgochod.git.commit.ChangeType;
import com.fobgochod.git.commit.CommitMessage;
import com.fobgochod.git.commit.GitLogQuery;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CommitWindow {

    private JPanel mainPanel;
    private JComboBox<ChangeType> changeType;
    private JComboBox<String> changeScope;
    private JTextField shortDescription = new JTextField();
    private JTextArea longDescription = new JTextArea();
    private JCheckBox wrapTextCheckBox = new JCheckBox("Wrap text at 72 characters?", true);
    private JTextArea breakingChanges = new JTextArea();
    private JTextField closedIssues = new JTextField();
    private JCheckBox skipCICheckBox = new JCheckBox("Skip CI?");

    public CommitWindow(Project project, CommitMessage commitMessage) {

        changeType = new ComboBox<>(new DefaultComboBoxModel<>(ChangeType.values()));
        changeScope = new ComboBox<>( );

        File workingDirectory = new File(project.getBasePath());
        GitLogQuery.Result result = new GitLogQuery(workingDirectory).execute();
        if (result.isSuccess()) {
            changeScope.addItem(""); // no value by default
            result.getScopes().forEach(changeScope::addItem);
        }

        if (commitMessage != null) {
            restoreValuesFromParsedCommitMessage(commitMessage);
        }

        longDescription.setPreferredSize(new Dimension(150, 100));
        longDescription.setLineWrap(true);
        breakingChanges.setPreferredSize(new Dimension(150, 50));
        breakingChanges.setLineWrap(true);

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JLabel("Type of change"), changeType)
                .addLabeledComponent(new JLabel("Scope of this change"), changeScope)
                .addLabeledComponent(new JLabel("Short description"), shortDescription)
                .addLabeledComponent(new JLabel("Long description"), longDescription)
                .addComponentToRightColumn(wrapTextCheckBox)
                .addLabeledComponent(new JLabel("Breaking changes"), breakingChanges)
                .addLabeledComponent(new JLabel("Closed issues"), closedIssues)
                .addComponentToRightColumn(skipCICheckBox)
                .getPanel();
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
