package com.example.mache.recetario2;

/**
 * Created by Mache on 07-05-2016.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class RecyclerViewHolder extends RecyclerView.ViewHolder  {
    // View holder for gridview recycler view as we used in listview
    public TextView title;
    public ImageView imageview;
    //nuevo
    public TextView categoria;

    public RecyclerViewHolder(View view) {
        super(view);
        // Find all views ids

        this.title = (TextView) view
                .findViewById(R.id.title);
        this.imageview = (ImageView) view
                .findViewById(R.id.image);
//nuevo
        this.categoria = (TextView) view
                .findViewById(R.id.categoria);


    }



}