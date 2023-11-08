package com.example.assign2mobile;

import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.assign2mobile.databinding.FragmentSecondBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class secondFragment extends Fragment {

    EditText lat,longi;
    private FragmentSecondBinding binding;

    public secondFragment() {
        // Required empty public constructor
    }


    public static secondFragment newInstance(String param1, String param2) {
        secondFragment fragment = new secondFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lat= binding.latField;
        longi=binding.longiField;

        binding.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lat.getText().length() != 0 && longi.getText().length() != 0){
                double latI = Double.parseDouble(lat.getText().toString().trim());
                double longI= Double.parseDouble(longi.getText().toString().trim());
                String address = "The Address for those coordinates are: "+calculateAddr(latI,longI);

                binding.textView.setText(address);
                }else {
                    Toast.makeText(getContext(), "Fields can not be Blank.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAddr();
            }
        });



        binding.noteBackbutton.setOnClickListener(view12 -> NavHostFragment.findNavController(secondFragment.this)
                .navigate(R.id.secondTofirst));
    }

    private String calculateAddr(double latInp, double longInp) {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latInp, longInp, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return addresses.get(0).getAddressLine(0).trim();
    }


    private void addAddr() {
        if(lat.getText().length() != 0 && longi.getText().length() != 0){
            Double latI = Double.valueOf(lat.getText().toString().trim());
            Double longInp= Double.valueOf(longi.getText().toString().trim());
            String addressI = calculateAddr(latI,longInp);

            addrObj addAddr = new addrObj(addressI,latI,longInp);
            databaseHelper sDB = new databaseHelper(getContext());
            sDB.addAddr(addAddr);

            sDB.close();
            Toast.makeText(getContext(), "Address Saved.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Fields can not be Blank.", Toast.LENGTH_SHORT).show();
        }
        }
}