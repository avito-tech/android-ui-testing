package com.avito.android.test.page_object

import android.support.test.espresso.web.model.Atom
import android.support.test.espresso.web.model.ElementReference
import android.support.test.espresso.web.sugar.Web
import android.view.View
import com.avito.android.test.action.WebElementActions
import com.avito.android.test.checks.WebElementChecks
import java.util.concurrent.TimeUnit
import org.hamcrest.Matcher

class WebViewElement(
    private val webViewMatcher: Matcher<View>,
    private val elementMatcher: Atom<ElementReference>
) :
    PageObject() {

    val actions: WebElementActions
        get() = WebElementActions(
            Web.onWebView(webViewMatcher).withTimeout(5, TimeUnit.SECONDS).withElement(
                elementMatcher
            )
        )

    val checks: WebElementChecks
        get() = WebElementChecks(
            Web.onWebView(webViewMatcher).withTimeout(5, TimeUnit.SECONDS).withElement(
                elementMatcher
            )
        )
}
