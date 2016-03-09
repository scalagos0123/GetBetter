<?php

  if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    require_once('db_connect.php');

    $file_path = "uploads";
    $actual_path = "http://128.199.205.226/get_better/$file_path/";
    $patient_id = $_POST['id'];
    $patient_first_name = $_POST['firstName'];
    $patient_middle_name = $_POST['middleName'];
    $patient_last_name = $_POST['lastName'];
    $birthdate = $_POST['birthdate'];
    $gender = $_POST['genderId'];
    $civil_status = $_POST['civilStatusId'];
    $role_id = 6;
    $health_center = 51;
    $profile_image_name = $actual_path . $_POST['imageName'];
    $image = $_POST["image"];

    // $sql = "SELECT gender_id FROM tbl_genders WHERE gender_name = $gender";
    // $res = mysqli_query($con, $sql);
    //
    // $gender_id = 1;
    // while($row = mysqli_fetch_array($res)) {
    //   $gender_id = $row['gender_id'];
    // }
    $gender_id = 1;

    $civil_status_id = 1;

    $sql_query = "INSERT INTO tbl_users (user_id, first_name, middle_name, last_name,
    birthdate, gender_id, civil_status_id, role_id, profile_url, default_health_center)
    VALUES ('$patient_id', '$patient_first_name', '$patient_middle_name', '$patient_last_name', '$birthdate',
    '$gender_id', '$civil_status_id', '$role_id', '$profile_image_name', '$health_center')";

    if(mysqli_query($con, $sql_query)) {
      file_put_contents($actual_path, base64_decode($image));
      echo "Successfully Uploaded";
    } else {
      echo "Error Uploading Image";
    }

    mysqli_close($con);

  } else {
    echo "Error";

  }



?>
