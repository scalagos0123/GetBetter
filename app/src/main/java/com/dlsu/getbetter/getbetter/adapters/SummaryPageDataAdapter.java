package com.dlsu.getbetter.getbetter.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dlsu.getbetter.getbetter.R;
import com.dlsu.getbetter.getbetter.objects.Attachment;

import java.util.ArrayList;

/**
 * Created by mikedayupay on 13/02/2016.
 */
public class SummaryPageDataAdapter extends RecyclerView.Adapter<SummaryPageDataAdapter.ViewHolder> {

    private ArrayList<Attachment> filesDataset;
    private OnItemClickListener mItemClickListener;
    private int selectedItem = 0;

    public SummaryPageDataAdapter (ArrayList<Attachment> dataset) {

        filesDataset = dataset;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView fileTitle;


        public ViewHolder(View v) {
            super(v);
            itemView.setOnClickListener(this);
            fileTitle = (TextView)v.findViewById(R.id.summary_page_file_list_item);

        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
                notifyItemChanged(selectedItem);
                selectedItem = getAdapterPosition();
                notifyItemChanged(selectedItem);
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener =  mItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_page_item,
                parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.fileTitle.setText(filesDataset.get(position).getAttachmentDescription());

    }

    @Override
    public int getItemCount() {
        return filesDataset.size();
    }


}
