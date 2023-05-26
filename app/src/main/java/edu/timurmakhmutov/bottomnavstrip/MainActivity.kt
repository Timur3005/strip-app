package edu.timurmakhmutov.bottomnavstrip

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository
import edu.timurmakhmutov.bottomnavstrip.databinding.ActivityMainBinding
import edu.timurmakhmutov.bottomnavstrip.view_model.HomeViewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private var tableForDBRepository: TableForDBRepository? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var reference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var model: HomeViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(
            layoutInflater)
        setContentView(activityMainBinding.root)
        model = ViewModelProvider(this)[HomeViewModel::class.java]
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment, R.id.LKFragment, R.id.stateTripFragment, R.id.paymentTripFragment)
            .build()
        val navController = findNavController(this, R.id.nav_host_fragment)
        setupActionBarWithNavController(this, navController, appBarConfiguration)
        setupWithNavController(activityMainBinding.bottomNavigationView, navController)
        Objects.requireNonNull(supportActionBar)?.hide()
        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase!!.reference
        tableForDBRepository = TableForDBRepository(application)
        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            if (navDestination.id == R.id.homeFragment || navDestination.id == R.id.LKFragment || navDestination.id == R.id.stateTripFragment || navDestination.id == R.id.paymentTripFragment || navDestination.id == R.id.bottomSheetFragment) {
                activityMainBinding.bottomNavigationView.visibility = View.VISIBLE
            } else {
                activityMainBinding.bottomNavigationView.visibility = View.GONE
            }
        }
        tableForDBRepository!!.allTables.observe(this) { tableForDBS ->
            if (tableForDBS != null && mAuth?.currentUser?.uid!=null) {
                for (i in tableForDBS.indices) {
                    if (tableForDBS[i].inPath == 1 || tableForDBS[i].inLiked == 1) {
//                            reference.child("Users").child(mAuth.getUid()).child("Liked").child("placeId").setValue(tableForDBS.get(i).identification);
                        reference!!.child("Users").child(mAuth?.currentUser?.uid.toString()).child("Liked").child(
                            tableForDBS[i].identification).child("id")
                            .setValue(tableForDBS[i].identification)
                        reference!!.child("Users").child(mAuth?.currentUser?.uid.toString()).child("Liked").child(
                            tableForDBS[i].identification).child("address")
                            .setValue(tableForDBS[i].address)
                        reference!!.child("Users").child(mAuth?.currentUser?.uid.toString()).child("Liked").child(
                            tableForDBS[i].identification).child("inLiked")
                            .setValue(tableForDBS[i].inLiked)
                        reference!!.child("Users").child(mAuth?.currentUser?.uid.toString()).child("Liked").child(
                            tableForDBS[i].identification).child("inPath")
                            .setValue(tableForDBS[i].inPath)
                        reference!!.child("Users").child(mAuth?.currentUser?.uid.toString()).child("Liked").child(
                            tableForDBS[i].identification).child("location")
                            .setValue(tableForDBS[i].location)
                        reference!!.child("Users").child(mAuth?.currentUser?.uid.toString()).child("Liked").child(
                            tableForDBS[i].identification).child("lat").setValue(tableForDBS[i].lat)
                        reference!!.child("Users").child(mAuth?.currentUser?.uid.toString()).child("Liked").child(
                            tableForDBS[i].identification).child("lon").setValue(tableForDBS[i].lon)
                        reference!!.child("Users").child(mAuth?.currentUser?.uid.toString()).child("Liked").child(
                            tableForDBS[i].identification).child("title")
                            .setValue(tableForDBS[i].title)
                    } else {
                        reference!!.child("Users").child(mAuth?.currentUser?.uid.toString()).child("Liked").child(
                            tableForDBS[i].identification).removeValue()
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }
}