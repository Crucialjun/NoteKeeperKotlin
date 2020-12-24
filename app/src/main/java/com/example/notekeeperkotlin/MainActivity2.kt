package com.example.notekeeperkotlin

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.Nav_notes,
                R.id.Nav_courses
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
        navView.menu.getItem(0).isChecked = true
        drawerLayout.closeDrawers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity2, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.nav_notes -> {
                val notesFragment = NoteListFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, notesFragment).commit()
                drawerLayout.closeDrawers()
                true
            }
            R.id.nav_courses -> {
                val coursesFragment = CoursesFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, coursesFragment).commit()
                drawerLayout.closeDrawers()
                true
            }
            R.id.nav_share -> {
                handleSelection("Dont think you have shared enough")
                handleshare()
                drawerLayout.closeDrawers()
                true
            }
            R.id.nav_send -> {
                handleSelection("Send")
                drawerLayout.closeDrawers()
                true
            }

            else -> true
        }


    }

    private fun handleshare() {
        Snackbar.make(
            currentFocus!!,
            "Share to - ${
                PreferenceManager.getDefaultSharedPreferences(this)
                    .getString("user_favorite_social", "")
            }",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun handleSelection(message: String) {
        Snackbar.make(findViewById(R.id.list_notes), message, Snackbar.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            navController.navigate(R.id.action_Nav_notes_to_settingsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        updateNavHeader()
    }

    private fun updateNavHeader() {
        val headerView = navView.getHeaderView(0)
        val textUsername = headerView.findViewById<TextView>(R.id.text_user_name)
        val textEmail = headerView.findViewById<TextView>(R.id.text_email_address)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val username = prefs.getString("user_display_name", "")
        val emailAddress = prefs.getString("user_email_address", "")

        textUsername.text = username
        textEmail.text = emailAddress

    }


}