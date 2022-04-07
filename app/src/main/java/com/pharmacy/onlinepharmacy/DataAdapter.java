package com.pharmacy.onlinepharmacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener {

    ArrayList<DataModel> dataModels;
    Context myContext;

    static class ViewHolder {
        TextView medical_name;
        TextView medical_description;
        TextView medical_price;
        ImageView imageView;
    }

    public DataAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.view_medicien_info_layout, data);

        this.dataModels = data;
        this.myContext = context;
    }


    @Override
    public void onClick(View view) {

        int position = (Integer) view.getTag();
        Object object = getItem(position);

        DataModel dataModel = (DataModel) object;

        Toast.makeText(getContext(), "" + dataModel.getMedicalName(), Toast.LENGTH_SHORT).show();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DataModel dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.view_medicien_info_layout, parent, false);
            viewHolder.medical_name = (TextView) convertView.findViewById(R.id.medical_name);
            viewHolder.medical_description = (TextView) convertView.findViewById(R.id.medical_description);
            viewHolder.medical_price = (TextView) convertView.findViewById(R.id.medical_price);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.medical_image);


            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.medical_name.setText("Medical Name: "+dataModel.getMedicalName());
        viewHolder.medical_description.setText("Medical Description: "+dataModel.getMedicalDescription());
        viewHolder.medical_price.setText("Medical Price: "+dataModel.getMedicalPrice()+" OR");

        SharedPreferences sharedpreferences = myContext.getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("medical_id", dataModel.getMedicalID());
        editor.putString("medical_cover_url", dataModel.getMedicalCoverURL());
        editor.apply();

        viewHolder.imageView.setImageResource(R.mipmap.ic_launcher_round);

        Picasso.get().load(dataModel.getMedicalCoverURL()).into(viewHolder.imageView);



        return convertView;
    }
}
