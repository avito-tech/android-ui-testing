package com.avito.android.test.page_object

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import com.avito.android.test.InteractionContext
import com.avito.android.test.SimpleInteractionContext
import com.avito.android.test.action.Actions
import com.avito.android.test.action.ActionsImpl
import com.avito.android.test.checks.Checks
import com.avito.android.test.checks.ChecksImpl
import com.avito.android.test.matcher.NoViewMatcher
import org.hamcrest.Matcher

abstract class PageObject {
    open val interactionContext: InteractionContext = SimpleInteractionContext(isRoot())

    protected inline fun <reified T> element(matcher: Matcher<View>): T =
        T::class.java.getConstructor(InteractionContext::class.java)
            .newInstance(interactionContext.provideChildContext(matcher))

    protected inline fun <reified T> element(): T = T::class.java.getConstructor().newInstance()
}

abstract class PageObjectElement : PageObject(), Actions {
    @Deprecated("don't use this matcher directly")
    abstract val matcher: Matcher<View>
    abstract val actions: Actions
    abstract val checks: Checks
}

open class ViewElement(
    @Deprecated("don't use this matcher directly")
    override val matcher: Matcher<View>,
    override val interactionContext: InteractionContext = SimpleInteractionContext(matcher),
    override val actions: Actions = ActionsImpl(interactionContext),
    override val checks: Checks = ChecksImpl(interactionContext)
) : PageObjectElement(), Actions by actions {

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
}
