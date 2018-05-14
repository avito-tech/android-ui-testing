package com.avito.android.test

import android.content.Context
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.runner.permission.PermissionRequester
import android.support.test.uiautomator.By
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.Until
import com.avito.android.test.espresso.EspressoActions
import com.avito.android.test.espresso.action.OrientationChangeAction
import com.avito.android.test.internal.Cache
import com.avito.android.test.internal.SQLiteDB
import com.avito.android.test.internal.SharedPreferences
import com.avito.android.test.page_object.KeyboardElement
import com.avito.android.test.page_object.PageObjectElement
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not

/**
 * Abstraction of android smartphone from user's perspective
 * Contains actions and checks not associated with apps
 */
object Device {

    private const val DEFAULT_LAUNCH_TIMEOUT_MS = 10000L

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private val rootElement: PageObjectElement
        get() = PageObjectElement(ViewMatchers.isRoot())

    val keyboard = KeyboardElement()

    /**
     * Changes device orientation between portrait and landscape
     */
    fun rotate() {
        rootElement.interaction.waitToPerform(OrientationChangeAction.toggle())
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
        rootElement.interaction.perform(EspressoActions.waitForIdle())
    }

    fun getLauncherIntentForAppUnderTest(testContext: Context, appContext: Context): Intent {
        return testContext.packageManager.getLaunchIntentForPackage(appContext.packageName)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) // Clear out any previous instances
    }

    fun waitForLauncher(timeout: Long = DEFAULT_LAUNCH_TIMEOUT_MS) {
        with(device) {
            assertThat(launcherPackageName, CoreMatchers.notNullValue())
            assertTrue(wait(Until.hasObject(By.pkg(launcherPackageName).depth(0)), timeout))
        }
    }

    fun waitForAppLaunchAndReady(appContext: Context, timeout: Long = DEFAULT_LAUNCH_TIMEOUT_MS) {
        with(device) {
            assertTrue(wait(Until.hasObject(By.pkg(appContext.packageName).depth(0)), timeout))
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
    fun clearApplicationData(appContext: Context = InstrumentationRegistry.getTargetContext()) {
        Cache(appContext).clear()
        SQLiteDB(appContext).clearAll()
        SharedPreferences(appContext).clear()
    }

    object Push {

        fun openNotification(timeoutMillis: Long = 30_000) {
            val expectedTitle = "Avito (DEV)"
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            device.openNotification()
            assertTrue(device.wait(Until.hasObject(By.text(expectedTitle)), timeoutMillis))
            val titleObject = device.findObject(By.text(expectedTitle))
            assertEquals(expectedTitle, titleObject.text)
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

            var intent = "com.google.android.c2dm.intent.RECEIVE"
            var packageName = "com.avito.android.dev"
            var receiverName = "com.google.android.gms.gcm.GcmReceiver"
            var uri = "ru.avito://1/main"
            var messageBody: String = ""
                set(value) {
                    assertThat(
                        "Передаваемая строка не может содержать символы пробела",
                        value, not(containsString(" "))
                    )
                    field = value
                }
            var phash: String? = null
        }
    }
}