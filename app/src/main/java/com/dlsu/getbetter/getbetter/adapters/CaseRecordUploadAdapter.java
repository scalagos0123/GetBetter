package com.dlsu.getbetter.getbetter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dlsu.getbetter.getbetter.R;
import com.dlsu.getbetter.getbetter.objects.CaseRecord;

import java.util.ArrayList;

/**
 * Created by mikedayupay on 15/04/2016.
 * GetBetter 2016
 */
public class CaseRecordUploadAdapter extends ArrayAdapter<CaseRecord> {

    private ArrayList<CaseRecord> caseRecordsList;
    private LayoutInflater inflater;

    public CaseRecordUploadAdapter(Context context, int textViewResourceId, ArrayList<CaseRecord> objects) {
        super(context, textViewResourceId, objects);

        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {

        TextView controlNumber;
        TextView patientName;
        TextView complaint;
        TextView caseStatus;
        TextView dateUpdated;
        CheckBox checkBox;

        public ViewHolder() {

        }

        public ViewHolder(TextView controlNumber, TextView patientName,
                          TextView complaint, TextView caseStatus, TextView dateUpdated, CheckBox checkBox) {

            this.controlNumber = controlNumber;
            this.patientName = patientName;
            this.complaint = complaint;
            this.caseStatus = caseStatus;
            this.dateUpdated = dateUpdated;
            this.checkBox = checkBox;

        }

        public TextView getControlNumber() {
            return controlNumber;
        }

        public void setControlNumber(TextView controlNumber) {
            this.controlNumber = controlNumber;
        }

        public TextView getPatientName() {
            return patientName;
        }

        public void setPatientName(TextView patientName) {
            this.patientName = patientName;
        }

        public TextView getComplaint() {
            return complaint;
        }

        public void setComplaint(TextView complaint) {
            this.complaint = complaint;
        }

        public TextView getCaseStatus() {
            return caseStatus;
        }

        public void setCaseStatus(TextView caseStatus) {
            this.caseStatus = caseStatus;
        }

        public TextView getDateUpdated() {
            return dateUpdated;
        }

        public void setDateUpdated(TextView dateUpdated) {
            this.dateUpdated = dateUpdated;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        CaseRecord caseRecord = this.getItem(position);

        if(convertView == null) {

            convertView = inflater.inflate(R.layout.case_record_item_checkbox, parent, false);

            holder = new ViewHolder();
            holder.patientName = (TextView)convertView.findViewById(R.id.upload_case_record_patient_name);
            holder.controlNumber = (TextView)convertView.findViewById(R.id.upload_control_number);
            holder.complaint = (TextView)convertView.findViewById(R.id.upload_caserecord_chief_complaint);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.upload_caserecord_checkbox);
            convertView.setTag(holder);

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    CaseRecord caseRecord = (CaseRecord) cb.getTag();
                    caseRecord.setChecked(cb.isChecked());
                }
            });

        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        String id = caseRecord.getCaseRecordControlNumber() + "";
        //String name = patient.getLastName() + ", " + patient.getFirstName();
        String name = "Patient Name";
        holder.controlNumber.setText(id);
        holder.patientName.setText(name);
        holder.complaint.setText(caseRecord.getCaseRecordComplaint());
        holder.checkBox.setChecked(caseRecord.isChecked());
        holder.checkBox.setTag(caseRecord);

        return convertView;
    }
}
