package com.github.neho4u.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.neho4u.R
import com.github.neho4u.controller.TicketController
import com.github.neho4u.controller.TicketInterface
import com.github.neho4u.databinding.FragmentTicketListBinding
import com.github.neho4u.model.Ticket
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [TicketFragment.OnListFragmentInteractionListener] interface.
 */
class TicketFragment : Fragment(), TicketInterface {
    private var _binding: FragmentTicketListBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun ticketError(error: String) {
        activity?.runOnUiThread {
            listener?.setProgressVisibility(false)
            Log.d("TicketFragment", "Failed to load ticket: $error")
            Snackbar.make(binding.list, error, Snackbar.LENGTH_LONG).apply {
                view.findViewById<TextView>(R.id.snackbar_text).apply {
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
                    compoundDrawablePadding = resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
                }
            }.show()
        }
    }

    override fun ticketLoadResult(result: Ticket) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    private var ticketType = 1
    private var listener: OnListFragmentInteractionListener? = null

    private var ticketArray: List<Ticket> = listOf()

    private lateinit var tController: TicketController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            ticketType = it.getInt(ARG_TICKET_TYPE)
        }

        tController = TicketController(this)
        startTicketRefresh()
    }

    fun startTicketRefresh() {
        listener?.setProgressVisibility(true)
        if (this.view != null) {
            with(binding.list.adapter as MyTicketRecyclerViewAdapter) {
                updateData(listOf())
                Log.d("TicketFragment", "Updated data in adapter")
            }
        }
        when (ticketType) {
            TYPE_FOLLOWED -> GlobalScope.launch(Dispatchers.Default) { tController.refreshMyTickets() }
            TYPE_ALL -> GlobalScope.launch(Dispatchers.Default) { tController.refreshAllTickets() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicketListBinding.inflate(inflater, container, false)

        val pullToRefresh: SwipeRefreshLayout = binding.pullToRefresh
        pullToRefresh.setOnRefreshListener {
            startTicketRefresh() // your code
            pullToRefresh.isRefreshing = false
        }
        return with(binding) {
            list.adapter = MyTicketRecyclerViewAdapter(ticketArray, listener)
            list.layoutManager = LinearLayoutManager(list.context)
            root
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun ticketRefreshResult(result: List<Ticket>) {
        activity?.runOnUiThread {
            listener?.setProgressVisibility(false)
            with(
                binding.list.adapter as MyTicketRecyclerViewAdapter
            ) {
                updateData(result)
                Log.d("TicketFragment", "Updated data in adapter")
            }
        }
        this.ticketArray = result

        Log.d("TicketFragment", "Got result with num items: " + result.size)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(ticket: Ticket?)
        fun setProgressVisibility(visible: Boolean)
    }

    companion object {
        const val ARG_TICKET_TYPE = "ticket-type"

        const val TYPE_FOLLOWED = 1
        const val TYPE_ALL = 2

        @JvmStatic
        fun newInstance(ticketListType: Int) =
            TicketFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TICKET_TYPE, ticketListType)
                }
            }
    }
}
