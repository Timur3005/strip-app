package edu.timurmakhmutov.bottomnavstrip;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentLKBinding;

public class LKFragment extends Fragment {

    FragmentLKBinding lkBinding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lkBinding = FragmentLKBinding.inflate(inflater,container, false);
        // Inflate the layout for this fragment
        lkBinding.startInLk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(lkBinding.getRoot()).navigate(R.id.action_LKFragment_to_stateTripFragment);
            }
        });
        return lkBinding.getRoot();
    }
}