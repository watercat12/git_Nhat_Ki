package com.arm2.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arm2.model.SelectHinh;
import com.arm2.nhatki2.R;

/**
 * Created by ARM on 12-May-17.
 */

public class SelectHinhAdapter extends ArrayAdapter<SelectHinh> {
    Activity context;
    int resource;
    public SelectHinhAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = this.context.getLayoutInflater().inflate(this.resource,null);
        ImageView imgHinhPhuongThuc = (ImageView) view.findViewById(R.id.imgHinhPhuongThuc);
        TextView txtTenPhuongThuc = (TextView) view.findViewById(R.id.txtTenPhuongThuc);

        SelectHinh selectHinh = getItem(position);
        txtTenPhuongThuc.setText(selectHinh.getTenPhuongThuc());
        txtTenPhuongThuc.setTextColor(this.context
                .getResources().getColor(R.color.text_color));
        imgHinhPhuongThuc.setImageResource(selectHinh.getHinhPhuongThuc());
        return view;
    }
}
