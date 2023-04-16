package edu.timurmakhmutov.bottomnavstrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentPayBinding;

public class PayFragment extends Fragment {

    FragmentPayBinding fragmentPayBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentPayBinding = FragmentPayBinding.inflate(inflater, container, false);
        return fragmentPayBinding.getRoot();
    }
}