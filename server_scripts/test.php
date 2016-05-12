<?php

  require_once('db_connect.php');

  $case_record_id = 1;
  $user_id = 447;
  $health_center_id = 51;
  $complaint = "Fever";
  $control_number = "44141AABABA";

  if($stmt = $mysqli->prepare("INSERT INTO tbl_case_records
    (case_record_id, user_id, health_center_id, complaint, control_number)
    VALUES (?,?,?,?,?)")) {

      $stmt->bind_param('iiiss', $case_record_id, $user_id, $health_center_id, $complaint, $control_number);

      $stmt->execute();

      $stmt->bind_result($result);

      $stmt->fetch();

      printf("Success! %s", $result);

      echo "Successfully Uploaded";

      $stmt->close();

    }

$mysqli->close()





?>
