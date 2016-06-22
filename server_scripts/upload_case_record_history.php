<?php

// if (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['caseRecordId'])) {
  require_once('db_connect.php');

$case_record_id = $_POST['caseRecordId'];
$case_record_status_id = $_POST['caseRecordStatusId'];
$updated_by = $_POST['updatedBy'];
$updated_on = $_POST['updatedOn'];

// $case_record_id = 45029050;
// $case_record_status_id = 1;
// $updated_by = 444;
// $updated_on = "20160512 124300";


if($stmt = $mysqli->prepare("INSERT INTO tbl_case_record_history
  (case_record_id, record_status_id, updated_by, updated_on) VALUES (?,?,?,?)")) {

    $stmt->bind_param('iiis', $case_record_id, $case_record_status_id, $updated_by, $updated_on);

    $stmt->execute();

    $stmt->close();

    echo "Successfully Uploaded Case Record History";

} else {
  echo "Error Uploading Case Record History";
}


$mysqli->close();

// }







 ?>
