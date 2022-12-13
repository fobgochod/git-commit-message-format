package com.fobgochod.git.commit.view.settings;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class TypeEditor extends DialogWrapper {

    private final JPanel root;
    private final JTextField titleField;
    private final JTextField descriptionField;

    public TypeEditor(String dialogTitle, String title, String description) {
        super(true);
        setTitle(dialogTitle);
        
        root = new JPanel();
        titleField = new JTextField(title);
        descriptionField = new JTextField(description);

        JPanel panel = FormBuilder.createFormBuilder()
                .setHorizontalGap(5)
                .addLabeledComponent(new JLabel("title"), titleField)
                .addLabeledComponent(new JLabel("description"), descriptionField)
                .getPanel();
        panel.setPreferredSize(new Dimension(460, 70));
        root.add(panel);

        init();
    }

    public String getTitle() {
        return titleField.getText().trim();
    }

    public String getDescription() {
        return descriptionField.getText().trim();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return root;
    }

    public interface Validator {
        boolean isOK(String name, String value);
    }
}
