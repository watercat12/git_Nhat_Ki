package com.arm.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arm.model.CamXuc;
import com.arm.nhatki2.R;

import static com.arm.nhatki2.MainActivity.CAM_XUC_SELECT;

/**
 * Created by ARM on 22-Mar-17.
 */

public class CamXucAdapter extends ArrayAdapter<CamXuc> {
    Activity context;
    int resource;

    public CamXucAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = this.context.getLayoutInflater().inflate(this.resource,null);
        TextView txtCamXucSelect = (TextView) view.findViewById(R.id.txtCamXucSelect);
        ImageView imgCamXucSelect = (ImageView) view.findViewById(R.id.imgCamXucSelect);
        final CamXuc camXuc = getItem(position);
        txtCamXucSelect.setText(camXuc.getNoiDungCamXuc());
        imgCamXucSelect.setImageResource(camXuc.getHinhCamXuc());

        return view;
    }

    private void xuLyLayTextCamXuc(CamXuc camXuc, int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CAM_XUC_SELECT,camXuc);
        intent.putExtras(bundle);

    }

}
