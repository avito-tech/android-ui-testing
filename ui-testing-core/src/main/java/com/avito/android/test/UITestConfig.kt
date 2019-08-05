package com.avito.android.test

import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.PerformException
import com.avito.android.test.interceptor.ActionInterceptor
import com.avito.android.test.interceptor.AssertionInterceptor
import java.util.concurrent.TimeUnit

object UITestConfig {

    /**
     * global registry for [ActionInterceptor]'s
     */
    val actionInterceptors = mutableListOf<ActionInterceptor>()

    /**
     * global registry for [AssertionInterceptor]'s
     */
    val assertionInterceptors = mutableListOf<AssertionInterceptor>()

    /**
     * Changing this value will affect all subsequent actions/checks wait frequency
     */
    var waiterFrequencyMs: Long = 50L

    /**
     * Changing this value will affect all subsequent actions/checks wait timeout
     */
    var waiterTimeoutMs: Long = TimeUnit.SECONDS.toMillis(2)

    var activityLaunchTimeoutMilliseconds: Long = TimeUnit.SECONDS.toMillis(10)

    var openNotificationTimeoutMilliseconds: Long = TimeUnit.SECONDS.toMillis(30)

    var clicksType: ClickType = ClickType.InProcessClick

    /**
     * Exceptions to be waited for; any unregistered exceptions will be propagated
     */
    var waiterAllowedExceptions = setOf(
        PerformException::class.java,
        NoMatchingViewException::class.java,
        AssertionError::class.java
    )

    sealed class ClickType {
        /**
         * Use default espresso clicks and long clicks
         */
        class EspressoClick(val rollbackPolicy: ClickRollbackPolicy) : ClickType() {
            /**
             * Because of clicks implementation inside Espresso sometimes clicks can be interpreted
             * as long clicks. Here we have several options to handle it.
             *
             * https://stackoverflow.com/questions/32330671/android-espresso-performs-longclick-instead-of-click
             */
            sealed class ClickRollbackPolicy {
                object DoNothing : ClickRollbackPolicy()
                object TryOneMoreClick : ClickRollbackPolicy()
                object Fail : ClickRollbackPolicy()
            }
        }

        /**
         * Use clicks and long clicks implementation without IPC.
         *
         * Advantages:
         *  - Can click on moving objects (espresso has problems with clicking on moving objects, because
         *    it takes some time to touch after coordinates calculating).
         *  - Doesn't have "misinterpret clicks as long clicks" problem.
         *
         * Disadvantages:
         *  - Can click through any system elements on the screen. It applies clicks directly on root
         *    view of our application. Because of it, crash or permission dialogs can be ignored by
         *    tests.
         *  - Can click through separate decor view of our application. Sometimes we have multiple
         *    decor view in application (for example, when we have toolbar overflow menu). And that
         *    kind of click implementation can click through it.
         */
        object InProcessClick : ClickType()
    }
}
