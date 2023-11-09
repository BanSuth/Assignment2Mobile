package com.example.assign2mobile;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private LayoutInflater inflater;
    private List<addrObj> addrObjs; //defining a list of address object to be used.
    private Fragment frag;

    Adapter(Fragment frag, Context context, List<addrObj> addrObjs1){ //constructer thats take fragment, activity context, and the list of address.
        this.frag=frag;
        this.inflater = LayoutInflater.from(context);
        this.addrObjs = addrObjs1;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.custom_list_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) { //this function is used to set the display text

        String  uAddr     = addrObjs.get(i).getAddress();
        int    id       = addrObjs.get(i).getId(); //getting address information from the address object list
        double lati = addrObjs.get(i).getLatitude();
        double longi = addrObjs.get(i).getLongitude();

        viewHolder.nContent.setText(uAddr);
        viewHolder.nID.setText(String.valueOf(id)); //Setting the text ID number and address field

        viewHolder.parent.setOnClickListener(new View.OnClickListener() { // on click listner for each of the address entrys in the recycler view.
            @Override
            public void onClick(View view) { //If a entry is pressed, pack entry into bundle and navigate to the thirdFragment (View address page)
                Bundle bundle = new Bundle(); //creating a bundle to pass into the navigate function
                bundle.putInt("id", id);
                bundle.putDouble("lat",lati); //loading address information
                bundle.putDouble("longi",longi);
                bundle.putString("address",uAddr);

                NavHostFragment.findNavController(frag).navigate(R.id.firstTothird,bundle); //navigating to the third fragment
            }
        });
    }

    @Override
    public int getItemCount() {
        return addrObjs.size();
    } //function to return the size of the address list

    public void filterList(List<addrObj> filteredList) { //this function is used to set the adapter to display a filter list. Used for the search function.
        addrObjs = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{ //view holder used to display the addresses
        TextView nContent,nID;
        CardView parent;
        RelativeLayout parent2;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.cardView);
            parent2 = itemView.findViewById(R.id.mainLayout); //getting all the ids of the list view.
            nContent   = itemView.findViewById(R.id.content_txt);
            nID     = itemView.findViewById(R.id.id_text);

        }
    }
}
