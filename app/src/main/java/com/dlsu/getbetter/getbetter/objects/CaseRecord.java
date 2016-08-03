package com.dlsu.getbetter.getbetter.objects;

/**
 * Created by mikedayupay on 25/02/2016.
 * GetBetter 2016
 */
public class CaseRecord {

    private int caseRecordId;
    private int caseRecordStatusId;
    private int userId;
    private String patientName;
    private String profilePic;
    private String healthCenter;
    private String caseRecordComplaint;
    private String caseRecordAdditionalNotes;
    private String caseRecordControlNumber;
    private String caseRecordStatus;
    private String caseRecordUpdatedOn;
    private int caseRecordUpdatedBy;
    private boolean checked = false;

    public CaseRecord() {

    }

    public CaseRecord(int caseRecordId, int caseRecordStatusId, int caseRecordUpdatedBy, String caseRecordUpdatedOn) {
        this.caseRecordId = caseRecordId;
        this.caseRecordStatusId = caseRecordStatusId;
        this.caseRecordUpdatedBy = caseRecordUpdatedBy;
        this.caseRecordUpdatedOn = caseRecordUpdatedOn;
    }

    public CaseRecord(int caseRecordId, String caseRecordComplaint, String caseRecordControlNumber) {
        this.caseRecordId = caseRecordId;
        this.caseRecordComplaint = caseRecordComplaint;
        this.caseRecordControlNumber = caseRecordControlNumber;
    }

    public CaseRecord(int caseRecordId, String patientName, String caseRecordComplaint, String caseRecordAdditionalNotes,
                      String healthCenter, String caseRecordStatus, String caseRecordUpdatedOn) {
        this.caseRecordId = caseRecordId;
        this.patientName = patientName;
        this.caseRecordComplaint = caseRecordComplaint;
        this.caseRecordAdditionalNotes = caseRecordAdditionalNotes;
        this.healthCenter = healthCenter;
        this.caseRecordStatus = caseRecordStatus;
        this.caseRecordUpdatedOn = caseRecordUpdatedOn;
    }

    public CaseRecord(String caseRecordComplaint, String caseRecordControlNumber) {
        this.caseRecordComplaint = caseRecordComplaint;
        this.caseRecordControlNumber = caseRecordControlNumber;
    }

    public CaseRecord(int caseRecordId, int userId, String caseRecordComplaint, String caseRecordUpdatedOn) {
        this.caseRecordId = caseRecordId;
        this.userId = userId;
        this.caseRecordComplaint = caseRecordComplaint;
        this.caseRecordUpdatedOn = caseRecordUpdatedOn;
    }

    public CaseRecord(int caseRecordId, String complaint, int userId, String caseRecordControlNumber, String caseRecordAdditionalNotes) {
        this.caseRecordId = caseRecordId;
        this.caseRecordComplaint = complaint;
        this.userId = userId;
        this.caseRecordControlNumber = caseRecordControlNumber;
        this.caseRecordAdditionalNotes = caseRecordAdditionalNotes;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCaseRecordStatus(String caseRecordStatus) {
        this.caseRecordStatus = caseRecordStatus;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getHealthCenter() {
        return healthCenter;
    }

    public void setHealthCenter(String healthCenter) {
        this.healthCenter = healthCenter;
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

    public String getCaseRecordAdditionalNotes() {
        return caseRecordAdditionalNotes;
    }

    public void setCaseRecordAdditionalNotes(String caseRecordAdditionalNotes) {
        this.caseRecordAdditionalNotes = caseRecordAdditionalNotes;
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
