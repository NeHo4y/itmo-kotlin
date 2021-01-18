package com.github.neho4u.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.neho4u.databinding.ATicketViewBinding
import com.github.neho4u.databinding.FragmentTicketBinding
import com.github.neho4u.model.Ticket
import com.github.neho4u.view.TicketFragment.OnListFragmentInteractionListener

class MyTicketRecyclerViewAdapter(
    private var mValues: List<Ticket>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyTicketRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener = View.OnClickListener { v ->
        val item = v.tag as Ticket
        // Notify the active callbacks interface that an item has been selected.
        mListener?.onListFragmentInteraction(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
        val binding = FragmentTicketBinding.inflate(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.apply {
            mIdView.text = item.id.toString()
            mClient.text = item.displayClient
            mLastUpdated.text = item.lastUpdated.toString()
            mSubject.text = item.subject
            mDetails.text = item.detail
        }

        with(holder.mView.root) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    fun updateData(data: List<Ticket>) {
        mValues = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = mValues.size

    inner class ViewHolder(val mView: FragmentTicketBinding) : RecyclerView.ViewHolder(mView.root) {
        val mIdView = mView.tvTickId
        val mClient = mView.tvTickClient
        val mLastUpdated = mView.tvTickLastUpdated
        val mSubject = mView.tvTickSubj
        val mDetails = mView.tvTickDet

        override fun toString() = "${super.toString()} '${mSubject.text}'"
    }
}
