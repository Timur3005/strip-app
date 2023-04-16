
package edu.timurmakhmutov.bottomnavstrip;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.timurmakhmutov.bottomnavstrip.databinding.FragmentOnePaymentTripBinding;


public class OnePaymentTripFragment extends Fragment {

    FragmentOnePaymentTripBinding fragmentOnePaymentTripBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentOnePaymentTripBinding = FragmentOnePaymentTripBinding.inflate(inflater, container, false);
        fragmentOnePaymentTripBinding.buyInOneTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(fragmentOnePaymentTripBinding.getRoot()).navigate(R.id.action_onePaymentTripFragment_to_payFragment);
            }
        });
        return fragmentOnePaymentTripBinding.getRoot();
    }
}