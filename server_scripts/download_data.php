<?php

// if($_SERVER['REQUEST_METHOD'] == 'POST') {
  require_once('db_connect.php');

  if($stmt = $mysqli->prepare('SELECT c.case_record_id, c.user_id, c.complaint,
    c.health_center_id, h.record_status_id, h.updated_on FROM tbl_case_records AS c,
    tbl_case_record_history AS h WHERE c.case_record_id = h.case_record_id AND
    (h.record_status_id = 6 || h.record_status_id = 3 || h.record_status_id = 7)
    GROUP BY c.case_record_id')) {

      $stmt->execute();

      $stmt->bind_result($case_record_id, $user_id, $complaint, $health_center_id,
      $record_status_id, $updated_on);

      $result = array();

      while($stmt->fetch()) {
        array_push($result, array('case_record_id'=>$case_record_id,
        'user_id'=>$user_id,
        'complaint'=>$complaint,
        'health_center_id'=>$health_center_id,
        'record_status_id'=>$record_status_id,
        'updated_on'=>$updated_on
      ));

      }

      $stmt->close();
      echo json_encode(array('case_records'=>$result));

  } else {
    echo 'SQL Query Error!';
  }

$mysqli->close();

// else {
//   header($_SERVER['SERVER_PROTOCOL'] . ' 404 Not Found');
// }




?>
