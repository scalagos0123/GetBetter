<?php

if(($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['caseRecordId'])) {
  require_once('db_connect.php');

  $caseRecordId = $_POST['caseRecordId'];

  if($stmt = $mysqli->prepare("SELECT * FROM tbl_case_record_attachments
    WHERE case_record_id = ?")) {

      $stmt->bind_param('i', $caseRecordId);

      $stmt->execute();

      $stmt->bind_result($case_record_attachment_id, $case_record_id, $description,
      $case_file_url, $case_record_attachment_type_id, $uploaded_on);

      $result = array();

      while($stmt->fetch()) {

        if($case_record_attachment_type_id == 1) {

          $filetype = pathinfo($case_file_url, PATHINFO_EXTENSION);
          $image = base64_encode_image($case_file_url, $filetype);

          array_push($result, array('case_attachment_id'=>$case_record_attachment_id,
          'case_record_id'=>$case_record_id,
          'description'=>$description,
          'encoded_image'=>$image,
          'case_attachment_type'=>$case_record_attachment_type_id,
          'uploaded_on'=>$uploaded_on));

        } else if($case_record_attachment_type_id == 3) {
           

        }

      }

      $stmt->close();

      echo json_encode(array('case_attachments'=>$result));

    } else {
      echo 'SQL Query Fail';
    }


} else {
  echo 'Failed to run script';
}

public function base64_encode_image($filename='',$filetype='')
{
  if($filename) {
    $imgbinary = fread(fopen($filename, 'r'), filesize($filename));
    return 'data:image/' . $filetype . ';base64,' . base64_encode($imgbinary);
  }

}







 ?>
