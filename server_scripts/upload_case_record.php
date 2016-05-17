<?php

  if (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['caseRecordId'])) {
    require_once('db_connect.php');

    $case_record_id = $_POST['caseRecordId'];
    $user_id = $_POST['userId'];
    $health_center_id = $_POST['healthCenterId'];
    $complaint = $_POST['complaint'];
    $control_number = $_POST['controlNumber'];
    $case_id = 6;

    // $case_record_status_id = $_POST['caseRecordStatusId'];
    // $updated_by = $_POST['updatedBy'];
    // $updated_on = $_POST['updatedOn'];


    if($stmt = $mysqli->prepare("INSERT INTO tbl_case_records
      (case_record_id, case_id, user_id, health_center_id, complaint, control_number)
      VALUES (?,?,?,?,?,?)")) {

        $stmt->bind_param('iiiiss', $case_record_id, $case_id, $user_id, $health_center_id, $complaint, $control_number);

        $stmt->execute();

        // $stmt->bind_result($result);
        //
        // $stmt->fetch();

        $stmt->close();

      } else {
         echo "Error Uploading Case Records";
      }

    // if($stmt2 = $mysqli->prepare("INSERT INTO tbl_case_record_history
    //   (case_record_id, case_record_status_id, updated_by, updated_on) VALUES (?,?,?,?)")) {
    //
    //     $stmt2->bind_param('iiis', $case_record_id, $case_record_status_id, $updated_by, $updated_on);
    //
    //     $stmt2->execute();
    //
    //     $stmt2->close();
    //
    //
    // } else {
    //   echo "Error Uploading Case Record History";
    // }

    echo "Successfully Uploaded Case Record";

  $mysqli->close();

  } else {
    echo "Unable to process request!";
  }



?>
