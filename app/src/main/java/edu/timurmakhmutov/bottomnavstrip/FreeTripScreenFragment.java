package edu.timurmakhmutov.bottomnavstrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentFreeTripScreenBinding;

public class FreeTripScreenFragment extends Fragment {

    FragmentFreeTripScreenBinding fragmentFreeTripScreenBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentFreeTripScreenBinding = FragmentFreeTripScreenBinding.inflate(inflater, container, false);
        return fragmentFreeTripScreenBinding.getRoot();
    }
}