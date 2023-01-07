package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class TypeEditor(title: String, name: String, description: String) : DialogWrapper(true) {

    private val root: JPanel
    private val nameLabel = JLabel(GitBundle.message("settings.type.table.column.1"))
    private val nameField: JBTextField
    private val descriptionLabel = JLabel(GitBundle.message("settings.type.table.column.2"))
    private val descriptionField: JBTextField

    init {
        setTitle(title)
        root = JPanel(BorderLayout())
        nameField = JBTextField(name)
        descriptionField = JBTextField(description)
        val formBuilder = FormBuilder.createFormBuilder()
            .addLabeledComponent(nameLabel, nameField)
            .addLabeledComponent(descriptionLabel, descriptionField)

        root.add(formBuilder.panel, BorderLayout.CENTER)
        root.preferredSize = Dimension(500, 0)
        init()
    }

    fun name(): String = nameField.text.trim()
    fun description(): String = descriptionField.text.trim { it <= ' ' }

    override fun createCenterPanel(): JComponent {
        return root
    }
}
