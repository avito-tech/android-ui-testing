package com.avito.android.test.page_object

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import android.widget.RatingBar
import com.avito.android.test.InteractionContext
import com.avito.android.test.SimpleInteractionContext
import com.avito.android.test.action.ActionsDriver
import com.avito.android.test.checks.Checks
import com.avito.android.test.checks.ChecksDriver
import com.avito.android.test.checks.ChecksImpl
import junit.framework.AssertionFailedError
import org.hamcrest.Matcher

class RatingBarElement(
    interactionContext: InteractionContext
) : ViewElement(interactionContext),
    RatingBarActions by RatingBarActionsImpl(interactionContext) {

    constructor(matcher: Matcher<View>) : this(SimpleInteractionContext(matcher))

    override val checks: RatingBarChecks = RatingBarChecksImpl(interactionContext)
}

interface RatingBarActions {

    fun setRating(rating: Float)
}

class RatingBarActionsImpl(private val driver: ActionsDriver) : RatingBarActions {

    override fun setRating(rating: Float) {
        driver.perform(SetRatingAction(rating))
    }
}

interface RatingBarChecks : Checks {

    fun withRating(rating: Float)
}

class RatingBarChecksImpl(
    private val driver: ChecksDriver
) : RatingBarChecks,
    Checks by ChecksImpl(driver) {

    override fun withRating(rating: Float) {
        driver.check(ViewAssertion { view, _ ->
            when (view) {
                is RatingBar -> {
                    if (view.rating != rating) {
                        throw AssertionFailedError(
                            "Current rating is: ${view.rating}. Checked is $rating"
                        )
                    }
                }
                else -> throw AssertionFailedError("Matched view with is not RatingBar")
            }
        })
    }
}

class SetRatingAction(private val rating: Float) : ViewAction {

    override fun getConstraints(): Matcher<View> =
        ViewMatchers.isAssignableFrom(RatingBar::class.java)

    override fun getDescription(): String = "Set rating for RatingBar"

    override fun perform(uiController: UiController, view: View) {
        (view as RatingBar).rating = rating
        view.numStars
    }
}
