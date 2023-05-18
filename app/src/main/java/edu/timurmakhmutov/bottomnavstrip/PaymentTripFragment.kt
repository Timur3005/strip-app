package edu.timurmakhmutov.bottomnavstrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentPaymentTripBinding;


public class PaymentTripFragment extends Fragment {

    FragmentPaymentTripBinding fragmentPaymentTripBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentPaymentTripBinding = FragmentPaymentTripBinding.inflate(inflater, container, false);

        return fragmentPaymentTripBinding.getRoot();
    }
}