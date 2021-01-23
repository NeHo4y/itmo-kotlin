package com.github.neho4u.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.neho4u.R
import com.github.neho4u.model.IdWithName
import com.github.neho4u.model.Placeholder

/**
 * A SpinnerAdapter which does not show the value of the initial selection initially,
 * but an initialText.
 * To use the spinner with initial selection instead call notifyDataSetChanged().
 */
class SpinnerAdapterWithInitialText(
    context: Context,
    private val resource: Int,
    objects: List<IdWithName>,
    private val initialText: String = "Please select"
) : ArrayAdapter<IdWithName>(context, resource, objects) {
    private var initialTextWasShown = false

    fun update(list: List<IdWithName>) {
        clear()
        addAll(list)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): IdWithName? {
//        Log.w("SpinnerAdapterWithInitialText", "getItem: $position")
        return if (position == 0) {
            Placeholder
        } else {
            super.getItem(position - 1)
        }
    }

    override fun getCount(): Int {
        return super.getCount() + 1
    }

    override fun getView(position: Int, recycle: View?, container: ViewGroup): View {
//        Log.w("SpinnerAdapterWithInitialText", "position: $position")
        return if (position != 0) {
            super.getView(position, recycle, container)
        } else {
            initialTextWasShown = true
            LayoutInflater.from(context).inflate(resource, container, false).apply {
                with(this as TextView) {
                    text = initialText
                    setTextColor(getColor(android.R.color.darker_gray))
                }
            }
        }
    }

    private fun View.getColor(color: Int) = resources.getColor(color, context.theme)
}
