package com.fobgochod.git.commit.view

import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.util.GitBundle.message
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class TypeDialog(
    title: String,
    private val typeRow: TypeRow
) : DialogWrapper(true) {

    private lateinit var nameField: JBTextField

    private val root: DialogPanel = panel {
        row(message("settings.type.table.column.1")) {
            textField()
                .align(AlignX.FILL)
                .applyToComponent { nameField = this }
                .bindText(typeRow::name)
        }
        row {
            label(message("settings.type.table.column.2"))
                .align(AlignY.TOP)
                .gap(RightGap.SMALL)
            textArea()
                .rows(2)
                .align(Align.FILL)
                .applyToComponent { lineWrap = true }
                .bindText(typeRow::description)
        }.layout(RowLayout.PARENT_GRID).resizableRow()
    }.apply {
        preferredFocusedComponent = nameField
        withPreferredWidth(500)
    }

    init {
        setTitle(title)
        init()
    }

    override fun createCenterPanel(): JComponent {
        return root
    }
}
