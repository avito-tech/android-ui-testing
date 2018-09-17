package com.avito.android.test.page_object

import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.SimpleInteractionContext
import org.hamcrest.Matcher

@Deprecated("Use PageObjectElement")
class TextViewElement(interactionContext: InteractionContext) :
    ViewElement(interactionContext) {

    constructor(matcher: Matcher<View>) : this(SimpleInteractionContext(matcher))
}
