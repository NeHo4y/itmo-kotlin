package com.github.neho4u.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.github.neho4u.R
import com.github.neho4u.controller.UserDataController
import com.github.neho4u.databinding.AMainDrawerBinding
import com.github.neho4u.databinding.DrawerHeaderBinding
import com.github.neho4u.model.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DrawerView : AppCompatActivity(), TicketFragment.OnListFragmentInteractionListener {

    private var menuRefresh: MenuItem? = null
    private var menuAddFeedback: MenuItem? = null
    private lateinit var mainDrawerBinding: AMainDrawerBinding
    private lateinit var drawerHeaderBinding: DrawerHeaderBinding
    private lateinit var tvHeaderUser: TextView
    private val userDataController = UserDataController()

    override fun onListFragmentInteraction(ticket: Ticket?) {
        val i = Intent(this, TicketView::class.java)
        i.putExtra(TicketView.ARG_TICKET_ID, ticket?.id)
        startActivity(i)
    }

    override fun setProgressVisibility(visible: Boolean) {
        menuRefresh?.isVisible = !visible
//        menuAddFeedback?.isVisible = !visible

        if (visible) {
            mainDrawerBinding.pbMainDrawer.visibility = View.VISIBLE
        } else {
            mainDrawerBinding.pbMainDrawer.visibility = View.GONE
        }
    }

    private lateinit var preferences: SharedPreferences
    private var lastTranslate = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerHeaderBinding = DataBindingUtil.setContentView(this, R.layout.drawer_header)
        mainDrawerBinding = DataBindingUtil.setContentView(this, R.layout.a_main_drawer)
        setSupportActionBar(mainDrawerBinding.drawerToolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

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

        tvHeaderUser = mainDrawerBinding.navView
            .getHeaderView(0)
            .findViewById(R.id.tv_header_user)
        updateTechInfo()
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
        menuRefresh = menu?.findItem(R.id.menu_dv_refresh)
        menuAddFeedback = menu?.findItem(R.id.menu_add_feedback)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mainDrawerBinding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.menu_dv_refresh -> {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.content_frame) as TicketFragment
                currentFragment.startTicketRefresh()
                true
            }
            R.id.menu_add_feedback -> {
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
}
