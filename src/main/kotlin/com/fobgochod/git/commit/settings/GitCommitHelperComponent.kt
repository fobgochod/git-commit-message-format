package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.domain.TemplateVarType
import com.fobgochod.git.commit.domain.TypeTable
import com.intellij.codeInsight.template.HtmlContextType
import com.intellij.codeInsight.template.impl.TemplateEditorUtil
import com.intellij.codeInsight.template.impl.TemplateImpl
import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.Editor
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.JBSplitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.tabs.JBTabs
import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.impl.JBTabsImpl
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.MouseEvent
import java.util.stream.IntStream
import javax.swing.JLabel
import javax.swing.JPanel


class GitCommitHelperComponent : Disposable {

    /**
     * main panel
     */
    val mainPanel: JPanel = JPanel(BorderLayout())
    private val tabbedPane: JBTabs

    /**
     * type标签
     */
    @Transient
    private val typeTab: TabInfo
    private val typePanel: JPanel = JPanel(BorderLayout())
    val typeTable: TypeTable = TypeTable()

    /**
     * template标签
     */
    @Transient
    private val templateTab: TabInfo
    private val templatePanel: JBSplitter
    private val templateDescPanel: JPanel = JPanel(BorderLayout())
    private val variableDescPanel: JPanel = JPanel(GridBagLayout())
    private val templateTextPanel: JPanel = JPanel(BorderLayout())
    var templateEditor: Editor

    init {
        // 1.type标签
        typePanel.add(
            ToolbarDecorator.createDecorator(typeTable)
                .setAddAction { typeTable.addRow() }
                .setRemoveAction { typeTable.removeRow() }
                .setEditAction { typeTable.editRow() }
                .setMoveUpAction { typeTable.moveUp() }
                .setMoveDownAction { typeTable.moveDown() }
                .addExtraAction(
                    object : AnAction("Reset", "Reset Default Aliases", AllIcons.Actions.Rollback) {
                        override fun actionPerformed(anActionEvent: AnActionEvent) {
                            typeTable.resetRow()
                        }
                    })
                .createPanel(), BorderLayout.CENTER
        )
        object : DoubleClickListener() {
            override fun onDoubleClick(event: MouseEvent): Boolean {
                return typeTable.editRow()
            }
        }.installOn(typeTable)

        // 2.template标签
        templatePanel = JBSplitter(true, "", 0.25f)
        templatePanel.firstComponent = templateDescPanel
        templatePanel.secondComponent = templateTextPanel

        // 2.1. Template variables description
        val labels: MutableList<JLabel> = ArrayList()
        for (value in TemplateVarType.values()) {
            labels.add(JLabel(value.description))
        }
        val constraints = GridBagConstraints()
        constraints.fill = GridBagConstraints.HORIZONTAL
        constraints.insets = JBUI.insets(2, 10, 0, 0)
        IntStream.range(0, labels.size).forEach { i: Int ->
            constraints.weightx = 1.0
            constraints.gridx = 0
            constraints.gridy = i
            variableDescPanel.add(labels[i], constraints)
        }
        templateDescPanel.add(JLabel("Template variables:"), BorderLayout.NORTH)
        templateDescPanel.add(variableDescPanel, BorderLayout.CENTER)

        // 2.2Template text
        for (value in TemplateVarType.values()) {
            TEMPLATE.addVariable(value.title, "", "", true)
        }
        templateEditor = TemplateEditorUtil.createEditor(false, "", TEMPLATE.createContext())
        TemplateEditorUtil.setHighlighter(templateEditor, HtmlContextType())
        templateTextPanel.add(JLabel("Template text:"), BorderLayout.NORTH)
        templateTextPanel.add(templateEditor.component, BorderLayout.CENTER)

        // tabs
        tabbedPane = JBTabsImpl(null, null, this)
        typeTab = TabInfo(typePanel)
        typeTab.text = "type"
        tabbedPane.addTab(typeTab)

        templateTab = TabInfo(templatePanel)
        templateTab.text = "template"
        tabbedPane.addTab(templateTab)

        // main
        mainPanel.add(JLabel("Personalize your git commit types and templates."), BorderLayout.NORTH)
        mainPanel.add(tabbedPane.component, BorderLayout.CENTER)
    }

    companion object {
        private val TEMPLATE = TemplateImpl("commit-template", "Git")
    }

    override fun dispose() {
    }
}
