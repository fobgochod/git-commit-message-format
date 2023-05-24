package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class TypeDialog(
    title: String,
    private val name: String = "",
    private val description: String = ""
) : DialogWrapper(true) {

    private val root: JPanel = JPanel(BorderLayout())
    private val formBuilder: FormBuilder = FormBuilder.createFormBuilder()
    private val nameLabel = JLabel(GitBundle.message("settings.type.table.column.1"))
    private val nameField: JBTextField = JBTextField()
    private val descriptionLabel = JLabel(GitBundle.message("settings.type.table.column.2"))
    private val descriptionField: JBTextField = JBTextField()

    init {
        setTitle(title)
        initView()
        init()
    }

    private fun initView() {
        nameField.text = name
        descriptionField.text = description
        formBuilder.addLabeledComponent(nameLabel, nameField)
            .addLabeledComponent(descriptionLabel, descriptionField)
        root.add(formBuilder.panel, BorderLayout.CENTER)
        root.preferredSize = JBUI.size(500, 0)
    }

    fun name(): String = nameField.text.trim()
    fun description(): String = descriptionField.text.trim { it <= ' ' }

    override fun createCenterPanel(): JComponent {
        return root
    }
}
