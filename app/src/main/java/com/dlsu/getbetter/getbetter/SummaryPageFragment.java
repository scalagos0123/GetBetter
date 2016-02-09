package com.dlsu.getbetter.getbetter;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dlsu.getbetter.getbetter.sessionmanagers.NewPatientSessionManager;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryPageFragment extends Fragment {

    private NewPatientSessionManager newPatientDetails;
    private String image;

    private ImageView summaryProfileImage;
    private byte[] imageAsBytes;


    public SummaryPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newPatientDetails = new NewPatientSessionManager(this.getContext());
        HashMap<String, String> patient = newPatientDetails.getNewPatientDetails();
        image = patient.get(NewPatientSessionManager.NEW_PATIENT_PROFILE_IMAGE);

        imageAsBytes  = Base64.decode(image.getBytes(), Base64.DEFAULT);





    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_summary_page, container, false);

        summaryProfileImage = (ImageView)rootView.findViewById(R.id.profile_picture_display);

        summaryProfileImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        return rootView;
    }



}
