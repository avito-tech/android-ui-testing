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

interface PageObjectElement : PageObject, Actions {
    @Deprecated("don't use this matcher directly")
    val matcher: Matcher<View>
    val interactionContext: InteractionContext
    val actions: Actions
    val checks: Checks
}

open class ViewElement(
    @Deprecated("don't use this matcher directly") override val matcher: Matcher<View>,
    override val interactionContext: InteractionContext = SimpleInteractionContext(matcher),
    override val actions: Actions = ActionsImpl(interactionContext),
    override val checks: Checks = ChecksImpl(interactionContext)
) : PageObjectElement, Actions by actions {

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
