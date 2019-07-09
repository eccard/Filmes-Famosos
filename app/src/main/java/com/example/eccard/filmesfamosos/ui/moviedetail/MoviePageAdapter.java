package com.example.eccard.filmesfamosos.ui.moviedetail;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.eccard.filmesfamosos.R;

class MoviePageAdapter extends FragmentStatePagerAdapter {

    private static final int TOTAL_OF_FRGS = 3;
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
            case 2 :
                frg = new FrgReviews();
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
