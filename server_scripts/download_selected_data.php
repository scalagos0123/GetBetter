<?php

if(($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['caseRecordId'])) {
  require_once('db_connect.php');

  $caseRecordId = $_POST['caseRecordId'];

  if($stmt = $mysqli->prepare("SELECT * FROM "))


}








 ?>
