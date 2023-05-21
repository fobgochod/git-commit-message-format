package com.fobgochod.git.commit.settings.v1

import javax.swing.JPanel

interface GitComponent {

    fun getComponent(): JPanel

    fun isModified(): Boolean

    fun apply() {}

    fun reset() {}
}
