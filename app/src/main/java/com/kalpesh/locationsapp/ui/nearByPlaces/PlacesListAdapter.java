package com.kalpesh.locationsapp.ui.nearByPlaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kalpesh.locationsapp.R;
import com.kalpesh.locationsapp.model.Place;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.PlaceViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Place> places;
    private ClickListener clickListener;
    private DecimalFormat ratingFormat;

    public PlacesListAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        layoutInflater = LayoutInflater.from(context);
        places = new ArrayList<>();
        ratingFormat = new DecimalFormat("#.#");
        ratingFormat.setRoundingMode(RoundingMode.CEILING);
    }

    public void updatePlaces(List<Place> places) {
        this.places.clear();
        this.places.addAll(places);
        notifyDataSetChanged();
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlaceViewHolder(layoutInflater.inflate(R.layout.item_place, parent, false));
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Place place = places.get(position);
        holder.placeName.setText(place.name.replaceAll("\n", " "));
        if (place.rating == 0) {
            holder.rating.setVisibility(View.GONE);
        } else {
            holder.rating.setVisibility(View.VISIBLE);
            holder.rating.setText(String.format("Rating : %s", ratingFormat.format(place.rating)));
        }
        holder.area.setText(place.area);
        Glide.with(context)
                .load(place.icon)
                .placeholder(R.drawable.location_placeholder)
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public interface ClickListener {
        void onPlaceClicked(Place place);
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.place_name)
        TextView placeName;
        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.rating)
        TextView rating;
        @Bind(R.id.area)
        TextView area;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() < 0) return;

            if (clickListener != null)
                clickListener.onPlaceClicked(places.get(getAdapterPosition()));
        }
    }
}
