package com.dlsu.getbetter.getbetter.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlsu.getbetter.getbetter.R;
import com.dlsu.getbetter.getbetter.objects.Patient;

import java.util.ArrayList;

/**
 * Created by mikedayupay on 16/02/2016.
 */
public class ExistingPatientAdapter extends RecyclerView.Adapter<ExistingPatientAdapter.ExistingPatientViewHolder> {

    private ArrayList<Patient> existingPatients;
    private int selectedItem = 0;


    public class ExistingPatientViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView patientName;
        TextView patientBirthdate;
        TextView patientGender;
        ImageView patientImage;

        public ExistingPatientViewHolder (View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.existing_patient_item_card);
            patientName = (TextView)itemView.findViewById(R.id.existing_patient_item_name);
            patientBirthdate = (TextView)itemView.findViewById(R.id.existing_patient_item_birthdate);
            patientGender = (TextView)itemView.findViewById(R.id.existing_patient_item_gender);
            patientImage = (ImageView)itemView.findViewById(R.id.existing_patient_item_profile_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selectedItem);
                    selectedItem = getLayoutPosition();
                    notifyItemChanged(selectedItem);
                }
            });

        }
    }

    public ExistingPatientAdapter(ArrayList<Patient> existingPatients) {
        this.existingPatients = existingPatients;
    }

    @Override
    public ExistingPatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.existing_patient_list_item, parent, false);
        ExistingPatientViewHolder existingPatientViewHolder = new ExistingPatientViewHolder(v);

        return existingPatientViewHolder;
    }

    @Override
    public void onBindViewHolder(ExistingPatientViewHolder holder, int position) {

        String patientName = existingPatients.get(position).getLastName() + ", " +
                existingPatients.get(position).getFirstName();
        holder.patientName.setText(patientName);
        holder.patientBirthdate.setText(existingPatients.get(position).getBirthdate());
        holder.patientGender.setText(existingPatients.get(position).getGender());
        holder.patientImage.setImageBitmap(decodeEncodedImage(existingPatients.get(position).getProfileImageBytes()));

        holder.itemView.setSelected(selectedItem == position);

    }

    @Override
    public int getItemCount() {
        return existingPatients.size();
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        recyclerView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey (View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return tryMoveSelection(lm, 1);
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        return tryMoveSelection(lm, -1);
                    }
                }
                return false;
            }

        });
    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
        int nextSelectItem = selectedItem + direction;

        // If still within valid bounds, move the selection, notify to redraw, and scroll
        if (nextSelectItem >= 0 && nextSelectItem < getItemCount()) {
            notifyItemChanged(selectedItem);
            selectedItem = nextSelectItem;
            notifyItemChanged(selectedItem);
            lm.scrollToPosition(selectedItem);
            return true;
        }

        return false;
    }

    public Bitmap decodeEncodedImage(String encodedImage) {

        byte[] imageAsBytes = Base64.decode(encodedImage.getBytes(), Base64.DEFAULT);

        Bitmap image = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        return image;
    }
}
