package com.example.siasmobile.Onboarding.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class OnboardingPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Integer> layouts;

    public OnboardingPagerAdapter(Context context, List<Integer> layouts) {
        this.context = context;
        this.layouts = layouts;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layouts.get(position), container, false);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return layouts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
