package com.example.smartstudy

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

open class MenuLogic : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    fun setup(drawerLayout: DrawerLayout, toolbar: Toolbar, navigationView: NavigationView){
        drawer = drawerLayout

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_one -> { val intent = Intent(this, Perfil::class.java)
                startActivity(intent)
            }
            R.id.nav_item_two -> { val intent = Intent(this, Agenda::class.java)
                startActivity(intent)
            }
            R.id.nav_item_three -> { val intent = Intent(this, Horario::class.java)
                startActivity(intent)
            }
            R.id.nav_item_four -> { val intent = Intent(this, Tareas::class.java)
                startActivity(intent)
            }
            R.id.nav_item_seven -> { val intent = Intent(this, activity_permisos::class.java)
                startActivity(intent)
            }
            R.id.nav_item_eight -> { val intent = Intent(this, ApiMoviesActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_item_nine -> { val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}