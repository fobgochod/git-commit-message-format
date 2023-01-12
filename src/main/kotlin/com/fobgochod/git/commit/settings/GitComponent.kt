package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.action.ResetTypeAction
import com.fobgochod.git.commit.action.RestoreTypesAction
import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.util.GitBundle
import com.intellij.openapi.actionSystem.ActionToolbarPosition
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.ToolbarDecorator
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class GitComponent {

    /**
     * main panel
     */
    val mainPanel: JPanel = JPanel(BorderLayout(0, 10))

    /**
     * type标签
     */
    val typeTable: TypeTable = TypeTable()
    val commonCountField: JTextField = JTextField()

    init {
        initView()
        object : DoubleClickListener() {
            override fun onDoubleClick(event: MouseEvent): Boolean {
                return typeTable.editRow()
            }
        }.installOn(typeTable)
    }

    private fun initView() {
        // main
        mainPanel.add(JLabel(GitBundle.message("settings.desc.text")), BorderLayout.NORTH)
        mainPanel.add(
            ToolbarDecorator.createDecorator(typeTable)
                .setAddAction { typeTable.addRow() }
                .setRemoveAction { typeTable.removeRow() }
                .setEditAction { typeTable.editRow() }
                .setMoveUpAction { typeTable.moveUp() }
                .setMoveDownAction { typeTable.moveDown() }
                .addExtraAction(ResetTypeAction(typeTable))
                .addExtraAction(RestoreTypesAction(typeTable))
                .setToolbarPosition(ActionToolbarPosition.RIGHT)
                .createPanel(),
            BorderLayout.CENTER)
        mainPanel.add(
            FormBuilder.createFormBuilder()
                .addLabeledComponent(JLabel(GitBundle.message("settings.common.type.count")), commonCountField).panel,
            BorderLayout.SOUTH
        )
    }
}
