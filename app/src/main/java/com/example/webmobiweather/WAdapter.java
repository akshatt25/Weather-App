package com.example.webmobiweather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WAdapter extends RecyclerView.Adapter<WAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Model> list;

    public WAdapter(Context context, ArrayList<Model> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public WAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mylay,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WAdapter.ViewHolder holder, int position) {
        Model model = list.get(position);
        if (model != null) {
            Glide.with(context)
                    .load("http:"+model.getIcon())
                    .into(holder.cicon);
            holder.itemView.setBackgroundColor(0000000);
            holder.wSpeed.setText(model.getWSpeed()+"km/h");
            holder.temp.setText(Integer.toString(model.getTemp())+"°C");
            holder.humid.setText(model.getHumidity()+"%");
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            SimpleDateFormat output = new SimpleDateFormat("hh:mm aa ");
            try
            {
                Date t = input.parse(model.getTime());
                holder.time.setText(output.format(t));
            }
            catch(ParseException e)
            {
                e.printStackTrace();

            }
        } else {
            Log.e("WAdapter", "Model object at position " + position + " is null");
        }



    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        TextView temp;
        TextView humid;
        TextView wSpeed;
        ImageView cicon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            temp = itemView.findViewById(R.id.vtemp);
            humid = itemView.findViewById(R.id.humid);
            wSpeed = itemView.findViewById(R.id.wSpeed);
            cicon = itemView.findViewById(R.id.cicon);

        }


    }




}
