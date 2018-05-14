package com.avito.android.test.page_object

import android.support.test.espresso.Espresso
import android.support.test.espresso.ViewInteraction
import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.SimpleInteractionContext
import com.avito.android.test.action.Actions
import com.avito.android.test.action.ActionsImpl
import com.avito.android.test.checks.Checks
import com.avito.android.test.checks.ChecksImpl
import com.avito.android.test.matcher.NoViewMatcher
import org.hamcrest.Matcher

open class PageObjectElement(
    @Deprecated("don't use this matcher directly") val matcher: Matcher<View>,
    val interactionContext: InteractionContext = SimpleInteractionContext(matcher),
    open val actions: Actions = ActionsImpl(interactionContext),
    open val checks: Checks = ChecksImpl(interactionContext)
) : PageObject(), Actions by actions {

    constructor(matcher: Matcher<View>, interactionContext: InteractionContext) :
            this(
                matcher,
                interactionContext,
                ActionsImpl(interactionContext),
                ChecksImpl(interactionContext)
            )

    constructor(interactionContext: InteractionContext, checks: Checks) :
            this(NoViewMatcher(), interactionContext, ActionsImpl(interactionContext), checks)

    constructor(interactionContext: InteractionContext) :
            this(
                NoViewMatcher(),
                interactionContext,
                ActionsImpl(interactionContext),
                ChecksImpl(interactionContext)
            )

    @Deprecated("remove")
    open val interaction: ViewInteraction
        get() = Espresso.onView(matcher)
}