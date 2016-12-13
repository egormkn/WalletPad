package su.gear.walletpad.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ChildrenPagesAdapter extends PagerAdapter {

    private List<View> views;

    public ChildrenPagesAdapter(ViewGroup group) {
        views = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            views.add(group.getChildAt(i));
        }
        group.removeAllViews();
    }

    public View getChildAt(int position) {
        return views.get(position);
    }

    public View findViewById(int id) {
        for (View page : views) {
            View found = page.findViewById(id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View view = views.get(position);
        view.setVisibility(View.VISIBLE);
        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
        views.get(position).setVisibility(View.GONE);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return views.get(position).getContentDescription();
    }
}