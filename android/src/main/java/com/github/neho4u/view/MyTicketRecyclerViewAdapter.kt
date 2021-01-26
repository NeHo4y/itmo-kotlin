package com.github.neho4u.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.neho4u.databinding.FragmentTicketBinding
import com.github.neho4u.model.Ticket
import com.github.neho4u.utils.dateFormatter
import com.github.neho4u.view.TicketFragment.OnListFragmentInteractionListener
import kotlinx.datetime.toJavaLocalDateTime

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
            mLastUpdated.text = item.lastUpdated?.toJavaLocalDateTime()?.format(dateFormatter)
            mSubject.text = item.subject
            mDetails.text = item.detail
            mSeverity.text = item.priority?.name ?: "Priority"
            mStatus.text = item.status?.name ?: "Status"
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
        val mSeverity = mView.vSeverity
        val mStatus = mView.vStatus
        override fun toString() = "${super.toString()} '${mSubject.text}'"
    }
}
