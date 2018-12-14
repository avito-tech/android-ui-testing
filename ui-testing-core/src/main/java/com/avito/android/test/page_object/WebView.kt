package com.avito.android.test.page_object

import android.support.test.espresso.web.assertion.WebViewAssertions.webMatches
import android.support.test.espresso.web.model.Atom
import android.support.test.espresso.web.model.Atoms.getCurrentUrl
import android.support.test.espresso.web.model.ElementReference
import android.support.test.espresso.web.sugar.Web
import android.view.View
import com.avito.android.test.action.WebElementActions
import com.avito.android.test.checks.WebElementChecks
import org.hamcrest.Matcher
import org.hamcrest.Matchers.containsString
import java.util.concurrent.TimeUnit

open class WebView(private val webViewMatcher: Matcher<View>) : PageObject {

    private val interaction: Web.WebInteraction<Void>
        get() = Web.onWebView(webViewMatcher)

    val checks = WebViewChecksImpl()

    fun withElement(
        elementMatcher: Atom<ElementReference>,
        timeoutSeconds: Long = WEB_ELEMENT_TIMEOUT_SECONDS
    ) = WebViewElement(elementMatcher, timeoutSeconds)

    interface WebViewChecks {

        fun withUrl(uri: String)
    }

    inner class WebViewChecksImpl : WebViewChecks {

        override fun withUrl(uri: String) {
            interaction.check(webMatches(getCurrentUrl(), containsString(uri)))
        }
    }

    inner class WebViewElement(
        private val elementMatcher: Atom<ElementReference>,
        private val timeoutSeconds: Long
    ) : PageObject {

        val actions: WebElementActions
            get() = WebElementActions(
                interaction.withTimeout(timeoutSeconds, TimeUnit.SECONDS).withElement(
                    elementMatcher
                )
            )

        val checks: WebElementChecks
            get() = WebElementChecks(
                interaction.withTimeout(timeoutSeconds, TimeUnit.SECONDS).withElement(
                    elementMatcher
                )
            )
    }
}

private const val WEB_ELEMENT_TIMEOUT_SECONDS = 5L
