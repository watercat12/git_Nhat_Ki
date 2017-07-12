package com.arm2.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arm2.model.CamXuc;
import com.arm2.nhatki2.R;


/**
 * Created by ARM on 28-Apr-17.
 */

public class EditMemoAdapter extends ArrayAdapter<CamXuc> {
    Activity context;
    int resource;
    public EditMemoAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = this.context.getLayoutInflater().inflate(this.resource,null);
        ImageView imgCamXucSelect = (ImageView) view.findViewById(R.id.imgCamXucSelect);
        TextView txtCamXucSelect = (TextView) view.findViewById(R.id.txtCamXucSelect);
        txtCamXucSelect.setVisibility(View.GONE);
        CamXuc camXuc = getItem(position);
        imgCamXucSelect.setImageResource(camXuc.getHinhCamXuc());
        return view;
    }
}
