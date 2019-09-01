package com.avito.android.test.espresso.action.click

import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.webkit.WebView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.PrecisionDescriber
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.ViewActions.actionWithAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast
import org.hamcrest.Matcher

class ClickAction(
    private val event: Event,
    private val coordinatesProvider: CoordinatesProvider,
    private val precisionDescriber: PrecisionDescriber
) : ViewAction {

    override fun getDescription(): String = event.description()

    override fun getConstraints(): Matcher<View> = isDisplayingAtLeast(90)

    override fun perform(uiController: UiController, view: View) {
        val rootView = view.rootView
        val rootViewAbsoluteCoordinates = IntArray(2)
        rootView.getLocationOnScreen(rootViewAbsoluteCoordinates)

        val absoluteCoordinates = coordinatesProvider.calculateCoordinates(view)
        val relativeCoordinates = FloatArray(2).apply {
            set(0, absoluteCoordinates[0] - rootViewAbsoluteCoordinates[0])
            set(1, absoluteCoordinates[1] - rootViewAbsoluteCoordinates[1])
        }
        val precision = precisionDescriber.describePrecision()

        event.perform(
            uiController = uiController,
            view = view,
            rootView = rootView,
            coordinates = relativeCoordinates,
            precision = precision
        )
    }

    sealed class Event {

        abstract fun perform(
            uiController: UiController,
            view: View,
            rootView: View,
            coordinates: FloatArray,
            precision: FloatArray
        )

        abstract fun description(): String

        object ClickEvent : Event() {

            override fun description(): String = "single click"

            override fun perform(
                uiController: UiController,
                view: View,
                rootView: View,
                coordinates: FloatArray,
                precision: FloatArray
            ) {
                val downEvent = obtainEvent(
                    coordinates = coordinates,
                    precision = precision,
                    event = MotionEvent.ACTION_DOWN
                )
                rootView.dispatchTouchEvent(downEvent)

                uiController.loopMainThreadForAtLeast(ViewConfiguration.getTapTimeout().toLong())

                val upEvent = obtainEvent(
                    coordinates = coordinates,
                    precision = precision,
                    event = MotionEvent.ACTION_UP
                )
                rootView.dispatchTouchEvent(upEvent)

                downEvent.recycle()
                upEvent.recycle()

                uiController.loopMainThreadForAtLeast(ViewConfiguration.getPressedStateDuration().toLong())

                /**
                 * according with [android.support.test.espresso.action.GeneralClickAction.perform]
                 */
                if (view is WebView) {
                    uiController.loopMainThreadForAtLeast(ViewConfiguration.getDoubleTapTimeout().toLong())
                }
            }
        }

        object LongClickEvent : Event() {

            override fun description(): String = "long click"

            override fun perform(
                uiController: UiController,
                view: View,
                rootView: View,
                coordinates: FloatArray,
                precision: FloatArray
            ) {
                val downEvent = obtainEvent(
                    coordinates = coordinates,
                    precision = precision,
                    event = MotionEvent.ACTION_DOWN
                )
                rootView.dispatchTouchEvent(downEvent)

                uiController.loopMainThreadForAtLeast(ViewConfiguration.getLongPressTimeout().toLong())

                val upEvent = obtainEvent(
                    coordinates = coordinates,
                    precision = precision,
                    event = MotionEvent.ACTION_UP
                )
                rootView.dispatchTouchEvent(upEvent)

                downEvent.recycle()
                upEvent.recycle()

                uiController.loopMainThreadForAtLeast(ViewConfiguration.getPressedStateDuration().toLong())

                /**
                 * according with [android.support.test.espresso.action.GeneralClickAction.perform]
                 */
                if (view is WebView) {
                    uiController.loopMainThreadForAtLeast(ViewConfiguration.getDoubleTapTimeout().toLong())
                }
            }
        }
    }
}

internal fun inProcessClickAction(): ViewAction = actionWithAssertions(
    ClickAction(
        coordinatesProvider = GeneralLocation.VISIBLE_CENTER,
        precisionDescriber = Press.FINGER,
        event = ClickAction.Event.ClickEvent
    )
)

internal fun inProcessLongClickAction(): ViewAction = actionWithAssertions(
    ClickAction(
        coordinatesProvider = GeneralLocation.VISIBLE_CENTER,
        precisionDescriber = Press.FINGER,
        event = ClickAction.Event.LongClickEvent
    )
)