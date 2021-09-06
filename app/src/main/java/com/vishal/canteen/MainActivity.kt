package com.vishal.canteen

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vishal.canteen.databinding.ActivityMainBinding
import com.vishal.canteen.viewmodel.MainAcivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.annotation.Signed

const val RC_SIGN_IN = 0

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    var signedIn = false
    private val viewModel: MainAcivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)//https://stackoverflow.com/questions/57175226/how-to-disable-night-mode-in-my-application-even-if-night-mode-is-enable-in-andr
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
//        auth.signOut()
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.cartFragment, R.id.orderFragment))
        binding.bottomNav.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        auth.addAuthStateListener { user ->
            Log.i("logcat", "auth change invoked")
            viewModel.authh.value = auth

            when (user.currentUser) {
                null -> {
                    Log.i("logcat", "not signed in")
                    Toast.makeText(this, "not signed in", Toast.LENGTH_SHORT).show()
                    googleSignIn()
                    signedIn = false
                    invalidateOptionsMenu()
                }
                else -> {
                    Log.i("logcat", "signed in")
                    Toast.makeText(this, auth.currentUser.displayName, Toast.LENGTH_SHORT).show()
                    viewModel.authh.value = auth
                    signedIn = true
                    invalidateOptionsMenu()
                }
            }
        }

    }


    private fun googleSignIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        invalidateOptionsMenu()
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.ic_baseline_fastfood_24)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, "Signed in as $user?.displayName.toString()", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
//        menu.findItem(R.id.signOut).isVisible = true
        menu.findItem(R.id.signOu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.signOut -> {
                auth.signOut()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}