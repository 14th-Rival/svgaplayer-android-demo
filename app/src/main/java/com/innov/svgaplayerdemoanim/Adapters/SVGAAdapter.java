package com.innov.svgaplayerdemoanim.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.innov.svgaplayerdemoanim.Interfaces.OnClickListener;
import com.innov.svgaplayerdemoanim.MainActivity;
import com.innov.svgaplayerdemoanim.Models.AssetsModel;
import com.innov.svgaplayerdemoanim.R;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.util.ArrayList;
import java.util.List;

public class SVGAAdapter extends RecyclerView.Adapter<SVGAAdapter.SVGAViewHolder> {

    private final List<AssetsModel> assetsModelList;
    private Context mContext;
    private MainActivity mActivity;
    private OnClickListener onClickListener;
    private int selectedItem;

    public SVGAAdapter(List<AssetsModel> assetsModelList, Context mContext, MainActivity mActivity) {
        this.assetsModelList = assetsModelList;
        this.mContext = mContext;
        this.mActivity = mActivity;
        selectedItem = 0;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return assetsModelList.size();
    }

    @NonNull
    @Override
    public SVGAAdapter.SVGAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(
                        R.layout.cardview_assets_holder, parent, false);
        return new SVGAViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SVGAAdapter.SVGAViewHolder holder, int position) {
        AssetsModel currentItem = assetsModelList.get(position);

        try {
            holder.svgaParser = new SVGAParser(mContext);
            holder.svgaParser.decodeFromAssets(currentItem.getmAssets(), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable svgaDrawable = new SVGADrawable(svgaVideoEntity);
                    holder.svgaImageView.setImageDrawable(svgaDrawable);
                    holder.svgaImageView.startAnimation();
                }

                @Override
                public void onError() {

                }
            }, null);

            if (selectedItem == holder.getAdapterPosition()) {
                holder.svgaImageView.setBackground(
                        ContextCompat.getDrawable(
                                holder.svgaImageView.getContext(),
                                R.drawable.asset_selected)
                );
                mActivity.setViewAsset(currentItem.getmAssets());
            }

            else {
                holder.svgaImageView.setBackground(
                        ContextCompat.getDrawable(
                                holder.svgaImageView.getContext(),
                                R.drawable.asset_not_selected
                        )
                );
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> {
            int previousItem = selectedItem;
            selectedItem = holder.getAdapterPosition();

            notifyItemChanged(previousItem);
            notifyItemChanged(holder.getAdapterPosition());
        });

    }

    public static class SVGAViewHolder extends RecyclerView.ViewHolder{
        private SVGAImageView svgaImageView;
        private SVGAParser svgaParser;

        public SVGAViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            svgaImageView = itemView.findViewById(R.id.svga_asset_holder);

            itemView.setOnClickListener(v -> {
                if (onClickListener != null){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        onClickListener.onClick(v, pos);
                    }
                }
            });
        }
    }
}
