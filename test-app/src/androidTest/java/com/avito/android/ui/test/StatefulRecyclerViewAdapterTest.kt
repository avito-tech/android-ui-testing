package com.avito.android.ui.test

import com.avito.android.ui.StatefulRecyclerViewAdapterActivity
import org.junit.Rule
import org.junit.Test

class StatefulRecyclerViewAdapterTest {

    @get:Rule
    val rule = screenRule<StatefulRecyclerViewAdapterActivity>(launchActivity = true)

    /**
     * For finding element inside recycler view by view matching we have to render all items
     * inside recycler view. That rendering may affect application logic (production code may
     * cause some side effects inside recycler view item binding code).
     *
     * We can't avoid that behavior. For that reason, we've created special tag for
     * ViewHolder itemView, that can be used by production code for detecting fake rendering
     * and avoiding any side effects.
     *
     * For more details look at recycler view adapter inside StatefulRecyclerViewAdapterActivity
     */
    @Test
    fun adapterState_notAffectedByDynamicRecyclerViewElementFindingLogic_whenProductionCodeAvoidToUsingSideEffectsForFakeViewHolder() {
        Screen.statefulRecyclerViewAdapterScreen.list.cellWithTitle("60").scrollTo()
        Screen.statefulRecyclerViewAdapterScreen.list.cellWithTitle("1").scrollTo()
        Screen.statefulRecyclerViewAdapterScreen.list.cellWithTitle("60").scrollTo()
        Screen.statefulRecyclerViewAdapterScreen.list.cellWithTitle("1").scrollTo()

        Screen.statefulRecyclerViewAdapterScreen.list.cellWithTitle("1")
            .title2.checks.displayedWithText("3")
    }
}