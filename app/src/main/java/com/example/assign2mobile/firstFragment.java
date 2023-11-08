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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link firstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class firstFragment extends Fragment {


    private FragmentFirstBinding binding;

    Adapter adapter;

    databaseHelper addrDatabase;

    List<addrObj> allAddr;

    TextView noAddr;
    RecyclerView recyclerView;

    public firstFragment() {
        // Required empty public constructor
    }


    public static firstFragment newInstance(String param1, String param2) {
        firstFragment fragment = new firstFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    private void displayList(List<addrObj> allAddrs) {
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
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addrDatabase = new databaseHelper(getActivity());

        noAddr = binding.noAddrtext;
        noAddr.setVisibility(View.GONE);
        allAddr = addrDatabase.getAlladdr();
        recyclerView = binding.showAddr;

        if(allAddr.isEmpty()){
            addrDatabase.deleteTable();
            addrDatabase.addAddrs(addAddrList());
            allAddr = addrDatabase.getAlladdr();
        }
        displayList(allAddr);

        binding.addAddrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(firstFragment.this)
                        .navigate(R.id.firstTosecond);
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                if(!allAddr.isEmpty()) {
                    filter(newText);
                }
                return false;
            }
        });

    }

    public List<addrObj> addAddrList(){
        List<Address> addresses;
        double lat,longV;
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        List<addrObj> allAddrs = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray jsonarray = obj.getJSONArray("coordinates");
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                 lat = Double.parseDouble(jsonobject.getString("latitude"));
                 longV = Double.parseDouble(jsonobject.getString("longitude"));

                addresses = geocoder.getFromLocation(lat, longV, 1);

                String address = addresses.get(0).getAddressLine(0).trim();

                allAddrs.add(new addrObj(address,lat,longV));
            }
            Toast.makeText(getActivity(), "Added Addresses to list from file", Toast.LENGTH_SHORT).show();
            return allAddrs;
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String loadJSONFromAsset() throws IOException {
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

    private void filter(String text) {
        List<addrObj> filteredList = new ArrayList<>();

        for (addrObj item : allAddr) {
            if (item.getAddress().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);

    }
}