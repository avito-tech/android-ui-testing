package com.avito.android.test.page_object

import android.support.test.espresso.assertion.ViewAssertions
import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.SimpleInteractionContext
import com.avito.android.test.action.ActionsDriver
import com.avito.android.test.checks.Checks
import com.avito.android.test.checks.ChecksDriver
import com.avito.android.test.checks.ChecksImpl
import com.avito.android.test.espresso.action.ViewPagersFlipAction
import com.avito.android.test.espresso.action.ViewPagersSelectAction
import com.avito.android.test.matcher.ViewPagersSelectMatcher
import com.avito.android.test.matcher.ViewPagersTabsCountMatcher
import org.hamcrest.Matcher

class ViewPagerElement(interactionContext: InteractionContext) :
    BasePageObjectElement(interactionContext),
    ViewPagerActions by ViewPagerActionsImpl(interactionContext) {

    constructor(matcher: Matcher<View>) : this(SimpleInteractionContext(matcher))

    override val checks: ViewPagerChecks = ViewPagerChecksImpl(interactionContext)
}

interface ViewPagerActions {

    fun toRight()

    fun toLeft()

    fun select(position: Int)
}

class ViewPagerActionsImpl(private val actionsDriver: ActionsDriver) : ViewPagerActions {

    override fun toRight() {
        actionsDriver.perform(ViewPagersFlipAction(ViewPagersFlipAction.Direction.RIGHT))
    }

    override fun toLeft() {
        actionsDriver.perform(ViewPagersFlipAction(ViewPagersFlipAction.Direction.LEFT))
    }

    override fun select(position: Int) {
        actionsDriver.perform(ViewPagersSelectAction(position))
    }
}

interface ViewPagerChecks : Checks {

    fun withSelectedPosition(position: Int)

    fun withTabsCount(count: Int)
}

class ViewPagerChecksImpl(private val driver: ChecksDriver) : ViewPagerChecks,
    Checks by ChecksImpl(driver) {

    override fun withSelectedPosition(position: Int) {
        driver.check(ViewAssertions.matches(ViewPagersSelectMatcher(position)))
    }

    override fun withTabsCount(count: Int) {
        driver.check(ViewAssertions.matches(ViewPagersTabsCountMatcher(count)))
    }
}
