package com.dlsu.getbetter.getbetter.objects;

/**
 * Created by mikedayupay on 21/02/2016.
 */
public class Attachment {

    private int caseRecordId;
    private String attachmentPath;
    private String attachmentDescription;
    private String attachmentType;
    private String uploadedDate;

    public Attachment(String attachmentPath, String attachmentDescription, String attachmentType, String uploadedDate) {
        this.attachmentPath = attachmentPath;
        this.attachmentDescription = attachmentDescription;
        this.attachmentType = attachmentType;
        this.uploadedDate = uploadedDate;
    }

    public Attachment(String attachmentPath, String attachmentDescription) {
        this.attachmentPath = attachmentPath;
        this.attachmentDescription = attachmentDescription;
    }

    public Attachment(int caseRecordId, String attachmentPath, String attachmentDescription,
                      String attachmentType, String uploadedDate) {
        this.caseRecordId = caseRecordId;
        this.attachmentPath = attachmentPath;
        this.attachmentDescription = attachmentDescription;
        this.attachmentType = attachmentType;
        this.uploadedDate = uploadedDate;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getAttachmentDescription() {
        return attachmentDescription;
    }

    public void setAttachmentDescription(String attachmentDescription) {
        this.attachmentDescription = attachmentDescription;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public int getCaseRecordId() {
        return caseRecordId;
    }

    public void setCaseRecordId(int caseRecordId) {
        this.caseRecordId = caseRecordId;
    }

    public String getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(String uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
}
