package com.dlsu.getbetter.getbetter.objects;

import java.util.ArrayList;

/**
 * Created by mikedayupay on 25/02/2016.
 */
public class CaseRecord {

    private int caseRecordId;
    private String caseRecordComplaint;
    private String caseRecordControlNumber;
    private ArrayList<Attachment> caseRecordAttachments;

    public CaseRecord(int caseRecordId, String caseRecordComplaint, String caseRecordControlNumber) {
        this.caseRecordId = caseRecordId;
        this.caseRecordComplaint = caseRecordComplaint;
        this.caseRecordControlNumber = caseRecordControlNumber;
    }

    public CaseRecord(ArrayList<Attachment> caseRecordAttachments) {
        this.caseRecordAttachments = caseRecordAttachments;
    }

    public ArrayList<Attachment> getCaseRecordAttachments() {
        return caseRecordAttachments;
    }

    public int getCaseRecordId() {
        return caseRecordId;
    }

    public String getCaseRecordComplaint() {
        return caseRecordComplaint;
    }

    public String getCaseRecordControlNumber() {
        return caseRecordControlNumber;
    }

}
