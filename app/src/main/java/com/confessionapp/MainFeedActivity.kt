package com.confessionapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MainFeedActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    val mAuth = FirebaseAuth.getInstance()
    var userEmailTextView: TextView? = null
    var userNameTextView: TextView? = null
    var navProfileImage: CircleImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_feed)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //Set up Account Information
        val headerView : View = navView.getHeaderView(0)
        userEmailTextView = headerView.findViewById(R.id.userEmailTextView)
        userEmailTextView?.text = mAuth.currentUser?.email.toString()
        userNameTextView = headerView.findViewById(R.id.usernameTextView)
        navProfileImage = headerView.findViewById(R.id.nav_profile_image)



        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid.toString()).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userNameTextView?.text = snapshot.child("name").value.toString()
                Picasso.get().load(snapshot.child("profileImageURL").value.toString()).placeholder(R.drawable.profile).into(navProfileImage)

            }
            override fun onCancelled(error: DatabaseError) { }
        })



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_feed, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == R.id.logout){
            mAuth.signOut()
            finish()
        } else if (item?.itemId == R.id.action_create) {
            val intent = Intent(this, WriteConfessionActivity::class.java)
            startActivity(intent)
        } else if (item?.itemId == R.id.action_settings) {
            Toast.makeText(this, "In progress!", Toast.LENGTH_SHORT).show()
        }


        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    fun profileImageClicked(view: View) {
        val intent = Intent(this, AccountInfoActivity::class.java)
        startActivity(intent)

    }




}