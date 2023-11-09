package com.example.assign2mobile;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assign2mobile.databinding.FragmentFirstBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class firstFragment extends Fragment {


    private FragmentFirstBinding binding; //defining a view binding to use.
    Adapter adapter; // defining an adapter to be user to set to recycler view.
    databaseHelper addrDatabase; //defining a database helper to use.
    List<addrObj> allAddr; //defining list for all addresses
    TextView noAddr; //defining xml elements
    RecyclerView recyclerView;

    public firstFragment() { // Required empty public constructor

    }


    public static firstFragment newInstance(String param1, String param2) { //unused, required for fragment.
        firstFragment fragment = new firstFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    private void displayList(List<addrObj> allAddrs) { //function to set the adapter for the recycler view so that the addresses can be displayed
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter(this,getActivity(),allAddrs);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false); //defining the view binding.
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addrDatabase = new databaseHelper(getActivity()); //creating DB helper to use

        noAddr = binding.noAddrtext; //defining xml elements to variables
        recyclerView = binding.showAddr;
        noAddr.setVisibility(View.GONE); //hiding one of the elements
        allAddr = addrDatabase.getAlladdr(); //sending query to DB to get all entries and to return a list

        if(allAddr.isEmpty()){ //if the DB is empty, read from the coordinate JSON file and add addresses to DB and the to return the list again.
            addrDatabase.deleteTable();
            addrDatabase.addAddrs(addAddrList());
            allAddr = addrDatabase.getAlladdr();
        }
        displayList(allAddr); //calling function to setup the recycler view to display the addresses

        binding.addAddrbutton.setOnClickListener(new View.OnClickListener() { //button on click to navigate to the second fragment (add address page)
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(firstFragment.this)
                        .navigate(R.id.firstTosecond);
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //creating on query listner to create search functionality on the main page.
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter the recycler view.
                if(!allAddr.isEmpty()) { //As long as the address list is not empty, attempt to filter the list based on user criteria
                    filter(newText);
                }
                return false;
            }
        });

    }

    public List<addrObj> addAddrList(){ //function to add the address to the DB
        List<Address> addresses;
        double lat,longV;

        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault()); //creating a geocode to get an address from the lat and long inputs
        List<addrObj> allAddrs = new ArrayList<>(); //creating a list to store all the addresses
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray jsonarray = obj.getJSONArray("coordinates"); //loading a JSON array from the input file
            for (int i = 0; i < jsonarray.length(); i++) { //iterate through the file to get all the coordinate values and addresses
                 JSONObject jsonobject = jsonarray.getJSONObject(i);
                 lat = Double.parseDouble(jsonobject.getString("latitude"));
                 longV = Double.parseDouble(jsonobject.getString("longitude")); //getting lat and long values from file

                 addresses = geocoder.getFromLocation(lat, longV, 1); //search for the address that corresponds to associated coordinates.

                 String address = addresses.get(0).getAddressLine(0).trim(); //getting address result from the search

                 allAddrs.add(new addrObj(address,lat,longV)); //creating a new address object and adding it to the list
            }
            Toast.makeText(getActivity(), "Added Addresses to list from file", Toast.LENGTH_SHORT).show(); //letting user know file was read
            return allAddrs; //return the list of addresses
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String loadJSONFromAsset() throws IOException { //function to read the json file
        String json;
        InputStream is;
        try {
            is = requireActivity().getAssets().open("coordinates.JSON");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void filter(String text) { //function to search through list of address based on search criteria and to then update the list
        List<addrObj> filteredList = new ArrayList<>();

        for (addrObj item : allAddr) { //searching the list for the input
            if (item.getAddress().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList); //filtering list based on criteria

    }
}