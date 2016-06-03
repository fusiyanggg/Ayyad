package com.ddf.fusiyang.ayyad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ddf.fusiyang.ayyad.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private List<Integer> mDates;

    public interface OnItemClickLitentner {


        void onItemClick(View view, int position);

    }

    private OnItemClickLitentner onItemClickLitentner;

    public void setOnItemClickLitentner(OnItemClickLitentner onItemClickLitentner){

        this.onItemClickLitentner = onItemClickLitentner;

    }


    public GalleryAdapter(Context context, List<Integer> daters) {

        mInflater = LayoutInflater.from(context);
        mDates = daters;

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {

            super(view);

        }

        ImageView mImg;
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.pic_item, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mImg = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.mImg.setImageResource(mDates.get(i));

        if (onItemClickLitentner != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLitentner.onItemClick(viewHolder.itemView,i);
                }
            });
        }

    }
}