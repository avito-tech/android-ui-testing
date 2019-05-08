package com.avito.android.ui.test

import com.avito.android.ui.StateFullRecyclerViewAdapterActivity
import org.junit.Rule
import org.junit.Test

class StateFullRecyclerViewAdapterTest {

    @get:Rule
    val rule = screenRule<StateFullRecyclerViewAdapterActivity>(launchActivity = true)

    /**
     * For finding element inside recycler view by view matching we have to render all items
     * inside recycler view. That rendering may affect application login (production code may
     * execute some side effects inside recycler view item binding code).
     *
     * We can't avoid that behaviour. For that reason, we've created special tag for
     * ViewHolder itemView, that can be used by production code for detecting fake rendering
     * and avoiding any side effects.
     *
     * For more details look at recycler view adapter inside StateFullRecyclerViewAdapterActivity
     */
    @Test
    fun adapterState_notAffectedByDynamicRecyclerViewElementFindingLogic_whenProductionCodeAvoidToUsingSideEffectsForFakeViewHolder() {
        Screen.stateFullRecyclerViewAdapterScreen.list.cellWithTitle("60").scrollTo()
        Screen.stateFullRecyclerViewAdapterScreen.list.cellWithTitle("1").scrollTo()
        Screen.stateFullRecyclerViewAdapterScreen.list.cellWithTitle("60").scrollTo()
        Screen.stateFullRecyclerViewAdapterScreen.list.cellWithTitle("1").scrollTo()

        Screen.stateFullRecyclerViewAdapterScreen.list.cellWithTitle("1")
            .title2.checks.displayedWithText("3")
    }
}