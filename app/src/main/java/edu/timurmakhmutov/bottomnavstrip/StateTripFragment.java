package edu.timurmakhmutov.bottomnavstrip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentStateTripBinding;

public class StateTripFragment extends Fragment {

    FragmentStateTripBinding fragmentStateTripBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentStateTripBinding = FragmentStateTripBinding.inflate(inflater, container, false);
        fragmentStateTripBinding.stateTripEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(fragmentStateTripBinding.getRoot()).navigate(R.id.action_stateTripFragment_to_homeFragment);
            }
        });
        return fragmentStateTripBinding.getRoot();
    }
}