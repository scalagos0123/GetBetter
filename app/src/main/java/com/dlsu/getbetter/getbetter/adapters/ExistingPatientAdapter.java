package com.dlsu.getbetter.getbetter.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
    private OnItemClickListener mItemClickListener;
    private int selectedItem = 0;


    public class ExistingPatientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        TextView patientName;
        TextView patientBirthdate;
        TextView patientGender;
        ImageView patientImage;

        public ExistingPatientViewHolder (View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.existing_patient_item_card);
            patientName = (TextView)itemView.findViewById(R.id.upload_patient_item_name);
            patientBirthdate = (TextView)itemView.findViewById(R.id.existing_patient_item_birthdate);
            patientGender = (TextView)itemView.findViewById(R.id.existing_patient_item_gender);
            patientImage = (ImageView)itemView.findViewById(R.id.upload_patient_item_profile_image);
            itemView.setOnClickListener(this);
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
    public void onBindViewHolder(final ExistingPatientViewHolder holder, final int position) {

        String patientName = existingPatients.get(position).getLastName() + ", " +
                existingPatients.get(position).getFirstName();
        holder.patientName.setText(patientName);
        holder.patientBirthdate.setText(existingPatients.get(position).getBirthdate());
        holder.patientGender.setText(existingPatients.get(position).getGender());
        //holder.patientImage.setImageBitmap(decodeEncodedImage(existingPatients.get(position).getProfileImageBytes()));

        holder.itemView.post(new Runnable() {
            @Override
            public void run() {
                setPic(holder.patientImage, existingPatients.get(position).getProfileImageBytes());
            }
        });

        holder.itemView.setSelected(selectedItem == position);

//        Bitmap imageBm = BitmapFactory.decodeFile(existingPatients.get(position).getProfileImageBytes());
//        holder.patientImage.setImageBitmap(imageBm);

    }

    @Override
    public int getItemCount() {
        return existingPatients.size();
    }


    private void setPic(ImageView mImageView, String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }
}
