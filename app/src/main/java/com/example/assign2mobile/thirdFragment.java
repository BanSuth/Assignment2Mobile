package com.example.assign2mobile;

import android.content.ContentValues;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assign2mobile.databinding.FragmentThirdBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class thirdFragment extends Fragment {

    private FragmentThirdBinding binding;

    private databaseHelper addrDB;
    private EditText lati,longi;
    addrObj uAddr;

    private TextView addrDisp;
    public thirdFragment() {
        // Required empty public constructor
    }


    public static thirdFragment newInstance(String param1, String param2) {
        thirdFragment fragment = new thirdFragment();
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
        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addrDB = new databaseHelper(getActivity());
        lati=binding.latField;
        longi =binding.longiField;
        addrDisp = binding.textView;
        uAddr = new addrObj(getArguments().getInt("id"),getArguments().getString("address"),getArguments().getDouble("lat"),getArguments().getDouble("longi"));


        lati.setText(String.valueOf(uAddr.getLatitude()));
        longi.setText(String.valueOf(uAddr.getLongitude()));

        addrDisp.setText("The Address is "+uAddr.getAddress());


        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NavHostFragment.findNavController(secondFragment.this).navigate(R.id.secondTofirst);
                deleteAddress(uAddr);
            };
        });

        binding.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NavHostFragment.findNavController(secondFragment.this).navigate(R.id.secondTofirst);
                if(lati.getText().length() != 0 && longi.getText().length() != 0){
                    Double latI = Double.valueOf(lati.getText().toString().trim());
                    Double longI= Double.valueOf(longi.getText().toString().trim());
                    String address = calculateAddr(latI,longI);

                    addrDisp.setText("The Address for those coordinates are: "+address);
                }else {
                    Toast.makeText(getContext(), "Fields can not be Blank.", Toast.LENGTH_SHORT).show();
                }

            }
        });




        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NavHostFragment.findNavController(secondFragment.this).navigate(R.id.secondTofirst);

                updateAddress(uAddr);
            };
        });
        binding.noteBackbutton.setOnClickListener(e-> NavHostFragment.findNavController(thirdFragment.this).navigate(R.id.thirdTofirst));
    }
    private String calculateAddr(double latInp, double longInp) {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latInp, longInp, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String address = addresses.get(0).getAddressLine(0).trim();

        return address;
    }
    private void deleteAddress(addrObj uAddr) {
        boolean result = addrDB.deleteAddr(uAddr);
        if(!result){
            Toast.makeText(getActivity(), "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
        NavHostFragment.findNavController(thirdFragment.this).navigate(R.id.thirdTofirst);
    }

    private void updateAddress(addrObj uAddr) {
        if(lati.getText().length() != 0 && longi.getText().length() != 0){
            Double latI = Double.valueOf(lati.getText().toString().trim());
            Double longInp= Double.valueOf(longi.getText().toString().trim());
            String addressI = calculateAddr(latI,longInp);

            uAddr.setLatitude(latI);;
            uAddr.setLongitude(longInp);
            uAddr.setAddress(addressI);
            boolean result = addrDB.updateAddr(uAddr);

            if(!result){
                Toast.makeText(getActivity(), "Failed to Update.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Successfully Updated.", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(getContext(), "Fields can not be Blank.", Toast.LENGTH_SHORT).show();
        }
    }
}
