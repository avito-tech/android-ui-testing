package com.avito.android.test.page_object

import android.support.test.espresso.assertion.ViewAssertions
import com.avito.android.test.InteractionContext
import com.avito.android.test.checks.*
import com.avito.android.test.matcher.TextInputLayoutErrorMatcher
import com.avito.android.test.matcher.TextInputLayoutHintMatcher
import com.avito.android.test.matcher.TextInputPasswordVisibilityMatcher
import org.hamcrest.Matchers.`is`

class TextInputElement(interactionContext: InteractionContext) : PageObjectElement(interactionContext) {

    override val checks: TextInputChecks = TextInputChecks(interactionContext)
}

class TextInputChecks(private val interactionContext: InteractionContext) : Checks by ChecksImpl(interactionContext),
        PasswordFieldChecks,
        TextFieldErrorChecks,
        TextFieldHintChecks {

    override fun withErrorText(text: String) {
        interactionContext.check(ViewAssertions.matches(TextInputLayoutErrorMatcher(`is`(text))))
    }

    override fun withHintText(text: String) {
        interactionContext.check(ViewAssertions.matches(TextInputLayoutHintMatcher(`is`(text))))
    }

    override fun isPasswordVisible() {
        interactionContext.check(ViewAssertions.matches(TextInputPasswordVisibilityMatcher(`is`(true))))
    }

    override fun isPasswordHidden() {
        interactionContext.check(ViewAssertions.matches(TextInputPasswordVisibilityMatcher(`is`(false))))
    }
}