package com.example.au575154coronatracker2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.au575154coronatracker2.Models.Country;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    public static ItemClickListener ItemClickListener;
    private List<Country> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private final String url = "https://www.countryflags.io/";

    //callback interface for user actions on each item
    private ICountryItemClickedListener listener;

    //interface for handling when and Person item is clicked in various ways
    public interface ICountryItemClickedListener{
        void onCountryClicked(int index);
    }

    // data is passed into the constructor
    public FileAdapter(ICountryItemClickedListener listener) {
        this.listener = listener;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, listener);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtviewcntryname.setText(mData.get(position).name);
        holder.txtviewrating.setText("Rating: " + mData.get(position).rating);
        holder.txtviewcases.setText("Cases:" + mData.get(position).cases);
        holder.txtviewdeaths.setText("Deaths: " + mData.get(position).deaths);
        String shortName = mData.get(position).countryCode;
        String imgurl = url + shortName + "/flat/64.png";
        Glide.with(holder.itemView.getContext())
                .load(imgurl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageviewcountry);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    //a method for updating the list - causes the adapter/recyclerview to update
    public void updateList(List<Country> lists){
        mData = lists;
        notifyDataSetChanged();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtviewcases, txtviewrating, txtviewcntryname, txtviewdeaths;
        ImageView imageviewcountry;

        //custom callback interface for user actions done the view holder item
        ICountryItemClickedListener listener;

        ViewHolder(View itemView, ICountryItemClickedListener countryItemClickedListener) {
            super(itemView);
            txtviewdeaths = itemView.findViewById(R.id.txtviewdeaths);
            txtviewcases = itemView.findViewById(R.id.txtviewcases);
            txtviewrating = itemView.findViewById(R.id.txtviewuserrating);
            imageviewcountry = itemView.findViewById(R.id.imgeViewCountryDetails);
            txtviewcntryname = itemView.findViewById(R.id.textviewcountry);
            listener = countryItemClickedListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onCountryClicked(getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Object getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}