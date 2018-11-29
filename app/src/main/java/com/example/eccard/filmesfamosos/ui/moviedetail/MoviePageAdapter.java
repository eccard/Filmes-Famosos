package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.eccard.filmesfamosos.R;

public class MoviePageAdapter extends FragmentStatePagerAdapter {

    private static final int TOTAL_OF_FRGS = 2;
    private final Context context;

    public MoviePageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frg = null;

        switch (position) {
            case 0:
                frg = new FrgSummary();
                break;
            case 1:
                frg = new FrgTrailers();
                break;
            default:
                break;
        }

        return frg;
    }

    @Override
    public int getCount() {
        return TOTAL_OF_FRGS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence charSequence;

        switch (position){
            case 0:
                charSequence = context.getString(R.string.summary);
                break;
            case 1:
                charSequence = context.getString(R.string.videos);
                break;
            default:
                charSequence = context.getString(R.string.review);
                break;
        }

        return charSequence;
    }
}
