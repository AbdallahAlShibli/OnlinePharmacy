package com.pharmacy.onlinepharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ReviewDataAdapter extends ArrayAdapter<ReviewDataModel> implements View.OnClickListener {

    ArrayList<ReviewDataModel> dataModels;
    Context myContext;

    static class ViewHolder {
        TextView name;
        TextView review;
        RatingBar ratingBar;
        String id;
        ImageView imageView;
    }

    public ReviewDataAdapter(ArrayList<ReviewDataModel> data, Context context) {
        super(context, R.layout.comments_layout, data);

        this.dataModels = data;
        this.myContext = context;
    }


    @Override
    public void onClick(View view) {

        int position = (Integer) view.getTag();
        Object object = getItem(position);

        ReviewDataModel dataModel = (ReviewDataModel) object;

        Toast.makeText(getContext(), "" + dataModel.getName(), Toast.LENGTH_SHORT).show();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ReviewDataModel dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.comments_layout, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.review = (TextView) convertView.findViewById(R.id.user_comment);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView2);

            viewHolder.id = "";

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.name.setText(dataModel.getName());
        viewHolder.review.setText(dataModel.getReview());
        viewHolder.ratingBar.setRating(dataModel.getRatingStars());

        viewHolder.id = dataModel.getId();

        viewHolder.imageView.setImageResource(R.mipmap.ic_launcher_round);


        return convertView;
    }
}
