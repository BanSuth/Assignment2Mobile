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

    EditText lat,longi; //defining xml elements
    private FragmentSecondBinding binding; //defining a view binding to use.

    public secondFragment() {
        // Required empty public constructor
    }

    public static secondFragment newInstance(String param1, String param2) { //unused, required for fragment.
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
        binding = FragmentSecondBinding.inflate(inflater, container, false);  //defining the view binding.
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lat= binding.latField; //defining xml elements to variables
        longi=binding.longiField;

        binding.calculateButton.setOnClickListener(new View.OnClickListener() { //button on click to display the address of the user input coords
            @Override
            public void onClick(View view) {
                if(lat.getText().length() != 0 && longi.getText().length() != 0){ //making sure that input fields are not empty
                    double latI = Double.parseDouble(lat.getText().toString().trim()); //getting the user inputs
                    double longI= Double.parseDouble(longi.getText().toString().trim());
                    String address = "The Address for those coordinates are: "+calculateAddr(latI,longI); //calculating the address and storing to string

                    binding.textView.setText(address); //displaying the calculated address
                }else { //else for when input fields are empty, display message to user
                    Toast.makeText(getContext(), "Fields can not be Blank.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() { //button on click to call save function for user input coords
            @Override
            public void onClick(View view) {
                addAddr();
            }
        });



        binding.noteBackbutton.setOnClickListener(view12 -> NavHostFragment.findNavController(secondFragment.this)
                .navigate(R.id.secondTofirst)); //on click to navigate back to main page
    }

    private String calculateAddr(double latInp, double longInp) { //function to calculate an address given coordinates
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault()); //using geocoding to find address
        try {
            addresses = geocoder.getFromLocation(latInp, longInp, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return addresses.get(0).getAddressLine(0).trim(); //getting the address
    }


    private void addAddr() { //function to get address input and insert into DB
        if(lat.getText().length() != 0 && longi.getText().length() != 0){ //checking to see inputs are not blank
            Double latI = Double.valueOf(lat.getText().toString().trim());
            Double longInp= Double.valueOf(longi.getText().toString().trim()); //getting user input
            String addressI = calculateAddr(latI,longInp); //calculating address from user input

            addrObj addAddr = new addrObj(addressI,latI,longInp); //defining new address object to pass
            databaseHelper sDB = new databaseHelper(getContext()); //creating new DB helper to use
            sDB.addAddr(addAddr); // calling DB function to insert into DB

            sDB.close();
            Toast.makeText(getContext(), "Address Saved.", Toast.LENGTH_SHORT).show();
        }else { //else for when input fields are empty, display message to user
            Toast.makeText(getContext(), "Fields can not be Blank.", Toast.LENGTH_SHORT).show();
        }
        }
}