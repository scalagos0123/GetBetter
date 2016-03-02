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

    public SummaryPageDataAdapter (ArrayList<Attachment> dataset) {

        filesDataset = dataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fileTitle;


        public ViewHolder(View v) {
            super(v);
            fileTitle = (TextView)v.findViewById(R.id.summary_page_file_list_item);
        }
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

    public void addAttachmentList(Attachment attachment) {

        filesDataset.add(attachment);
    }

    @Override
    public int getItemCount() {
        return filesDataset.size();
    }


}
