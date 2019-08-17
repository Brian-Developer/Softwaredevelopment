package com.cm0573.w16041611.k5;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantInfoAdapter extends ArrayAdapter<data> {

    private Activity Context;
    private List<data> restaurantList;
    public RestaurantInfoAdapter(Activity Context, List<data>restaurantList) {
        super(Context, R.layout.list_restaurant,restaurantList);
        this.Context = Context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = Context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.list_restaurant, null, true);
        ImageView im = listView.findViewById(R.id.im);
        TextView Name = listView.findViewById(R.id.name);
        TextView Number = listView.findViewById(R.id.number);
        TextView Address = listView.findViewById(R.id.address);

        data name = restaurantList.get(position);
        Picasso.get().load(name.getImage()).into(im);
        Name.setText(name.getName1());
        Number.setText(name.getNumber());
        Address.setText(name.getAddress());

        return listView;
    }
}
