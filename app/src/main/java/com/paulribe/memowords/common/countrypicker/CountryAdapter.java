package com.paulribe.memowords.common.countrypicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulribe.memowords.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CountryAdapter extends ArrayAdapter<CountryItem> {

    private static final int ITEM_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;

    public CountryAdapter(Context context, ArrayList<CountryItem> countryList) {
        super(context, 0, countryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_country, parent, false);
        }
        ImageView imageViewFlag = convertView.findViewById(R.id.icon);
        TextView textViewName = convertView.findViewById(R.id.name);
        CountryItem currentItem = getItem(position);
        if (currentItem != null) {
            if(position == 0) {
                imageViewFlag.setVisibility(View.GONE);
            } else {
                imageViewFlag.setImageResource(currentItem.getFlagImage());
            }
            textViewName.setText(currentItem.getCountryName().toString(convertView.getContext()));
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_country, parent, false);
        }
        ImageView imageViewFlag = convertView.findViewById(R.id.icon);
        TextView textViewName = convertView.findViewById(R.id.name);
        CountryItem currentItem = getItem(position);
        if (currentItem != null) {
            if(position == 0) {
                ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
                layoutParams.height = 1;
                convertView.setLayoutParams(layoutParams);
            } else {
                ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
                layoutParams.height = ITEM_HEIGHT;
                convertView.setLayoutParams(layoutParams);
                imageViewFlag.setImageResource(currentItem.getFlagImage());
            }
            textViewName.setText(currentItem.getCountryName().toString(convertView.getContext()));
        }
        return convertView;
    }
}
