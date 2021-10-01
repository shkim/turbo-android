package dev.hotwire.turbo.demo.main

import android.os.Bundle
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.hotwire.turbo.activities.TurboActivity
import dev.hotwire.turbo.delegates.TurboActivityDelegate
import dev.hotwire.turbo.demo.R

class MainActivity : AppCompatActivity(), TurboActivity {
    override lateinit var delegate: TurboActivityDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        delegate = TurboActivityDelegate(this, R.id.tab1)
        val frag1 = delegate.registerNavHostFragment(R.id.tab1)
        val frag2 = delegate.registerNavHostFragment(R.id.tab2)

        val tabSwitcher = findViewById<ViewFlipper>(R.id.tabSwitcher)
        val btmTabBar = findViewById<BottomNavigationView>(R.id.tabBar)

        btmTabBar.setOnNavigationItemSelectedListener {
            val (tabIndex, fragId) = when (it.itemId) {
                R.id.tab1 -> Pair(0, R.id.tab1)
                R.id.tab2 -> Pair(1, R.id.tab2)
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
            tabSwitcher.displayedChild = tabIndex
            delegate.currentNavHostFragmentId = fragId
            return@setOnNavigationItemSelectedListener true
        }
    }
}
