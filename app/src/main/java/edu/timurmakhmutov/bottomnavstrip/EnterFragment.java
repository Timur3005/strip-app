package edu.timurmakhmutov.bottomnavstrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentEnterBinding;

public class EnterFragment extends Fragment {

    FragmentEnterBinding fragmentEnterBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentEnterBinding = FragmentEnterBinding.inflate(inflater, container, false);
        return fragmentEnterBinding.getRoot();
    }
}