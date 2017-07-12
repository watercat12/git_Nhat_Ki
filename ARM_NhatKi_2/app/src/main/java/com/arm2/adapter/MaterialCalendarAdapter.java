package com.arm2.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arm2.nhatki2.MaterialCalendar;
import com.arm2.nhatki2.MaterialCalendarFragment;
import com.arm2.nhatki2.R;

/**
 * Created by Maximilian on 9/1/14.
 */
public class MaterialCalendarAdapter extends BaseAdapter {
    // Variables
    private Context mContext;
    public static ViewHolder mHolder;
    int mWeekDayNames = 7;
    int mGridViewIndexOffset = 1;

    private static class ViewHolder {
        ImageView mSelectedDayImageView;
        TextView mTextView;
        ImageView mSavedEventImageView;
        RelativeLayout rlDay;
    }

    // Constructor
    public MaterialCalendarAdapter(Context context) {
        mContext = context;
        
    }
//-------------------------------------------------------------------------------
    @Override
    public int getCount() {
        if (MaterialCalendar.getmFirstDay() != -1
                && MaterialCalendar.getmNumDaysInMonth() != -1) {
            Log.d("GRID_COUNT", String.valueOf(mWeekDayNames + MaterialCalendar.getmFirstDay() + MaterialCalendar.getmNumDaysInMonth()));
            return mWeekDayNames
                    + MaterialCalendar.getmFirstDay()
                    + MaterialCalendar.getmNumDaysInMonth();
        }

        return mWeekDayNames;
    }
//-------------------------------------------------------------------------------
    @Override
    public Object getItem(int position) {
        return null;
    }
//-------------------------------------------------------------------------------
    @Override
    public long getItemId(int position) {
        return 0;
    }
//-------------------------------------------------------------------------------
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_material_day, parent, false);

            mHolder = new ViewHolder();

            if (convertView != null)
            {
                mHolder.mSelectedDayImageView = (ImageView)
                        convertView.findViewById(R.id.material_calendar_selected_day);
                mHolder.mTextView =
                        (TextView) convertView.findViewById(R.id.material_calendar_day);
                mHolder.rlDay =
                        (RelativeLayout) convertView.findViewById(R.id.rlDay);
                Typeface typeFace = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/HeraBig.ttf");
                //mHolder.mTextView.setTypeface(typeFace);
                mHolder.mSavedEventImageView =
                        (ImageView) convertView.findViewById(R.id.saved_event_imageView);
                convertView.setTag(mHolder);
            }
        }
        else
        {
            mHolder = (ViewHolder) convertView.getTag();
        }

        if (mHolder.mSelectedDayImageView != null) {
            GridView gridView = (GridView) parent;
            Log.d("ITEM_CHECKED_POSITION", String.valueOf(gridView.isItemChecked(position)));
            if (gridView.isItemChecked(position)) {
                Animation feedBackAnimation = AnimationUtils.loadAnimation(mContext, R.anim.selected_day_feedback);
                mHolder.mSelectedDayImageView.setVisibility(View.VISIBLE);

                if (feedBackAnimation != null) {
                    mHolder.mSelectedDayImageView.startAnimation(feedBackAnimation);
                }
            } else {
                mHolder.mSelectedDayImageView.setVisibility(View.INVISIBLE);
            }
        }


        if (mHolder.mTextView != null) {
            setCalendarDay(position);
        }

        if (mHolder.mSavedEventImageView != null) {
            setSavedEvent(position);
        }

        return convertView;
    }

//-------------------------------------------------------------------------------

    private void setCalendarDay(int position) {

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , 100);


        int isColumSunday =0;
        if (position <= mWeekDayNames - mGridViewIndexOffset + MaterialCalendar.getmFirstDay()) {
            mHolder.mTextView
                    .setTextColor(mContext
                            .getResources()
                            .getColor(R.color.calendar_day_text_color));

            Log.d("NO_CLICK_POSITION", String.valueOf(position));
        }

        if (position <= 6)
        {
            mHolder.rlDay.setBackgroundColor(mContext
                    .getResources()
                    .getColor(R.color.day_color));
        }
        if (position % 7 == 0)
        {
            isColumSunday =1;
            mHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.sunday));
        }
        switch (position) {
            case 0:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.sunday));
                //mHolder.mTextView.setTypeface(Typeface.DEFAULT);
                break;

            case 1:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.monday));
                //mHolder.mTextView.setTypeface(Typeface.DEFAULT);

                break;

            case 2:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.tuesday));
                //mHolder.mTextView.setTypeface(Typeface.DEFAULT);

                break;

            case 3:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.wednesday));
                //mHolder.mTextView.setTypeface(Typeface.DEFAULT);

                break;

            case 4:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.thursday));
                //mHolder.mTextView.setTypeface(Typeface.DEFAULT);

                break;

            case 5:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.friday));
                //mHolder.mTextView.setTypeface(Typeface.DEFAULT);

                break;

            case 6:
                mHolder.mTextView.setText(mContext.getResources().getString(R.string.saturday));
                //mHolder.mTextView.setTypeface(Typeface.DEFAULT);

                break;

            default:
                Log.d("CURRENT_POSITION", String.valueOf(position));
                mHolder.rlDay
                        .setBackground
                                (mContext.getResources().getDrawable(R.drawable.date_vien));
                if (position < mWeekDayNames + MaterialCalendar.getmFirstDay()) {
                    Log.d("BLANK_POSITION", "This is a blank day");
                    mHolder.mTextView.setText("");
                    //mHolder.mTextView.setTypeface(Typeface.DEFAULT);
                } else {
                    mHolder.mTextView.setText(String.valueOf(position - (mWeekDayNames - mGridViewIndexOffset) -
                            MaterialCalendar.getmFirstDay()));
                    //mHolder.mTextView.setTypeface(Typeface.DEFAULT_BOLD);

                    if (MaterialCalendar.getmCurrentDay() != -1) {
                        int startingPosition = mWeekDayNames - mGridViewIndexOffset + MaterialCalendar.getmFirstDay();
                        int currentDayPosition = startingPosition + MaterialCalendar.getmCurrentDay();

                        if (position == currentDayPosition) {
                            mHolder.mTextView.setTextColor(mContext.getResources().getColor(
                                    R.color.calendar_current_number_text_color));
                        } else {
                            if (isColumSunday == 1)
                            {
                                mHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.sunday));
                            }
                            else
                            {
                                mHolder.mTextView.setTextColor(mContext.getResources().getColor(
                                        R.color.calendar_number_text_color));
                            }

                        }
                    } else {
                        if (isColumSunday == 1)
                        {
                            mHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.sunday));
                        }
                        else
                        {
                            mHolder.mTextView.setTextColor(mContext.getResources().getColor(
                                    R.color.calendar_number_text_color));
                        }
                    }
                }
                break;
        }
    }

    private void setSavedEvent(int position) {
        // Reset saved position indicator
        mHolder.mSavedEventImageView.setVisibility(View.INVISIBLE);

        if (MaterialCalendar.getmFirstDay() != -1 && MaterialCalendarFragment.mSavedEventDays != null &&
                MaterialCalendarFragment.mSavedEventDays.size() > 0) {

            int startingPosition = mWeekDayNames - mGridViewIndexOffset + MaterialCalendar.getmFirstDay();
            Log.d("SAVED_EVENT_STARTING_POS", String.valueOf(startingPosition));
            if (position > startingPosition) {
                for (int i = 0; i < MaterialCalendarFragment.mSavedEventDays.size(); i++) {
                    int savedEventPosition = startingPosition + MaterialCalendarFragment.mSavedEventDays.get(i);

                    Log.d("POSITION", String.valueOf(position));
                    Log.d("SAVED_POSITION", String.valueOf(savedEventPosition));
                    if (position == savedEventPosition) {
                        mHolder.mSavedEventImageView.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                mHolder.mSavedEventImageView.setVisibility(View.INVISIBLE);
            }
        }
    }

}
