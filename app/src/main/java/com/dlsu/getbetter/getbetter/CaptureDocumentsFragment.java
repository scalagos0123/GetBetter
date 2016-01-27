package com.dlsu.getbetter.getbetter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaptureDocumentsFragment extends Fragment implements View.OnClickListener {

    private LinearLayout captureBasicInfoBtn;
    private LinearLayout captureFamilyHistBtn;
    private static final int REQUEST_IMAGE1 = 100;
    private static final int REQUEST_IMAGE2 = 100;
    private static final int REQUEST_IMAGE3 = 100;

    public CaptureDocumentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_capture_documents, container, false);

        captureBasicInfoBtn = (LinearLayout)rootView.findViewById(R.id.capture_patient_info);
        captureFamilyHistBtn = (LinearLayout)rootView.findViewById(R.id.capture_family_history);

        captureBasicInfoBtn.setOnClickListener(this);
        captureFamilyHistBtn.setOnClickListener(this);

        Button nextBtn = (Button)rootView.findViewById(R.id.capture_docu_fragment_next_btn);
        Button backBtn = (Button)rootView.findViewById(R.id.capture_docu_fragment_back_btn);

        nextBtn.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_IMAGE1 && resultCode == Activity.RESULT_OK) {

        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch(id) {
            case R.id.capture_docu_fragment_next_btn:
                RecordHpiFragment recordHpiFragment = new RecordHpiFragment();
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).commit();
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, recordHpiFragment).commit();
                break;

            case R.id.capture_patient_info: try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE1);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
                break;

            case R.id.capture_family_history: try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE2);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
                break;




        }


    }
}
