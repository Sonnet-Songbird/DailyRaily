package com.example.dailyraily

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dailyraily.data.service.TodoListManager
import com.example.dailyraily.databinding.ActivityMainBinding
import com.example.dailyraily.ui.handler.FabOnClickListenerHandler
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    companion object {
        const val FILTER_ITEM_COUNT = 3
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView
    private lateinit var navigationViewModel: MainNavigationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TodoListManager.load(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        FabOnClickListenerHandler(this, binding).setOnClickListener()

        navigationViewModel = ViewModelProvider(this)[MainNavigationViewModel::class.java]

        val drawerLayout: DrawerLayout = binding.drawerLayout
        drawerLayout.addDrawerListener(drawerListener())

        navView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        updateNavigationMenu(3)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_todo, R.id.nav_game, R.id.nav_filter, R.id.nav_filter, R.id.nav_filter
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationItemClick(menuItem)
            true
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }


    private var backPressedTime = 0L

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                finish()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this@MainActivity, "한 번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show()
            }
            navController.navigate(R.id.nav_todo)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun updateNavigationMenu(itemCount: Int) {
        val navMenu = navView.menu
        navigationViewModel.updateNavigation(this)
        navMenu.clear()
        navMenu.add(R.id.drawer_menu, R.id.nav_todo, 1001, "Todo")
            .setIcon(R.drawable.ic_menu_todo)
            .setCheckable(true)

        navMenu.add(R.id.drawer_menu, R.id.nav_game, 1002, "Game")
            .setIcon(R.drawable.ic_menu_game)
            .setCheckable(true)
        if (navigationViewModel.navData.isNotEmpty()) {
            for (i in 0 until itemCount) {
                val itemId = View.generateViewId()
                navMenu.add(R.id.drawer_menu, itemId, Menu.NONE, generateItemTitle(i))
                    .setIcon(setFilterIcon(i))
                    .setCheckable(true)
            }
        }
    }

    private fun generateItemTitle(index: Int): String {
        return navigationViewModel.getItemName(index)
    }

    private fun setFilterIcon(index: Int): Int {
        val basePath = "ic_menu_filter"
        val suffix = "$basePath$index"
        return resources.getIdentifier(suffix, "drawable", packageName)
    }

    private fun handleNavigationItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.nav_todo, R.id.nav_game -> {
                navController.navigate(menuItem.itemId)
            }

            else -> {
                val itemId = menuItem.itemId
                val index = itemId - R.id.drawer_menu

                val argKey = "filterGame"
                val argValue = navigationViewModel.getItemName(index)
                val args = Bundle()
                args.putString(argKey, argValue)
                navController.navigate(itemId, args)
            }
        }
    }

    fun hideFab() {
        binding.appBarMain.fab.visibility = View.GONE
    }

    fun showFab() {
        binding.appBarMain.fab.visibility = View.VISIBLE
    }

    private fun drawerListener() = object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        }

        override fun onDrawerOpened(drawerView: View) {
            updateNavigationMenu(FILTER_ITEM_COUNT)
        }

        override fun onDrawerClosed(drawerView: View) {
        }

        override fun onDrawerStateChanged(newState: Int) {
        }
    }
}
