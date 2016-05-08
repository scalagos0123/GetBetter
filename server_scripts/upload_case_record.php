<?php

  if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    require_once('db_connect.php');

    $case_record_id = $_POST['caseRecordId'];
    $user_id = $_POST['userId'];
    $health_center_id = $_POST['healthCenterId'];
    $complaint = $_POST['complaint'];
    $control_number = $_POST['controlNumber'];

    $sql_query = "INSERT INTO tbl_case_records (case_record_id, user_id, health_center_id, complaint, control_number)
    VALUES ('$case_record_id', '$user_id', '$health_center_id', '$complaint', '$control_number')";

    if(mysqli_query($con, $sql_query)) {
      echo "Successfully Uploaded";
    }

    mysqli_close($con);

  } else {

    echo "Error Uploading Case Records";

  }



?>
