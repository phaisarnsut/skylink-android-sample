package sg.com.temasys.skylink.sdk.sampleapp.ConfigFragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import sg.com.temasys.skylink.sdk.sampleapp.R;


public class ConfigFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        // Use tabs to switch between config pages.
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Rooms"));
        tabLayout.addTab(tabLayout.newTab().setText("App Keys"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager configViewPager = (ViewPager) view.findViewById(R.id.pager);

        final ConfigPagerAdapter configPagerAdapter =
                new ConfigPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        configViewPager.setAdapter(configPagerAdapter);

        configViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        configViewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }

                });

        return view;
    }

}

