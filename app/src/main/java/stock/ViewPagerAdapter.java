package stock;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> lsFragment = new ArrayList<>();
    private final List<String> lsTitles = new ArrayList<>();


    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        return lsFragment.get(position);
    }

    @Override
    public int getCount(){
        return lsTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return lsTitles.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        lsFragment.add(fragment);
        lsTitles.add(title);
    }
}
