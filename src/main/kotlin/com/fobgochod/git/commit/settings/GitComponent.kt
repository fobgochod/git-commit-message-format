package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.action.ResetTypeAction
import com.fobgochod.git.commit.action.RestoreTypesAction
import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.domain.ViewForm
import com.fobgochod.git.commit.domain.ViewMode
import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class GitComponent {

    /**
     * main panel
     */
    val mainPanel: JPanel = JPanel(BorderLayout(0, 6))
    private val topPanel: JPanel = JPanel(BorderLayout())
    private val bottomPanel: JPanel = JPanel(BorderLayout())

    /**
     * type标签
     */
    val typeTable: TypeTable = TypeTable()
    val typeCountField: JTextField = JTextField()
    val viewMode: ComboBox<ViewMode> = ComboBox()

    /**
     * 窗口组件
     */
    val viewForm: MutableList<JCheckBox> = LinkedList()

    init {
        initData()
        initView()
        initEvent()
    }

    private fun initData() {
        ViewMode.values().forEach { mode ->
            viewMode.addItem(mode)
        }

        ViewForm.values().forEach { item ->
            val checkBox = JBCheckBox(item.name)
            checkBox.isEnabled = item != ViewForm.Type && item != ViewForm.Subject
            checkBox.toolTipText = item.description()
            checkBox.isSelected = GitState.getInstance().isViewFormHidden(item)
            viewForm.add(checkBox)
        }
    }

    private fun initView() {
        // main
        mainPanel.add(topPanel, BorderLayout.NORTH)
        mainPanel.add(
            ToolbarDecorator.createDecorator(typeTable)
                .setAddAction { typeTable.addRow() }
                .setRemoveAction { typeTable.removeRow() }
                .setEditAction { typeTable.editRow() }
                .setMoveUpAction { typeTable.moveUp() }
                .setMoveDownAction { typeTable.moveDown() }
                .addExtraAction(ResetTypeAction(typeTable))
                .addExtraAction(RestoreTypesAction(typeTable))
                .createPanel(),
            BorderLayout.CENTER)
        mainPanel.add(bottomPanel, BorderLayout.SOUTH)

        // top panel
        topPanel.add(JLabel(GitBundle.message("settings.desc.text")), BorderLayout.WEST)
        topPanel.add(viewMode, BorderLayout.EAST)

        // bottom panel
        val formRow1 = JPanel(BorderLayout());
        formRow1.add(
            FormBuilder.createFormBuilder()
                .addLabeledComponent(GitBundle.message("settings.common.type.count"), typeCountField).panel,
            BorderLayout.WEST
        )
        formRow1.add(
            FormBuilder.createFormBuilder()
                .addLabeledComponent(GitBundle.message("settings.common.view.mode"), viewMode).panel,
            BorderLayout.EAST
        )
        val formRow2 = JPanel(FlowLayout(FlowLayout.LEFT, 2, 0))
        viewForm.forEach { element ->
            formRow2.add(element)
        }

        val formBuilder = FormBuilder.createFormBuilder()
            .addComponent(formRow1)
            .addLabeledComponent(GitBundle.message("settings.common.hidden.ui"), formRow2)
        bottomPanel.add(formBuilder.panel, BorderLayout.CENTER)
    }

    private fun initEvent() {
        object : DoubleClickListener() {
            override fun onDoubleClick(event: MouseEvent): Boolean {
                return typeTable.editRow()
            }
        }.installOn(typeTable)
    }

    fun getViewMode(): ViewMode {
        val selectedItem = viewMode.selectedItem
        return selectedItem as ViewMode
    }
}
