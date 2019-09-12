package com.avito.android.ui.test

import com.avito.android.test.UITestConfig.ClickType.EspressoClick
import com.avito.android.test.util.ClicksTypeRule
import com.avito.android.test.util.ChangeClickType
import com.avito.android.ui.TextElementActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test

class TextElementsTest {

    @get:Rule
    val rule = screenRule<TextElementActivity>()

    @get:Rule
    val clicksRule = ClicksTypeRule(EspressoClick(EspressoClick.ClickRollbackPolicy.DoNothing))

    @Test
    @ChangeClickType
    fun clicksOnLink_espressoClicks() {
        rule.launchActivity(null)
        Screen.textsElements.textView.clickOnLink()
        MatcherAssert.assertThat(rule.activity.count, CoreMatchers.equalTo(1))
    }

    @Test
    @ChangeClickType
    fun clicksOnText_espressoClicks() {
        rule.launchActivity(null)
        Screen.textsElements.textView.clickOnText("link")
        MatcherAssert.assertThat(rule.activity.count, CoreMatchers.equalTo(1))
    }

    @Test
    @ChangeClickType
    fun clicksOnSeveralLinks_espressoClicks() {
        rule.launchActivity(null)
        Screen.textsElements.textViewLong.clickOnLink(0)
        Screen.textsElements.textViewLong.clickOnLink(1)
        Screen.textsElements.textViewLong.clickOnLink(2)
        MatcherAssert.assertThat(rule.activity.count, CoreMatchers.equalTo(3))
    }

    @Test
    fun clicksOnLink() {
        rule.launchActivity(null)
        Screen.textsElements.textView.clickOnLink()
        MatcherAssert.assertThat(rule.activity.count, CoreMatchers.equalTo(1))
    }

    @Test
    fun clicksOnText() {
        rule.launchActivity(null)
        Screen.textsElements.textView.clickOnText("link")
        MatcherAssert.assertThat(rule.activity.count, CoreMatchers.equalTo(1))
    }

    @Test
    fun clicksOnSeveralLinks() {
        rule.launchActivity(null)
        Screen.textsElements.textViewLong.clickOnLink(0)
        Screen.textsElements.textViewLong.clickOnLink(1)
        Screen.textsElements.textViewLong.clickOnLink(2)
        MatcherAssert.assertThat(rule.activity.count, CoreMatchers.equalTo(3))
    }
}