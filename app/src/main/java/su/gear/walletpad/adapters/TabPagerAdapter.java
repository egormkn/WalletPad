package su.gear.walletpad.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import su.gear.walletpad.R;

public class TabPagerAdapter extends PagerAdapter {

    private Context context;

    public TabPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        TabPagerEnum customPagerEnum = TabPagerEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return TabPagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        TabPagerEnum customPagerEnum = TabPagerEnum.values()[position];
        return context.getResources().getString(customPagerEnum.getTitleResId());
    }

    private enum TabPagerEnum {

        TAB1(R.string.tab_summary, R.layout.tab_summary),
        TAB2(R.string.tab_wallets, R.layout.tab_wallets),
        TAB3(R.string.tab_plans, R.layout.tab_plans);

        private int titleResId;
        private int layoutResId;

        TabPagerEnum(int titleResId, int layoutResId) {
            this.titleResId = titleResId;
            this.layoutResId = layoutResId;
        }

        public int getTitleResId() {
            return titleResId;
        }

        public int getLayoutResId() {
            return layoutResId;
        }
    }
}