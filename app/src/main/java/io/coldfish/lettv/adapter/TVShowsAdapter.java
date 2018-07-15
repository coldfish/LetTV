package io.coldfish.lettv.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.coldfish.lettv.R;
import io.coldfish.lettv.model.TVShow;
import io.coldfish.lettv.utils.Utils;

import static io.coldfish.lettv.rest.RestClient.IMAGE_BASE_URL;

public class TVShowsAdapter extends RecyclerView.Adapter<TVShowsAdapter.TVShowHolder> {

    private List<TVShow> tvShowsList;
    private ItemClickListener clickListener;

    public TVShowsAdapter(List<TVShow> tvShowsList) {
        this.tvShowsList = tvShowsList;
    }

    @NonNull
    @Override
    public TVShowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_show_item, parent, false);
        TVShowHolder viewHolder = new TVShowHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowHolder holder, int position) {
        TVShow tvShow = tvShowsList.get(position);
        Picasso.with(holder.imageView.getContext()).load(IMAGE_BASE_URL + tvShow.getBackdrop_path()).centerCrop().fit().into(holder.imageView);
        holder.title.setText(new StringBuilder().append(tvShow.getName()).append(" (").append(Utils.getYearFromAirDate(tvShow.getFirst_air_date())).append(")").toString());
        holder.averageVote.setText(String.valueOf(tvShow.getVote_average()));
    }

    @Override
    public int getItemCount() {
        return tvShowsList == null ? 0 : tvShowsList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void setItems(List<TVShow> items) {
        tvShowsList.clear();
        tvShowsList.addAll(items);
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public class TVShowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView title;
        private TextView averageVote;

        public TVShowHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tv_show_poster_image);
            title = itemView.findViewById(R.id.tv_show_title);
            averageVote = itemView.findViewById(R.id.tv_show_average_vote);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }
}
