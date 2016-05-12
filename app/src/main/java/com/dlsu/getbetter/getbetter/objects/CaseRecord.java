package com.dlsu.getbetter.getbetter.objects;

import java.util.ArrayList;

/**
 * Created by mikedayupay on 25/02/2016.
 */
public class CaseRecord {

    private int caseRecordId;
    private int caseRecordStatusId;
    private String caseRecordComplaint;
    private String caseRecordControlNumber;
    private String caseRecordStatus;
    private String caseRecordUpdatedOn;
    private int caseRecordUpdatedBy;
    private boolean checked = false;

    private ArrayList<Attachment> caseRecordAttachments;

    public CaseRecord(int caseRecordId, String caseRecordComplaint, String caseRecordControlNumber, String caseRecordStatus) {
        this.caseRecordId = caseRecordId;
        this.caseRecordComplaint = caseRecordComplaint;
        this.caseRecordControlNumber = caseRecordControlNumber;
        this.caseRecordStatus = caseRecordStatus;
    }

    public CaseRecord(int caseRecordId, String caseRecordComplaint, String caseRecordControlNumber) {
        this.caseRecordId = caseRecordId;
        this.caseRecordComplaint = caseRecordComplaint;
        this.caseRecordControlNumber = caseRecordControlNumber;
    }

    public void setCaseRecordAttachments(ArrayList<Attachment> caseRecordAttachments) {
        this.caseRecordAttachments = caseRecordAttachments;
    }

    public void setCaseRecordStatus(String caseRecordStatus) {
        this.caseRecordStatus = caseRecordStatus;
    }

    public void setCaseRecordId(int caseRecordId) {
        this.caseRecordId = caseRecordId;
    }

    public int getCaseRecordStatusId() {
        return caseRecordStatusId;
    }

    public void setCaseRecordStatusId(int caseRecordStatusId) {
        this.caseRecordStatusId = caseRecordStatusId;
    }

    public void setCaseRecordComplaint(String caseRecordComplaint) {
        this.caseRecordComplaint = caseRecordComplaint;
    }

    public void setCaseRecordControlNumber(String caseRecordControlNumber) {
        this.caseRecordControlNumber = caseRecordControlNumber;
    }

    public String getCaseRecordUpdatedOn() {
        return caseRecordUpdatedOn;
    }

    public void setCaseRecordUpdatedOn(String caseRecordUpdatedOn) {
        this.caseRecordUpdatedOn = caseRecordUpdatedOn;
    }

    public int getCaseRecordUpdatedBy() {
        return caseRecordUpdatedBy;
    }

    public void setCaseRecordUpdatedBy(int caseRecordUpdatedBy) {
        this.caseRecordUpdatedBy = caseRecordUpdatedBy;
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

    public String getCaseRecordStatus() {
        return caseRecordStatus;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toggleChecked() {
        checked = !checked;
    }
}
