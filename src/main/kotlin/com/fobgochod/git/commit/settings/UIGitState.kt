package com.fobgochod.git.commit.settings

import com.fobgochod.git.commit.constant.GitConstant
import com.fobgochod.git.commit.domain.SkipCIWord
import com.fobgochod.git.commit.domain.TypeRow
import com.fobgochod.git.commit.domain.ViewMode
import com.intellij.openapi.components.BaseState
import com.intellij.util.xmlb.annotations.OptionTag
import com.intellij.util.xmlb.annotations.Tag
import com.intellij.util.xmlb.annotations.XCollection


class UIGitState : BaseState() {

    @get:Tag("TYPE_ROWS")
    @get:XCollection(style = XCollection.Style.v2)
    var typeRows by list<TypeRow>()

    @get:OptionTag("TYPE_COUNT")
    var typeCount by property(GitConstant.RADIO_BUTTON_TYPE_COUNT)

    @get:OptionTag("SKIP_CI")
    var skipCI by enum(SkipCIWord.SKIP_CI)

    @get:OptionTag("VIEW_MODE")
    var viewMode by enum(ViewMode.Window)

    @get:OptionTag("HIDE_TYPE_GROUP")
    var hideTypeGroup by property(false)

    @get:OptionTag("HIDE_TYPE")
    var hideType by property(false)

    @get:OptionTag("HIDE_SCOPE")
    var hideScope by property(false)

    @get:OptionTag("HIDE_SUBJECT")
    var hideSubject by property(false)

    @get:OptionTag("HIDE_BODY")
    var hideBody by property(false)

    @get:OptionTag("HIDE_WRAP_TEXT")
    var hideWrapText by property(false)

    @get:OptionTag("HIDE_BREAKING")
    var hideBreaking by property(false)

    @get:OptionTag("HIDE_ISSUES")
    var hideIssues by property(false)

    @get:OptionTag("HIDE_SKIP_CI")
    var hideSkipCI by property(false)
}
