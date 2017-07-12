package com.arm2.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arm2.model.Font;
import com.arm2.model.SaveValue;
import com.arm2.nhatki2.R;


/**
 * Created by ARM on 24-Mar-17.
 */

public class FontAdapter extends ArrayAdapter<Font> {
    Activity context;
    int resource;
    SaveValue saveValue;
    public Typeface typeFace;
    public FontAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;

    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = this.context.getLayoutInflater().inflate(this.resource,null);
        TextView txtFont = (TextView) view.findViewById(R.id.txtFont);
        Font font = getItem(position);
        txtFont.setText(font.getTenFont());
        txtFont.setTextColor(this.context.getResources().getColor(R.color.text_color));
        /*typeFace = Typeface.createFromAsset(this.context
                .getAssets(), "fonts/"
                +font.getTenFont()
                +".ttf");*/
        txtFont.setTypeface(font.getKieuFont());

        return view;
    }

    private void xuLySaveFont(int position) {
    }
}
