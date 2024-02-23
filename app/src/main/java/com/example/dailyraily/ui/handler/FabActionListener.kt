package com.example.dailyraily.ui.handler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.dailyraily.R
import com.example.dailyraily.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


class FabOnClickListenerHandler(
    private val activity: AppCompatActivity,
    val _binding: ActivityMainBinding
) {
    val binding
        get() = _binding

    fun setOnClickListener() {
        val fab = _binding.appBarMain.fab
        fab.setOnClickListener {
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_main)
            val navView: NavigationView = _binding.navView

            val selectedItemId = navView.checkedItem?.itemId ?: return@setOnClickListener

            when (selectedItemId) {
                R.id.nav_todo -> {
                    val selectedTab = "Todo"
                    handleFabClick(selectedTab)
                }

                R.id.nav_game -> {
                    val selectedTab = "Game"
                    handleFabClick(selectedTab)
                }

                R.id.nav_filter -> {
                    val selectedTab = "Todo"
                    val filterGame =
                        navController.previousBackStackEntry?.arguments?.getString("filterGame")
                    handleFabClick(selectedTab, filterGame)
                }


                else -> {
                    defaultFabClick()
                }
            }
        }

    }

    private fun handleFabClick(selectedTab: String, filterGame: String? = null) {
        val navController =
            Navigation.findNavController(activity, R.id.nav_host_fragment_content_main)
        val args = Bundle()
        args.putString("selectedTab", selectedTab)
        if (filterGame != null) {
            args.putString("filterGame", filterGame)
        }
        navController.navigate(R.id.nav_input, args)
    }

    private fun defaultFabClick() {
    }
}

