package com.example.elevator_music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.example.elevator_music.Viewpager.ViewpagerAdapter;

import org.jetbrains.annotations.NotNull;

import me.relex.circleindicator.CircleIndicator3;

public class ViewpagerActivity extends AppCompatActivity {

    ViewPager2 pager;
    FragmentStateAdapter pagerAdapter;
    private int num_page = 3;
    CircleIndicator3 indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        pager = findViewById(R.id.viewpager);
        pagerAdapter = new ViewpagerAdapter(this, num_page);
        pager.setAdapter(pagerAdapter);

        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.createIndicators(num_page,0);

        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pager.setCurrentItem(3);
        pager.setOffscreenPageLimit(3);

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    pager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator.animatePageSelected(position%num_page);
            }
        });

        final float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        pager.setPageTransformer((page, position) -> {
            float myOffset = position * -(2 * pageOffset + pageMargin);

            if(pager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL){
                if(ViewCompat.getLayoutDirection(pager) == ViewCompat.LAYOUT_DIRECTION_RTL){
                    page.setTranslationX(-myOffset);
                }else{
                    page.setTranslationX(myOffset);
                }
            }else{
                page.setTranslationY(myOffset);
            }
        });
    }
}