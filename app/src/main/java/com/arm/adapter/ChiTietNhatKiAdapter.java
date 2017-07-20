package com.arm.adapter;

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

import com.arm.model.ChiTietNhatKi;
import com.arm.nhatki2.R;
import com.arm.nhatki2.VietNhatKiActivity;

import static com.arm.nhatki2.MainActivity.EDIT_NHAT_KI;
import static com.arm.nhatki2.MainActivity.NHAT_KI_DOI_TUONG_BUNDLE;

/**
 * Created by ARM on 18-Mar-17.
 */

public class ChiTietNhatKiAdapter  extends ArrayAdapter<ChiTietNhatKi> {
    Activity context;
    int resource;

    public ChiTietNhatKiAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = this.context.getLayoutInflater().inflate(this.resource,null);

        LinearLayout itemNhatKi = (LinearLayout) view.findViewById(R.id.itemNhatKi);
        ImageView imgHinhTitle = (ImageView) view.findViewById(R.id.imgHinhTitle);
        ImageView imgHinhCamXuc = (ImageView) view.findViewById(R.id.imgHinhCamXuc);
        ImageView imgHinhDinhKem = (ImageView) view.findViewById(R.id.imgHinhDinhKem);
        ImageView imgHinhTime = (ImageView) view.findViewById(R.id.imgHinhTime);
        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView txtCamXuc = (TextView) view.findViewById(R.id.txtCamXuc);
        TextView txtNoiDung = (TextView) view.findViewById(R.id.txtNoiDung);
        TextView txtTime = (TextView) view.findViewById(R.id.txtTime);

        final ChiTietNhatKi nhatKi = getItem(position);
        imgHinhCamXuc.setImageResource(nhatKi.getHinhCamXuc());
        imgHinhDinhKem.setImageResource(nhatKi.getHinhDinhKem());
        txtTitle.setText(nhatKi.getTieuDe());
        txtCamXuc.setText(nhatKi.getNoiDungCamXuc());
        txtNoiDung.setText(nhatKi.getNoiDung());
        txtTime.setText(context.getString(R.string.write_at)+" : "+nhatKi.getDateNgayThangNam()+"  "+ nhatKi.getTime()+"");

        itemNhatKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyEditNhatKi(nhatKi);
            }
        });

        return view;
    }

    private void xuLyEditNhatKi(ChiTietNhatKi nhatKi) {
        Intent intent = new Intent(this.context, VietNhatKiActivity.class);
        intent.setAction(EDIT_NHAT_KI);
        Bundle bundle = new Bundle();
        bundle.putSerializable(NHAT_KI_DOI_TUONG_BUNDLE,nhatKi);
        intent.putExtras(bundle);
        this.context.startActivity(intent);
    }


}
