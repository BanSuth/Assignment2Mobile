package com.example.assign2mobile;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lat= binding.latField;
        longi=binding.longiField;

        binding.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // NavHostFragment.findNavController(secondFragment.this).navigate(R.id.secondTofirst);
                if(lat.getText().length() != 0 && longi.getText().length() != 0){
                Double latI = Double.valueOf(lat.getText().toString().trim());
                Double longI= Double.valueOf(longi.getText().toString().trim());
                String address = calculateAddr(latI,longI);

                binding.textView.setText("The Address for those coordinates are: "+address);
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
        String address = addresses.get(0).getAddressLine(0).trim();

        return address;
    }


    private void addAddr() {

        if(lat.getText().length() != 0 && longi.getText().length() != 0){
            Double latI = Double.valueOf(lat.getText().toString().trim());
            Double longInp= Double.valueOf(longi.getText().toString().trim());
            String addressI = calculateAddr(latI,longInp);

            addrObj addAddr = new addrObj(addressI,latI,longInp);
            databaseHelper sDB = new databaseHelper(getContext());
            boolean id = sDB.addAddr(addAddr);
         //   note check = sDB.getNote(id);
            //Log.d("inserted", "Note: "+ id + " -> Title:" + check.getTitle()+" colour: "+ check.getColour());


            Toast.makeText(getContext(), "Address Saved.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Fields can not be Blank.", Toast.LENGTH_SHORT).show();
        }
        }
}