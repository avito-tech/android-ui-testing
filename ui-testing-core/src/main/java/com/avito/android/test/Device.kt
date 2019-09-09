package com.avito.android.test

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.permission.PermissionRequester
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.avito.android.test.espresso.action.OrientationChangeAction
import com.avito.android.test.internal.Cache
import com.avito.android.test.internal.SQLiteDB
import com.avito.android.test.internal.SharedPreferences
import com.avito.android.test.page_object.KeyboardElement
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not

/**
 * Abstraction of android phone from user's perspective
 * Contains actions and checks not associated with apps
 */
object Device {

    private val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    val keyboard = KeyboardElement()

    /**
     * Changes device orientation between portrait and landscape
     */
    fun rotate() {
        onView(ViewMatchers.isRoot()).perform(OrientationChangeAction.toggle())
    }

    /**
     * short press on back button, ignore application under test boundaries
     */
    fun pressBack(failTestIfAppUnderTestClosed: Boolean = false) {
        if (failTestIfAppUnderTestClosed) {
            Espresso.pressBack()
        } else {
            Espresso.pressBackUnconditionally()
        }
    }

    /**
     * short press on home button
     */
    fun pressHome() {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).pressHome()
    }

    /**
     * Waits for app's main thread to be idle
     * Used only in screenshot tests as a temp hack. Don't use it in component or functional tests
     * todo consider remove
     */
    fun waitForIdle() {
        onView(ViewMatchers.isRoot()).perform(OrientationChangeAction.toggle())
    }

    fun getLauncherIntentForAppUnderTest(testContext: Context, appContext: Context): Intent {
        return testContext.packageManager.getLaunchIntentForPackage(appContext.packageName)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) // Clear out any previous instances
    }

    fun waitForLauncher(timeout: Long = UITestConfig.activityLaunchTimeoutMilliseconds) {
        with(uiDevice) {
            assertThat(
                "Launcher package name must be not null",
                launcherPackageName,
                CoreMatchers.notNullValue()
            )
            assertTrue(
                "Waiting for launcher screen was exceeded timeout: $timeout milliseconds",
                wait(Until.hasObject(By.pkg(launcherPackageName).depth(0)), timeout)
            )
        }
    }

    fun waitForAppLaunchAndReady(
        appContext: Context,
        timeout: Long = UITestConfig.activityLaunchTimeoutMilliseconds
    ) {
        with(uiDevice) {
            assertTrue(
                "Waiting for application launching was exceeded timeout: $timeout milliseconds",
                wait(Until.hasObject(By.pkg(appContext.packageName).depth(0)), timeout)
            )
        }
    }

    fun killApp(appContext: Context) {
        Runtime.getRuntime().exec(arrayOf("am", "force-stop", appContext.packageName))
    }

    fun grantPermissions(vararg permissions: String) {
        val requester = PermissionRequester()
        requester.addPermissions(*permissions)
        requester.requestPermissions()
    }

    /**
     * WARNING: Currently not working correctly while app is running. Use only on app with no processes alive
     */
    fun clearApplicationData(appContext: Context = ApplicationProvider.getApplicationContext()) {
        Cache(appContext).clear()
        SQLiteDB(appContext).clearAll()
        SharedPreferences(appContext).clear()
    }

    object Push {

        fun openNotification(
            expectedTitle: String,
            timeoutMillis: Long = UITestConfig.openNotificationTimeoutMilliseconds
        ) {
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            device.openNotification()
            assertTrue(
                "Waiting for notification with title: $expectedTitle was exceeded timeout: $timeoutMillis milliseconds",
                device.wait(Until.hasObject(By.text(expectedTitle)), timeoutMillis)
            )
            val titleObject = device.findObject(By.text(expectedTitle))
            assertEquals(
                "Notification has incorrect title",
                expectedTitle,
                titleObject.text
            )
            // it is possible to add a sleep here
            // to let some time to item to be synchronized with device after reject, message, etc
            titleObject.click()
        }

        fun receiveNotification(init: Notification.() -> Unit) {
            val notification = Notification()
            notification.init()
            val command = "am broadcast" +
                    " -a ${notification.intent}" +
                    " -n ${notification.packageName}/${notification.receiverName}" +
                    " --es uri ${notification.uri}" +
                    (notification.phash?.let { " --es phash $it" } ?: "") +
                    " --es notification {\"body\":\"${notification.messageBody}\"}"
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .executeShellCommand(command)
        }

        class Notification {
            var intent: String = "com.google.android.c2dm.intent.RECEIVE"
            var packageName: String = ApplicationProvider.getApplicationContext<Application>().packageName
            var receiverName = "com.google.android.gms.gcm.GcmReceiver"
            var uri: String = ""
            var messageBody: String = ""
                set(value) {
                    assertThat(
                        "Value must not contains spaces",
                        value,
                        not(containsString(" "))
                    )
                    field = value
                }
            var phash: String? = null
        }
    }
}
