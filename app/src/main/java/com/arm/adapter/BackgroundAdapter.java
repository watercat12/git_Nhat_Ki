package com.arm.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.arm.model.Background;
import com.arm.nhatki2.R;

import java.util.List;

import static com.arm.nhatki2.KhoaActivity.saveValue;

/**
 * Created by ARM on 29-Mar-17.
 */

public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.MyViewHolder> {

    private List<Background> backgrounds;
    Activity activity;

    public BackgroundAdapter(List<Background> backgrounds,Activity activity) {
        this.backgrounds = backgrounds;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_background,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Background background = backgrounds.get(position);
        holder.imgBackground.setImageResource(background.getHinhBackground());
    }

    @Override
    public int getItemCount() {
        return backgrounds.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgBackground;

        public MyViewHolder(final View view) {
            super(view);
            imgBackground = (ImageView) view.findViewById(R.id.imgBackground);
            imgBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    luuBackground(getAdapterPosition());
                    Toast.makeText(view.getContext()
                            , view.getContext().getResources().getString(R.string.da_doi)
                            , Toast.LENGTH_SHORT).show();
                    activity.finish();

                }
            });
        }
    }

    private void luuBackground(int position) {
        saveValue.setSaved_Last_Background_Integer(position);

    }

}
