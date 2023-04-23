package edu.timurmakhmutov.bottomnavstrip;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.android.volley.toolbox.DiskBasedCache;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB;
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository;
import edu.timurmakhmutov.bottomnavstrip.databinding.ActivityMainBinding;
import okhttp3.Cache;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    private TableForDBRepository tableForDBRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.LKFragment, R.id.stateTripFragment, R.id.paymentTripFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(activityMainBinding.bottomNavigationView, navController);
        Objects.requireNonNull(getSupportActionBar()).hide();

        tableForDBRepository = new TableForDBRepository(getApplication());
//        tableForDBRepository.getAllTables().observe(this, new Observer<List<TableForDB>>() {
//            @Override
//            public void onChanged(List<TableForDB> tableForDBS) {
//
//            }
//        });
    }
}