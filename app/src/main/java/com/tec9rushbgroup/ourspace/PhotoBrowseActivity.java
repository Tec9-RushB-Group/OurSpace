package com.tec9rushbgroup.ourspace;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//this class is from:   https://github.com/sbLaughing/DragCloseDemo1

public class PhotoBrowseActivity extends AppCompatActivity {


    private int firstDisplayImageIndex = 0;
    private boolean newPageSelected = false;
    private PhotoView mCurImage;
    private BaseAnimCloseViewPager imageViewPager;
    private List<String> pictureList;

    PagerAdapter adapter;

    boolean canDrag =false;

    public static void startWithElement(Activity context, ArrayList<String> urls,
                                        int firstIndex, View view) {
        Intent intent = new Intent(context, PhotoBrowseActivity.class);
        intent.putStringArrayListExtra("urls", urls);
        intent.putExtra("index", firstIndex);
        ActivityOptionsCompat compat = null;
        if (view == null) {
            compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context);
        } else {
            compat = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view,
                    "tansition_view");
        }
        ActivityCompat.startActivity(context, intent, compat.toBundle());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_browse);
        initView();
    }

    public void initView() {
        pictureList = getIntent().getStringArrayListExtra("urls");
        firstDisplayImageIndex = Math.min(getIntent().getIntExtra("index", firstDisplayImageIndex), pictureList.size());

        imageViewPager = (BaseAnimCloseViewPager) findViewById(R.id.viewpager);

        setViewPagerAdapter();

        setEnterSharedElementCallback(new SharedElementCallback() {

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                ViewGroup layout = (ViewGroup) imageViewPager.findViewWithTag(imageViewPager.getCurrentItem());
                if (layout == null) {
                    return;
                }
                View sharedView = layout.findViewById(R.id.image_view);
                sharedElements.clear();
                sharedElements.put("tansition_view", sharedView);
            }
        });
    }

    private void setViewPagerAdapter() {
        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return pictureList == null ? 0 : pictureList.size();
            }

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View layout = (View) object;
                container.removeView(layout);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return (view == object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View layout;
                layout = LayoutInflater.from(PhotoBrowseActivity.this).inflate(R.layout.layout_browse, null);
                //layout.setOnClickListener(onClickListener);
                container.addView(layout);
                layout.setTag(position);

                if (position == firstDisplayImageIndex) {
                    onViewPagerSelected(position);
                }

                return layout;

            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };

        imageViewPager.setAdapter(adapter);
        imageViewPager.setOffscreenPageLimit(1);
        imageViewPager.setCurrentItem(firstDisplayImageIndex);
        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0f && newPageSelected) {
                    newPageSelected = false;
                    onViewPagerSelected(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                newPageSelected = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imageViewPager.setiAnimClose(new BaseAnimCloseViewPager.IAnimClose() {
            @Override
            public boolean canDrag() {
                return canDrag;
            }

            @Override
            public void onPictureClick() {
                finishAfterTransition();
            }

            @Override
            public void onPictureRelease(View view) {
                finishAfterTransition();
            }
        });
        //imageViewPager.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishAfterTransition();
        }
    };


    private void onViewPagerSelected(int position) {
        updateCurrentImageView(position);
        setImageView(pictureList.get(position));
    }


    private void setImageView(final String path) {
        if (mCurImage.getDrawable() != null)
            return;
        if (TextUtils.isEmpty(path)) {
            mCurImage.setBackgroundColor(Color.GRAY);
            return;
        }
        canDrag = false;
        Glide.with(this).load(path).into(mCurImage);
        mCurImage.setOnClickListener(onClickListener);
    }


    @Override
    public void onBackPressed() {
        return;
    }
    protected void updateCurrentImageView(final int position) {
        View currentLayout = imageViewPager.findViewWithTag(position);
        if (currentLayout == null) {
            ViewCompat.postOnAnimation(imageViewPager, new Runnable() {

                @Override
                public void run() {
                    updateCurrentImageView(position);
                }
            });
            return;
        }
        mCurImage = (PhotoView) currentLayout.findViewById(R.id.image_view);
        imageViewPager.setCurrentShowView(mCurImage);
    }


    @Override
    public void finishAfterTransition() {
        Intent intent = new Intent();
        intent.putExtra("index", imageViewPager.getCurrentItem());
        setResult(RESULT_OK, intent);
        super.finishAfterTransition();
    }
}
