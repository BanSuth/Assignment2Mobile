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
    private List<addrObj> addrObjs;

    private Fragment frag;

    Adapter(Fragment frag, Context context, List<addrObj> addrObjs1){
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String  uAddr     = addrObjs.get(i).getAddress();
        int    id       = addrObjs.get(i).getId();
        double lati = addrObjs.get(i).getLatitude();
        double longi = addrObjs.get(i).getLongitude();

        viewHolder.nContent.setText(uAddr);

        viewHolder.nID.setText(String.valueOf(id));

        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putDouble("lat",lati);
                bundle.putDouble("longi",longi);
                bundle.putString("address",uAddr);


                NavHostFragment.findNavController(frag).navigate(R.id.firstTothird,bundle);
               // inflater.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addrObjs.size();
    }

    public void filterList(List<addrObj> filteredList) {
        addrObjs = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nTitle,nContent,nID;
        CardView parent;
        RelativeLayout parent2;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.cardView);
            parent2 = itemView.findViewById(R.id.mainLayout);
            nContent   = itemView.findViewById(R.id.content_txt);
            nID     = itemView.findViewById(R.id.id_text);

        }
    }
}
