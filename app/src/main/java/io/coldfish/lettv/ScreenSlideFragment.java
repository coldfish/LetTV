package io.coldfish.lettv;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.coldfish.lettv.model.TVShow;

import static io.coldfish.lettv.rest.RestClient.IMAGE_BASE_URL;


public class ScreenSlideFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private TVShow tvShow;

    public ScreenSlideFragment() {
        // Required empty public constructor
    }

    public static ScreenSlideFragment newInstance(int position, TVShow tvShow) {
        ScreenSlideFragment fragment = new ScreenSlideFragment();
        Bundle args = new Bundle();
        args.putParcelable("tv_show", tvShow);
        args.putInt(ARG_SECTION_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvShow = getArguments().getParcelable("tv_show");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tvshow_detail, container, false);
        
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setTitle(tvShow.getOriginal_name());

        // Hero Image
        ImageView imageView = rootView.findViewById(R.id.header_image);
        Picasso.with(getActivity()).load(IMAGE_BASE_URL + tvShow.getBackdrop_path()).centerCrop().fit().into(imageView);

        // Air Date
        TextView airDateText = rootView.findViewById(R.id.air_date);
        airDateText.setText(tvShow.getFirst_air_date());

        // Vote Average
        TextView voteAverageText = rootView.findViewById(R.id.vote_average);
        voteAverageText.setText(String.valueOf(tvShow.getVote_average()));

        // Vote Count
        TextView voteCountText = rootView.findViewById(R.id.vote_count);
        voteCountText.setText(String.valueOf(tvShow.getVote_count()));

        // Overview Details
        TextView overviewText = rootView.findViewById(R.id.overview);
        overviewText.setText(tvShow.getOverview());

        return rootView;
    }


}
