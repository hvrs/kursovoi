package com.example.praktikamobile;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomGridAdapter extends ArrayAdapter<String[]> {

    private Context mContext;
    private String[][] mItems;

    public CustomGridAdapter(Context context, String[][] items) {
        super(context, 0, items);
        mContext = context;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItem = convertView;
        if (gridItem == null) {
            gridItem = LayoutInflater.from(mContext).inflate(R.layout.grid_item_layout, parent, false);
        }

        String[] currentItem = mItems[position];

        ImageView imageView = gridItem.findViewById(R.id.imageView);
        String imageUrl = "https://smtpservers.ru/projects/praktikaMobile/uploads/"+currentItem[0];
        Picasso.get()
                .load(imageUrl)
                .into(imageView);



        return gridItem;
    }
}