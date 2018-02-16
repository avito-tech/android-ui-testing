package com.avito.android.test.page_object

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.actionWithAssertions
import android.support.test.espresso.action.ViewActions.clearText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.SimpleInteractionContext
import com.avito.android.test.action.Actions
import com.avito.android.test.action.ActionsImpl
import com.avito.android.test.action.FieldActions
import com.avito.android.test.checks.Checks
import com.avito.android.test.checks.ChecksImpl
import com.avito.android.test.checks.TextFieldHintChecks
import com.avito.android.test.espresso.action.SafeTypeTextAction
import com.avito.android.test.espresso.action.ScrollToIfPossibleAction
import org.hamcrest.Matcher

open class TextField(interactionContext: InteractionContext) :
    PageObjectElement(interactionContext),
    FieldActions by TextFieldActionImpl(interactionContext) {

    constructor(matcher: Matcher<View>) : this(SimpleInteractionContext(matcher))

    override val checks: TextFieldChecks = TextFieldChecksImpl(interactionContext)
    // do not replace with `val`, otherwise it will not be possible to specify explicitly text typing heuristic in someone's screen in future
    var doNotUseReplace: Boolean = false
}

class TextFieldActionImpl(private val interactionContext: InteractionContext) :
    FieldActions,
    Actions by ActionsImpl(interactionContext) {

    override fun write(text: String) {
        clear()
        interactionContext.perform(
            actionWithAssertions(ScrollToIfPossibleAction()),
            actionWithAssertions(SafeTypeTextAction(text, true))
        )
        Espresso.closeSoftKeyboard()
    }

    override fun clear() {
        interactionContext.perform(
            actionWithAssertions(ScrollToIfPossibleAction()),
            clearText()
        )
    }

    override fun writeAndPressImeAction(text: String) {
        interactionContext.perform(
            actionWithAssertions(ScrollToIfPossibleAction()),
            actionWithAssertions(SafeTypeTextAction(text, true)),
            ViewActions.pressImeActionButton()
        )
    }

    override fun pressImeAction() {
        interactionContext.perform(
            actionWithAssertions(ScrollToIfPossibleAction()),
            ViewActions.pressImeActionButton()
        )
    }

    override fun append(text: String) {
        interactionContext.perform(
            ViewActions.actionWithAssertions(ScrollToIfPossibleAction()),
            ViewActions.actionWithAssertions(SafeTypeTextAction(text, true, true))
        )
    }
}

interface TextFieldChecks : Checks, TextFieldHintChecks {

    override fun withHintText(text: String)
}

class TextFieldChecksImpl(private val interactionContext: InteractionContext) :
    TextFieldChecks,
    Checks by ChecksImpl(interactionContext) {

    override fun withHintText(text: String) {
        interactionContext.check(matches(ViewMatchers.withHint(text)))
    }
}