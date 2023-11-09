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
import android.widget.TextView;
import android.widget.Toast;

import com.example.assign2mobile.databinding.FragmentThirdBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class thirdFragment extends Fragment {

    private FragmentThirdBinding binding; //defining a view binding to use.
    private databaseHelper addrDB; //defining a database helper to use.
    private EditText lati,longi; //defining xml elements
    private TextView addrDisp;//defining xml elements
    addrObj uAddr; //defining address object to holder the address info passed into the view
    public thirdFragment() {
        // Required empty public constructor
    }


    public static thirdFragment newInstance(String param1, String param2) {//unused, required for fragment
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
        binding = FragmentThirdBinding.inflate(inflater, container, false); //defining the view binding.
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addrDB = new databaseHelper(getActivity()); //creating DB helper to use
        lati=binding.latField; //defining xml elements to variables
        longi =binding.longiField;
        addrDisp = binding.textView;
        //define address object to use based on passed arguments into this view
        uAddr = new addrObj(getArguments().getInt("id"),getArguments().getString("address"),getArguments().getDouble("lat"),getArguments().getDouble("longi"));


        lati.setText(String.valueOf(uAddr.getLatitude()));
        longi.setText(String.valueOf(uAddr.getLongitude())); //setting text based on the passed value
        addrDisp.setText("The Address is "+uAddr.getAddress());


        binding.deleteButton.setOnClickListener(new View.OnClickListener() { //onclick function to call delete function to delete entry
            @Override
            public void onClick(View view) {
                deleteAddress(uAddr);
            }
        });

        binding.calculateButton.setOnClickListener(new View.OnClickListener() { //button on click function to calculate address based on user input coords same as second fragment.
            @Override
            public void onClick(View view) {
                if(lati.getText().length() != 0 && longi.getText().length() != 0){ //making sure that input fields are not empty
                    double latI = Double.parseDouble(lati.getText().toString().trim());
                    double longI= Double.parseDouble(longi.getText().toString().trim());
                    String address = calculateAddr(latI,longI);

                    addrDisp.setText("The Address for those coordinates are: "+address); //displaying the calculated address
                }else { //else for when input fields are empty, display message to user
                    Toast.makeText(getContext(), "Fields can not be Blank.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.updateButton.setOnClickListener(new View.OnClickListener() { //button on click to call update function, to update existing entry in DB.
            @Override
            public void onClick(View view) {
                updateAddress(uAddr);
            }
        });
        //on click to navigate back to main page
        binding.noteBackbutton.setOnClickListener(e-> NavHostFragment.findNavController(thirdFragment.this).navigate(R.id.thirdTofirst));
    }
    private String calculateAddr(double latInp, double longInp) { //function to calculate address given coordinates using geocoding, same as secondFragment
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latInp, longInp, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return addresses.get(0).getAddressLine(0).trim();
    }
    private void deleteAddress(addrObj uAddr) { //function to delete address from DB, using DB function.
        boolean result = addrDB.deleteAddr(uAddr); //use DB handler to delete an entry from DB
        if(!result){ //if else to display if delete was successful or not and display message
            Toast.makeText(getActivity(), "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
        NavHostFragment.findNavController(thirdFragment.this).navigate(R.id.thirdTofirst); //going back to main page, as the current entry was deleted
    }

    private void updateAddress(addrObj uAddr) {
        if(lati.getText().length() != 0 && longi.getText().length() != 0){ //checking to see inputs are not blank
            double latI = Double.parseDouble(lati.getText().toString().trim());
            double longInp= Double.parseDouble(longi.getText().toString().trim()); //getting user input
            String addressI = calculateAddr(latI,longInp); //calculating address based on input

            uAddr.setLatitude(latI);
            uAddr.setLongitude(longInp); //updating the passed address object
            uAddr.setAddress(addressI);
            boolean result = addrDB.updateAddr(uAddr); //calling DB function to update the entry

            if(!result){ //if else to display if delete was successful or not and display message
                Toast.makeText(getActivity(), "Failed to Update.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Successfully Updated.", Toast.LENGTH_SHORT).show();
            }

        }else { //else for when fields are blank, display message to user
            Toast.makeText(getContext(), "Fields can not be Blank.", Toast.LENGTH_SHORT).show();
        }
    }
}
