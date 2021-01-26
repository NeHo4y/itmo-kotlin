package com.github.neho4u.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.github.neho4u.R
import com.github.neho4u.controller.UserDataController
import com.github.neho4u.databinding.AMainDrawerBinding
import com.github.neho4u.model.FeedbackFilter
import com.github.neho4u.model.Ticket
import com.github.neho4u.shared.model.comment.CommentCreationDto
import com.github.neho4u.shared.model.user.UserData
import com.github.neho4u.shared.model.user.UserRole
import com.github.neho4u.utils.AndroidTokenProvider
import com.github.neho4u.utils.Client
import com.github.neho4u.utils.IShowError
import com.github.neho4u.view.settings.SettingsActivity
import com.google.android.material.snackbar.Snackbar
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DrawerView : AppCompatActivity(), TicketFragment.OnListFragmentInteractionListener, IShowError {

    private var menuAddFeedback: MenuItem? = null
    private var menuSearchFilter: MenuItem? = null
    private lateinit var mainDrawerBinding: AMainDrawerBinding
    private lateinit var tvHeaderUser: TextView
    private val userDataController = UserDataController()
    private var feedbackFilter = FeedbackFilter()
    private var currentUser: UserData? = null

    override fun getFilter() = feedbackFilter

    override fun onListFragmentInteraction(ticket: Ticket?) {
        val i = Intent(this, TicketView::class.java)
        i.putExtra(TicketView.ARG_TICKET_ID, ticket?.id)
        startActivity(i)
    }

    override fun setProgressVisibility(visible: Boolean) {
        menuAddFeedback?.isEnabled = !visible
        menuSearchFilter?.isEnabled = !visible
    }

    private var lastTranslate = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainDrawerBinding = DataBindingUtil.setContentView(this, R.layout.a_main_drawer)
        setSupportActionBar(mainDrawerBinding.drawerToolbar)

        currentUser = AndroidTokenProvider.getUserData()

        if (currentUser?.role == UserRole.ADMIN) {
            val actionbar: ActionBar? = supportActionBar
            actionbar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_menu)
            }
        }

        mainDrawerBinding.navView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            mainDrawerBinding.drawerLayout.closeDrawers()

            // update the UI based on the item selected
            handleNavSelection(menuItem)
        }

        mainDrawerBinding.drawerLayout.addDrawerListener(
            object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    // Respond when the drawer's position changes
                    val moveFactor = mainDrawerBinding.drawerLayout.width * slideOffset / 4
                    val anim = TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f)
                    anim.duration = 0
                    anim.fillAfter = true
                    mainDrawerBinding.contentFrameParent.startAnimation(anim)

                    lastTranslate = moveFactor
                }

                override fun onDrawerOpened(drawerView: View) {
                    // Respond when the drawer is opened
                }

                override fun onDrawerClosed(drawerView: View) {
                    // Respond when the drawer is closed
                }

                override fun onDrawerStateChanged(newState: Int) {
                    // Respond when the drawer motion state changes
                }
            }
        )

        if (currentUser?.role == UserRole.USER) {
            mainDrawerBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            mainDrawerBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED)
        }
        mainDrawerBinding.navView.visibility =
            if (currentUser?.role == UserRole.ADMIN) {
                View.VISIBLE
            } else {
                View.GONE
            }
        tvHeaderUser = mainDrawerBinding.navView
            .getHeaderView(0)
            .findViewById(R.id.tv_header_user)
        updateTechInfo()
        mainDrawerBinding.navView.menu.findItem(R.id.dm_all_tickets)?.isVisible =
            currentUser?.role == UserRole.ADMIN
        mainDrawerBinding.navView.setCheckedItem(R.id.dm_my_tickets)
        handleNavSelection(mainDrawerBinding.navView.checkedItem)
    }

    override fun onResume() {
        super.onResume()
        updateTechInfo()
    }

    private fun handleNavSelection(item: MenuItem?): Boolean {
        Log.d("DrawerViewLog", "Selected item " + item?.title)

        return when (item?.itemId) {
            R.id.dm_my_tickets -> {
                mainDrawerBinding.drawerToolbar.title = getString(R.string.my_tickets)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, TicketFragment.newInstance(TicketFragment.TYPE_FOLLOWED))
                    .commit()
                true
            }
            R.id.dm_all_tickets -> {
                mainDrawerBinding.drawerToolbar.title = getString(R.string.all_tickets)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, TicketFragment.newInstance(TicketFragment.TYPE_ALL))
                    .commit()
                true
            }

            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_drawer_view, menu)
        menuAddFeedback = menu?.findItem(R.id.menu_add_feedback)
        menuSearchFilter = menu?.findItem(R.id.menu_filter)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mainDrawerBinding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.menu_add_feedback -> {
                NewTicketDialogWrapper(this, this, null) { creationDto, dialog ->
                    GlobalScope.launch {
                        try {
                            Client().use {
                                val created = it.feedback().create(creationDto)
                                it.comment().add(CommentCreationDto(created.id, "body", creationDto.comment))
                            }
                        } catch (e: Throwable) {
                            showError(getString(R.string.error_conn))
                        }
                        withContext(Dispatchers.Main) {
                            dialog.dismiss()
                            with(supportFragmentManager.findFragmentById(R.id.content_frame) as TicketFragment) {
                                startTicketRefresh(feedbackFilter)
                            }
                        }
                    }
                }.also {
                    it.show()
                }
                true
            }
            R.id.menu_filter -> {
                SearchFilterDialogWrapper(this) {
                    runOnUiThread {
                        feedbackFilter = it
                        with(supportFragmentManager.findFragmentById(R.id.content_frame) as TicketFragment) {
                            startTicketRefresh(feedbackFilter)
                        }
                    }
                }.also {
                    it.show(feedbackFilter)
                }
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateTechInfo() {
        GlobalScope.launch(Dispatchers.Default) {
            val techUserData = try {
                userDataController.getUserData()
            } catch (e: Throwable) {
                return@launch
            }

            withContext(Dispatchers.Main) {
                tvHeaderUser.text = techUserData.username
                tvHeaderUser.visibility = View.VISIBLE
            }
        }
    }

    override fun showError(error: String) {
        runOnUiThread {
            Log.d("TicketFragment", "Failed to load ticket: $error")
            Snackbar.make(mainDrawerBinding.contentFrame, error, Snackbar.LENGTH_LONG).apply {
                view.findViewById<TextView>(R.id.snackbar_text).apply {
                    setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
                    compoundDrawablePadding = resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
                }
            }.show()
        }
    }
}
