package io.coldfish.lettv;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.florent37.tutoshowcase.TutoShowcase;

import java.util.List;

import io.coldfish.lettv.model.TVShow;
import io.coldfish.lettv.rest.RestClient;
import io.coldfish.lettv.rest.TVShowsRepository;
import io.coldfish.lettv.viewmodel.VMTVShows;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ScreenSlidePagerAdapter screenSlidePagerAdapter;
    private ViewPager mViewPager;
    private List<TVShow> similarTVShows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshow_detail);

        TutoShowcase.from(this)
                .setContentView(R.layout.tutor_swipe)
                .setFitsSystemWindows(true)
                .on(R.id.view_pager)
                .displaySwipableRight()
                .delayed(1000)
                .animated(true)
                .showOnce("tutor_swipe");

        TVShow tv_show = getIntent().getExtras().getParcelable("tv_show");

        VMTVShows.Factory factory = new VMTVShows.Factory(new TVShowsRepository(RestClient.get()));
        final VMTVShows vmTVShows = ViewModelProviders.of(this, factory).get(VMTVShows.class);
        vmTVShows.callServiceForSimilarTVShows(tv_show);

        vmTVShows.getSimilarShowsData().observe(this, new Observer<List<TVShow>>() {
            @Override
            public void onChanged(@Nullable List<TVShow> tvShows) {
                similarTVShows = tvShows;
                if (!isDestroyed()) {
                    screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
                    mViewPager = findViewById(R.id.view_pager);
                    mViewPager.setAdapter(screenSlidePagerAdapter);
                    mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                }
            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlideFragment.newInstance(position, similarTVShows.get(position));
        }

        @Override
        public int getCount() {
            return 20;
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) {
                view.setAlpha(0);

            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else {
                view.setAlpha(0);
            }
        }
    }

    // Different animation for pager transition
    // mViewPager.setPageTransformer(true, new DepthPageTransformer());
    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) {
                view.setAlpha(0);

            } else if (position <= 0) {
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) {
                view.setAlpha(1 - position);

                view.setTranslationX(pageWidth * -position);

                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else {
                view.setAlpha(0);
            }
        }
    }
}
