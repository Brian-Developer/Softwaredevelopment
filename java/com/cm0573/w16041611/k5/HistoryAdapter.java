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

public class HistoryAdapter extends ArrayAdapter<history> {
    private Activity Context;
    private List<history> historyList;
    public HistoryAdapter(Activity Context, List<history>historyList){
        super(Context, R.layout.history_list,historyList);
        this.Context = Context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = Context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.history_list, null, true);
        ImageView im = listView.findViewById(R.id.im);
        TextView Name = listView.findViewById(R.id.restaurant);
        TextView Date = listView.findViewById(R.id.date);
        TextView Time = listView.findViewById(R.id.time);
        TextView Address = listView.findViewById(R.id.address);

        history name = historyList.get(position);
        Picasso.get().load(name.getImage()).into(im);
        Name.setText(name.getRestaurant());
        Date.setText(name.getDate());
        Time.setText(name.getTime());
        Address.setText(name.getAddress());

        return listView;
    }
}
