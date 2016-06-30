<?php

// if($_SERVER['REQUEST_METHOD'] == 'POST') {
  require_once('db_connect.php');

  if($stmt = $mysqli->prepare('SELECT c.case_record_id, c.user_id,
    c.complaint, c.health_center_id, d.record_status_id, d.updated_by,
    d.updated_on FROM tbl_case_records AS c
    INNER JOIN (SELECT h.* FROM tbl_case_record_history AS h
      INNER JOIN (SELECT case_record_id, record_status_id, MAX(updated_on) AS maxdate
      FROM tbl_case_record_history WHERE record_status_id IN ( 3, 6, 7 )
      GROUP BY case_record_id) t
    ON h.case_record_id = t.case_record_id
    AND h.updated_on = t.maxdate) d
    ON c.case_record_id = d.case_record_id')) {

      $stmt->execute();

      $stmt->bind_result($case_record_id, $user_id, $complaint, $health_center_id,
      $record_status_id, $updated_by, $updated_on);

      $result = array();

      while($stmt->fetch()) {
        array_push($result, array('case_record_id'=>$case_record_id,
        'user_id'=>$user_id,
        'complaint'=>$complaint,
        'health_center_id'=>$health_center_id,
        'record_status_id'=>$record_status_id,
        'updated_by'=>$updated_by,
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
