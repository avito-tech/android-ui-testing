package com.avito.android.test.checks

import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.LayoutMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withTagValue
import android.view.View
import com.avito.android.test.matcher.CompoundDrawableMatcher
import com.avito.android.test.matcher.TextViewLinesMatcher
import com.avito.android.test.matcher.WithHintEndingMatcher
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.hamcrest.core.AllOf

class LabelChecksImpl(private val driver: ChecksDriver) : LabelChecks {

    override fun withText(text: String) {
        driver.check(ViewAssertions.matches(ViewMatchers.withText(text)))
    }

    override fun withText(textResId: Int) {
        driver.check(ViewAssertions.matches(ViewMatchers.withText(textResId)))
    }

    override fun withText(matcher: Matcher<String>) {
        driver.check(ViewAssertions.matches(ViewMatchers.withText(matcher)))
    }

    override fun withTextStartingWith(text: String) {
        driver.check(ViewAssertions.matches(ViewMatchers.withText(Matchers.startsWith(text))))
    }

    override fun withTextIgnoringCase(text: String) {
        driver.check(ViewAssertions.matches(ViewMatchers.withText(Matchers.equalToIgnoringCase(text))))
    }

    override fun withEmptyText() {
        driver.check(ViewAssertions.matches(ViewMatchers.withText("")))
    }

    override fun containsText(text: String) {
        driver.check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString(text))))
    }

    override fun displayedWithText(text: String) {
        displayedAndMatchedWith(ViewMatchers.withText(text))
    }

    override fun displayedWithText(textResId: Int) {
        displayedAndMatchedWith(ViewMatchers.withText(textResId))
    }

    override fun displayedWithText(matcher: Matcher<String>) {
        displayedAndMatchedWith(ViewMatchers.withText(matcher))
    }

    override fun displayedWithTextStartingWith(text: String) {
        displayedAndMatchedWith(ViewMatchers.withText(Matchers.startsWith(text)))
    }

    override fun displayedWithTextEndingWith(text: String) {
        displayedAndMatchedWith(ViewMatchers.withText(Matchers.endsWith(text)))
    }

    override fun displayedWithTextIgnoringCase(text: String) {
        displayedAndMatchedWith(ViewMatchers.withText(Matchers.equalToIgnoringCase(text)))
    }

    override fun displayedWithEmptyText() {
        displayedAndMatchedWith(ViewMatchers.withText(""))
    }

    override fun withLinesCount(count: Int) {
        driver.check(ViewAssertions.matches(TextViewLinesMatcher(Matchers.`is`(count))))
    }

    override fun withLinesCount(matcher: Matcher<Int>) {
        driver.check(ViewAssertions.matches(TextViewLinesMatcher(matcher)))
    }

    override fun endsWithHint(hint: String) {
        driver.check(ViewAssertions.matches(WithHintEndingMatcher(hint)))
    }

    override fun hasEllipsizedText() {
        driver.check(ViewAssertions.matches(LayoutMatchers.hasEllipsizedText()))
    }

    override fun hasNotEllipsizedText() {
        driver.check(ViewAssertions.matches(Matchers.not(LayoutMatchers.hasEllipsizedText())))
    }

    override fun hasTag(tag: Any) {
        displayedAndMatchedWith(withTagValue(equalTo(tag)))
    }

    override fun withIcons(
        @DrawableRes left: Int?,
        @DrawableRes top: Int?,
        @DrawableRes right: Int?,
        @DrawableRes bottom: Int?,
        @ColorInt tint: Int?
    ) {
        driver.check(ViewAssertions.matches(CompoundDrawableMatcher(left, top, right, bottom, tint)))
    }

    private fun displayedAndMatchedWith(matcher: Matcher<View>) {
        driver.check(
            ViewAssertions.matches(
                AllOf.allOf(
                    ViewMatchers.isDisplayed(),
                    matcher
                )
            )
        )
    }
}