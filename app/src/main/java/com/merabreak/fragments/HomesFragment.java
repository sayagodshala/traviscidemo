package com.merabreak.fragments;


import android.os.Bundle;
import android.widget.ListView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.merabreak.BaseFragment;
import com.merabreak.CategoryItem;
import com.merabreak.CategoryItem_;
import com.merabreak.Constants;
import com.merabreak.CustomListAdapter;
import com.merabreak.R;
import com.merabreak.Util;
import com.merabreak.activities.ChallengesActivity_;
import com.merabreak.models.APIResponse;
import com.merabreak.models.challenge.Category;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

@EFragment(R.layout.home)
public class HomesFragment extends BaseFragment implements ViewPagerEx.OnPageChangeListener {


    @ViewById(R.id.slider)
    SliderLayout slider;

    private CustomListAdapter categoriesAdapter = null;
    List<Category> categories;

    @ViewById(R.id.categoriesList)
    ListView categoriesList;

    @AfterViews
    void init() {
        initSlider();
        getCategories();
    }

    public void initSlider() {
 /*       String[] images = {
                "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg"
                , "http://tvfiles.alphacoders.com/100/hdclearart-10.png"
                , "http://cdn3.nflximg.net/images/3093/2043093.jpg"
                , "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg"
        };
        for (int i = 0; i < images.length; i++) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
            defaultSliderView.image(images[i]);
            defaultSliderView.description("vijay");
            defaultSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
            slider.addSlider(defaultSliderView);
        }
*/


        HashMap<String, String> file_maps = new HashMap<String, String>();
        file_maps.put("Saya", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        file_maps.put("Vijay", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        file_maps.put("Vinay", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        file_maps.put("Ewebcore", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);


            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            slider.addSlider(textSliderView);
        }


        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(2000);
        slider.addOnPageChangeListener(this);

    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        slider.stopAutoCycle();
        super.onStop();
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void getCategories() {
        hideLoader();
        showLoader();
        Call<APIResponse<List<Category>>> callback = apiService.getCategories(Constants.MB_API_KEY, user.getAuthToken());
        callback.enqueue(new Callback<APIResponse<List<Category>>>() {
            @Override
            public void onResponse(Response<APIResponse<List<Category>>> response) {
                hideLoader();
                if (response.body().getMeta() != null) {
                    if (response.body().getMeta().isStatus()) {
                        if (response.body().getValues() != null && response.body().getValues().size() > 0) {
                            categories = response.body().getValues();
                            drawCategories();
                        }
                    } else {
                        Util.makeToast(getActivity(), Constants.SOME_THING_WRONG);
                    }
                } else {
                    Util.makeToast(getActivity(), Constants.SOME_THING_WRONG);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoader();
//                Util.makeToast(getActivity(), t.getMessage());
            }
        });
    }

    private void drawCategories() {
        categoriesAdapter = new CustomListAdapter<>(categories, this::categoryItemView);
        categoriesList.setAdapter(categoriesAdapter);
    }

    private CategoryItem categoryItemView() {
        CategoryItem item = CategoryItem_.build(getActivity());
        return item;
    }

    @ItemClick(R.id.categoriesList)
    void onItemClick(Category item) {
        ChallengesActivity_.intent(getActivity()).category(item).start();
    }


}
