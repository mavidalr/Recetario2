package com.example.mache.recetario2;

/**
 * Created by Mache on 07-05-2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

public class RecyclerView_Adapter extends
        RecyclerView.Adapter<com.example.mache.recetario2.RecyclerViewHolder> {// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<com.example.mache.recetario2.Data_Model> arrayList;
    private Context context;

    public RecyclerView_Adapter(Context context,
                                ArrayList<com.example.mache.recetario2.Data_Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(com.example.mache.recetario2.RecyclerViewHolder holder, int position) {
        final com.example.mache.recetario2.Data_Model model = arrayList.get(position);

        com.example.mache.recetario2.RecyclerViewHolder mainHolder = (com.example.mache.recetario2.RecyclerViewHolder) holder;// holder

        Bitmap image = BitmapFactory.decodeResource(context.getResources(),
                model.getImage());// This will convert drawbale image into
        // bitmap

        // setting title
        mainHolder.title.setText(model.getTitle());
        mainHolder.imageview.setImageBitmap(image);
        //nuevo
        mainHolder.categoria.setText(model.getCategoria());


    }

    @Override
    public com.example.mache.recetario2.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_row, viewGroup, false);
        com.example.mache.recetario2.RecyclerViewHolder listHolder = new com.example.mache.recetario2.RecyclerViewHolder(mainGroup);
        return listHolder;

    }

}