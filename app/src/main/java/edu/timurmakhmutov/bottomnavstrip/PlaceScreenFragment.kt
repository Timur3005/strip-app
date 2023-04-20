package edu.timurmakhmutov.bottomnavstrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentPlaceScreenBinding;

public class PlaceScreenFragment extends Fragment {

    FragmentPlaceScreenBinding fragmentPlaceScreenBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentPlaceScreenBinding = FragmentPlaceScreenBinding.inflate(inflater, container, false);
        return fragmentPlaceScreenBinding.getRoot();
    }
}