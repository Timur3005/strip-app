package edu.timurmakhmutov.bottomnavstrip;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yandex.mapkit.MapKitFactory;

import java.util.Objects;

import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDBRepository;
import edu.timurmakhmutov.bottomnavstrip.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    private TableForDBRepository tableForDBRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.LKFragment, R.id.stateTripFragment, R.id.paymentTripFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(activityMainBinding.bottomNavigationView, navController);
        Objects.requireNonNull(getSupportActionBar()).hide();

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
    }
}