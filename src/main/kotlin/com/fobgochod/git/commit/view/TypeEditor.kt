package com.fobgochod.git.commit.view

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.FormBuilder
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField


class TypeEditor(dialogTitle: String?, title: String?, description: String?) :
    DialogWrapper(true) {
    private val root: JPanel
    private val titleField: JTextField
    private val descriptionField: JTextField

    init {
        setTitle(dialogTitle)
        root = JPanel()
        titleField = JTextField(title)
        descriptionField = JTextField(description)
        val panel = FormBuilder.createFormBuilder()
            .setHorizontalGap(5)
            .addLabeledComponent(JLabel("title"), titleField)
            .addLabeledComponent(JLabel("description"), descriptionField)
            .panel
        panel.preferredSize = Dimension(460, 70)
        root.add(panel)
        init()
    }

    override fun getTitle(): String {
        return titleField.text.trim { it <= ' ' }
    }

    val description: String
        get() = descriptionField.text.trim { it <= ' ' }

    override fun createCenterPanel(): JComponent {
        return root
    }

    interface Validator {
        fun isOK(name: String, value: String): Boolean
    }
}
