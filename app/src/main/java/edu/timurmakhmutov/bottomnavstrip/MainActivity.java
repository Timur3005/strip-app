package edu.timurmakhmutov.bottomnavstrip;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB;
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository;
import edu.timurmakhmutov.bottomnavstrip.databinding.ActivityMainBinding;
import edu.timurmakhmutov.bottomnavstrip.view_model.HomeViewModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    private TableForDBRepository tableForDBRepository;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private HomeViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        model = new ViewModelProvider(this).get(HomeViewModel.class);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.LKFragment, R.id.stateTripFragment, R.id.paymentTripFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(activityMainBinding.bottomNavigationView, navController);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        tableForDBRepository = new TableForDBRepository(getApplication());

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if(navDestination.getId() == R.id.homeFragment || navDestination.getId() == R.id.LKFragment || navDestination.getId() == R.id.stateTripFragment ||
                        navDestination.getId() == R.id.paymentTripFragment || navDestination.getId() == R.id.bottomSheetFragment) {
                    activityMainBinding.bottomNavigationView.setVisibility(View.VISIBLE);
                } else {
                    activityMainBinding.bottomNavigationView.setVisibility(View.GONE);
                }
            }
        });
        tableForDBRepository.getAllTables().observe(this, new Observer<List<TableForDB>>() {
            @Override
            public void onChanged(List<TableForDB> tableForDBS) {
                if (tableForDBS != null) {
                    for (int i = 0; i < tableForDBS.size(); i++) {
                        if (tableForDBS.get(i).inPath == 1 || tableForDBS.get(i).inLiked == 1) {
//                            reference.child("Users").child(mAuth.getUid()).child("Liked").child("placeId").setValue(tableForDBS.get(i).identification);
                            reference.child("Users").child(mAuth.getUid()).child("Liked").child(tableForDBS.get(i).identification).child("id").setValue(tableForDBS.get(i).identification);
                            reference.child("Users").child(mAuth.getUid()).child("Liked").child(tableForDBS.get(i).identification).child("address").setValue(tableForDBS.get(i).address);
                            reference.child("Users").child(mAuth.getUid()).child("Liked").child(tableForDBS.get(i).identification).child("inLiked").setValue(tableForDBS.get(i).inLiked);
                            reference.child("Users").child(mAuth.getUid()).child("Liked").child(tableForDBS.get(i).identification).child("inPath").setValue(tableForDBS.get(i).inPath);
                            reference.child("Users").child(mAuth.getUid()).child("Liked").child(tableForDBS.get(i).identification).child("location").setValue(tableForDBS.get(i).location);
                            reference.child("Users").child(mAuth.getUid()).child("Liked").child(tableForDBS.get(i).identification).child("lat").setValue(tableForDBS.get(i).lat);
                            reference.child("Users").child(mAuth.getUid()).child("Liked").child(tableForDBS.get(i).identification).child("lon").setValue(tableForDBS.get(i).lon);
                            reference.child("Users").child(mAuth.getUid()).child("Liked").child(tableForDBS.get(i).identification).child("title").setValue(tableForDBS.get(i).title);
                        } else {
                            reference.child("Users").child(mAuth.getUid()).child("Liked").child(tableForDBS.get(i).identification).removeValue();
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}