package com.fobgochod.git.commit.view

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class TypeEditor(dialogTitle: String, title: String, description: String) : DialogWrapper(true) {

    private val root: JPanel
    private val titleField: JBTextField
    private val descriptionField: JBTextField

    init {
        setTitle(dialogTitle)
        root = JPanel(BorderLayout())
        titleField = JBTextField(title)
        descriptionField = JBTextField(description)
        val panel = FormBuilder.createFormBuilder().addLabeledComponent(JLabel("title"), titleField)
            .addLabeledComponent(JLabel("description"), descriptionField).panel
        root.add(panel, BorderLayout.CENTER)
        root.preferredSize = Dimension(500, 0)
        init()
    }

    fun title(): String = titleField.text.trim { it <= ' ' }
    fun description(): String = descriptionField.text.trim { it <= ' ' }

    override fun createCenterPanel(): JComponent {
        return root
    }

    interface Validator {
        fun isOK(name: String, value: String): Boolean
    }
}
