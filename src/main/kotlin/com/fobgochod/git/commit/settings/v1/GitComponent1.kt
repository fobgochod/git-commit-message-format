package com.fobgochod.git.commit.settings.v1

import com.fobgochod.git.commit.action.ResetTypeAction
import com.fobgochod.git.commit.action.RestoreTypesAction
import com.fobgochod.git.commit.domain.TypeTable
import com.fobgochod.git.commit.domain.option.ComponentType
import com.fobgochod.git.commit.domain.option.SkipCI
import com.fobgochod.git.commit.domain.option.ViewMode
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

internal class GitComponent1 : GitComponent {

    val state: GitState1 = GitState1.getInstance()

    /**
     * main panel
     */
    private val panel: JPanel = JPanel(BorderLayout(0, 6))
    private val topPanel: JPanel = JPanel(BorderLayout())
    private val bottomPanel: JPanel = JPanel(BorderLayout())

    /**
     * type标签
     */
    private val typeTable: TypeTable = TypeTable()

    /**
     * ButtonGroup显示type数目
     */
    private val typeCountField: JTextField = JTextField()

    /**
     *  skip workflow keywords
     */
    private val skipCI: ComboBox<String> = ComboBox(140)

    /**
     * 弹窗方式
     */
    private val viewMode: ComboBox<ViewMode> = ComboBox(140)

    /**
     * 窗口组件
     */
    private val viewForm: MutableList<JCheckBox> = LinkedList()

    init {
        initData()
        initView()
        initEvent()
    }

    override fun getComponent(): JPanel {
        return panel
    }

    override fun isModified(): Boolean {
        val typeRows = typeTable.typeRows
        val typeCount: Int = typeCountField.text.toInt()
        val skipCI = getSkipCI()
        val viewMode = getViewMode()
        return typeRows != state.typeRows
                || typeCount != state.typeCount
                || skipCI != state.skipCI
                || viewMode != state.viewMode
                || isViewFormModified(state)
    }

    override fun apply() {
        state.typeRows = typeTable.typeRows
        state.typeCount = typeCountField.text.toInt()
        state.skipCI = getSkipCI()
        state.viewMode = getViewMode()

        for (checkBox in viewForm) {
            val componentType = ComponentType.valueOf(checkBox.text)
            state.componentType[componentType] = checkBox.isSelected
        }
    }

    override fun reset() {
        typeTable.reset(state.typeRows)
        typeCountField.text = state.typeCount.toString()
        skipCI.selectedItem = state.skipCI
        viewMode.selectedItem = state.viewMode

        for (checkBox in viewForm) {
            val componentType = ComponentType.valueOf(checkBox.text)
            checkBox.isSelected = state.isComponentHidden(componentType)
        }
    }

    private fun isViewFormModified(state: GitState1): Boolean {
        for (checkBox in viewForm) {
            val componentType = ComponentType.valueOf(checkBox.text)
            if (state.isComponentHidden(componentType) != checkBox.isSelected) {
                return true
            }
        }
        return false
    }

    private fun initData() {
        SkipCI.values().forEach { word ->
            skipCI.addItem(word.label)
        }
        ViewMode.values().forEach { mode ->
            viewMode.addItem(mode)
        }
        ComponentType.values().forEach { item ->
            val checkBox = JBCheckBox(item.name)
            checkBox.isEnabled = item != ComponentType.Type && item != ComponentType.Subject
            checkBox.toolTipText = item.description()
            checkBox.isSelected = state.isComponentHidden(item)
            viewForm.add(checkBox)
        }
    }

    private fun initView() {
        // main
        panel.add(topPanel, BorderLayout.NORTH)
        panel.add(
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
        panel.add(bottomPanel, BorderLayout.SOUTH)

        // top panel
        topPanel.add(JLabel(GitBundle.message("settings.desc.text")), BorderLayout.WEST)
        topPanel.add(viewMode, BorderLayout.EAST)

        // bottom panel
        val hiddenUIs = JPanel(FlowLayout(FlowLayout.LEFT, 2, 0))
        viewForm.forEach { element ->
            hiddenUIs.add(element)
        }
        val formBuilder = FormBuilder.createFormBuilder()
            .addLabeledComponent(GitBundle.message("settings.common.type.count"), typeCountField)
            .addLabeledComponent(GitBundle.message("settings.common.view.mode"), viewMode)
            .addLabeledComponent(GitBundle.message("settings.common.skip.ci.word"), skipCI)
            .addLabeledComponent(GitBundle.message("settings.common.hidden.ui"), hiddenUIs)
        bottomPanel.add(formBuilder.panel, BorderLayout.CENTER)
    }

    private fun initEvent() {
        object : DoubleClickListener() {
            override fun onDoubleClick(event: MouseEvent): Boolean {
                return typeTable.editRow()
            }
        }.installOn(typeTable)
    }

    private fun getSkipCI(): SkipCI {
        val selectedItem = skipCI.selectedItem
        return selectedItem as SkipCI
    }

    fun getViewMode(): ViewMode {
        val selectedItem = viewMode.selectedItem
        return selectedItem as ViewMode
    }
}
