package com.arm2.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arm2.model.CamXuc;
import com.arm2.nhatki2.EditMemoActivity;
import com.arm2.nhatki2.R;

import static com.arm2.model.Const.CAM_XUC_EDIT;
import static com.arm2.model.Const.INTEN_EDIT_CAM_XUC;


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
        TextView txtCamXucSelect =
                (TextView) view.findViewById(R.id.txtCamXucSelect);
        ImageView imgCamXucSelect =
                (ImageView) view.findViewById(R.id.imgCamXucSelect);
        LinearLayout lnCamXucSelect =
                (LinearLayout) view.findViewById(R.id.lnCamXucSelect);
        ImageView imgPenCamXucSeclect =
                (ImageView) view.findViewById(R.id.imgPenCamXucSeclect);
        final CamXuc camXuc = getItem(position);
        txtCamXucSelect.setText(camXuc.getNoiDungCamXuc());
        txtCamXucSelect.setTextColor(this.context.getResources().getColor(R.color.text_color));
        imgCamXucSelect.setImageResource(camXuc.getHinhCamXuc());

        if (this.context.getLocalClassName().equals("MemoActivity"))
        {
            imgPenCamXucSeclect.setVisibility(View.VISIBLE);
            lnCamXucSelect.setBackgroundColor(getContext()
                    .getResources()
                    .getColor(R.color.background_memo_color));
            lnCamXucSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCamXuc(camXuc,position);
                }
            });
        }

        return view;
    }

    private void getCamXuc(CamXuc camXuc, int position) {

            Intent intent = new Intent(this.context, EditMemoActivity.class);
            intent.setAction(INTEN_EDIT_CAM_XUC);
            Bundle bundle = new Bundle();
            bundle.putSerializable(CAM_XUC_EDIT,camXuc);
            intent.putExtras(bundle);
            this.context.startActivity(intent);
    }

}
