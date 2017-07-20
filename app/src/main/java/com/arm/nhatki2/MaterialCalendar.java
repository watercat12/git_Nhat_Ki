package com.arm.nhatki2;

import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.arm.adapter.MaterialCalendarAdapter;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Created by Maximilian on 9/2/14.
 */
public class MaterialCalendar {
    // Variables
    private static int mMonth = -1;
    private static int mYear = -1;
    private static int mCurrentDay = -1;
    private static int mCurrentMonth = -1;
    private static int mCurrentYear = -1;
    private static int mFirstDay = -1;
    private static int mNumDaysInMonth = -1;


    public static int getmMonth() {
        return mMonth;
    }

    public static void setmMonth(int mMonth) {
        MaterialCalendar.mMonth = mMonth;
    }

    public static int getmYear() {
        return mYear;
    }

    public static void setmYear(int mYear) {
        MaterialCalendar.mYear = mYear;
    }

    public static int getmCurrentDay() {
        return mCurrentDay;
    }

    public static void setmCurrentDay(int mCurrentDay) {
        MaterialCalendar.mCurrentDay = mCurrentDay;
    }

    public static int getmCurrentMonth() {
        return mCurrentMonth;
    }

    public static void setmCurrentMonth(int mCurrentMonth) {
        MaterialCalendar.mCurrentMonth = mCurrentMonth;
    }

    public static int getmCurrentYear() {
        return mCurrentYear;
    }

    public static void setmCurrentYear(int mCurrentYear) {
        MaterialCalendar.mCurrentYear = mCurrentYear;
    }

    public static int getmFirstDay() {
        return mFirstDay;
    }

    public static void setmFirstDay(int mFirstDay) {
        MaterialCalendar.mFirstDay = mFirstDay;
    }

    public static int getmNumDaysInMonth() {
        return mNumDaysInMonth;
    }

    public static void setmNumDaysInMonth(int mNumDaysInMonth) {
        MaterialCalendar.mNumDaysInMonth = mNumDaysInMonth;
    }
//---------------------------------------------------------------------------
    protected static void getInitialCalendarInfo() {
        Calendar cal = Calendar.getInstance();

        if (cal != null) {
            mNumDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            mMonth = cal.get(Calendar.MONTH);
            mYear = cal.get(Calendar.YEAR);

            mCurrentDay = cal.get(Calendar.DAY_OF_MONTH);
            mCurrentMonth = mMonth;
            mCurrentYear = mYear;

            getFirstDay(mMonth, mYear);


        }
    }
//---------------------------------------------------------------------------
    public static void refreshCalendar(TextView monthTextView, GridView calendarGridView
            , MaterialCalendarAdapter materialCalendarAdapter
            , int month, int year) {

        checkCurrentDay(month, year);
        getNumDayInMonth(month, year);
        getFirstDay(month, year);

        if (monthTextView != null) {
            monthTextView.setText(getMonthName(month) + " " + year);
        }

        // Clear Saved Events ListView count when changing calendars
        if (MaterialCalendarFragment.mSavedEventsAdapter != null) {
            MaterialCalendarFragment.mNumEventsOnDay = -1;
            MaterialCalendarFragment.mSavedEventsAdapter.notifyDataSetChanged();

        }

        MaterialCalendarFragment.getSavedEventsForCurrentMonth();

        if (materialCalendarAdapter != null) {
            if (calendarGridView != null) {
                calendarGridView.setItemChecked(calendarGridView.getCheckedItemPosition(), false);
            }

            materialCalendarAdapter.notifyDataSetChanged();
        }
    }
//-----------------------tải lại lịch --------------------------------------
    public static void refreshCalendar1(TextView monthTextView, GridView calendarGridView
            , MaterialCalendarAdapter materialCalendarAdapter
            ) {



        // Clear Saved Events ListView count when changing calendars
        if (MaterialCalendarFragment.mSavedEventsAdapter != null) {
            MaterialCalendarFragment.mNumEventsOnDay = -1;
            MaterialCalendarFragment.mSavedEventsAdapter.notifyDataSetChanged();

        }

        MaterialCalendarFragment.getSavedEventsForCurrentMonth();

        if (materialCalendarAdapter != null) {
            if (calendarGridView != null) {
                calendarGridView.setItemChecked(calendarGridView.getCheckedItemPosition(), false);
            }

            materialCalendarAdapter.notifyDataSetChanged();
        }
    }
//---------------------------------------------------------------------------
    private static String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }
//---------------------------------------------------------------------------
    private static void checkCurrentDay(int month, int year) {
        if (month == mCurrentMonth && year == mCurrentYear) {
            Calendar cal = Calendar.getInstance();
            mCurrentDay = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            mCurrentDay = -1;
        }
    }
//---------------------------------------------------------------------------
    private static void getNumDayInMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        if (cal != null) {
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);
            mNumDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        }
    }
//---------------------------------------------------------------------------
    private static void getFirstDay(int month, int year) {
        Calendar cal = Calendar.getInstance();
        if (cal != null) {
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.DAY_OF_MONTH, 1);

            switch (cal.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    mFirstDay = 0;
                    break;

                case Calendar.MONDAY:
                    mFirstDay = 1;
                    break;

                case Calendar.TUESDAY:
                    mFirstDay = 2;
                    break;

                case Calendar.WEDNESDAY:
                    mFirstDay = 3;
                    break;

                case Calendar.THURSDAY:
                    mFirstDay = 4;
                    break;

                case Calendar.FRIDAY:
                    mFirstDay = 5;
                    break;

                case Calendar.SATURDAY:
                    mFirstDay = 6;
                    break;

                default:
                    break;
            }
        }
    }
//---------------------------------------------------------------------------
    // Call in View.OnClickListener for Previous ImageView
    protected static void previousOnClick(ImageView previousImageView, TextView monthTextView,
                                          GridView calendarGridView, MaterialCalendarAdapter materialCalendarAdapter) {
        if (previousImageView != null && mMonth != -1 && mYear != -1) {
            previousMonth(monthTextView, calendarGridView, materialCalendarAdapter);
        }
    }
//---------------------------------------------------------------------------
    // Call in View.OnClickListener for Next ImageView
    protected static void nextOnClick(ImageView nextImageView, TextView monthTextView,
                                      GridView calendarGridView,
                                      MaterialCalendarAdapter materialCalendarAdapter) {
        if (nextImageView != null && mMonth != -1 && mYear != -1) {
            nextMonth(monthTextView, calendarGridView, materialCalendarAdapter);
        }
    }
//---------------------------------------------------------------------------
    private static void previousMonth(TextView monthTextView, GridView calendarGridView,
                                      MaterialCalendarAdapter materialCalendarAdapter) {
        if (mMonth == 0) {
            mMonth = 11;
            mYear = mYear - 1;
        } else {
            mMonth = mMonth - 1;
        }

        refreshCalendar(monthTextView, calendarGridView, materialCalendarAdapter, mMonth, mYear);
    }
//---------------------------------------------------------------------------
    private static void nextMonth(TextView monthTextView, GridView calendarGridView,
                                  MaterialCalendarAdapter materialCalendarAdapter) {
        if (mMonth == 11) {
            mMonth = 0;
            mYear = mYear + 1;
        } else {
            mMonth = mMonth + 1;
        }

        refreshCalendar(monthTextView, calendarGridView, materialCalendarAdapter, mMonth, mYear);
    }
//---------------------------------------------------------------------------
    // Call in GridView.OnItemClickListener for custom Calendar GirdView
    protected static void selectCalendarDay(MaterialCalendarAdapter materialCalendarAdapter, int position) {

        int weekPositions = 6;
        int noneSelectablePositions = weekPositions + mFirstDay;

        if (position > noneSelectablePositions) {
            getSelectedDate(position, mMonth, mYear);

            if (materialCalendarAdapter != null) {
                materialCalendarAdapter.notifyDataSetChanged();
            }
        }
    }
//---------------------------------------------------------------------------
    private static void getSelectedDate(int selectedPosition, int month, int year) {
        int weekPositions = 6;
        int dateNumber = selectedPosition - weekPositions - mFirstDay;

    }
}

