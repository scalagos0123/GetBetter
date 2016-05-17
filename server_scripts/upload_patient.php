<?php

  header('Content-type : bitmap; charset=utf-8');

  if (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['id'])) {
    require_once('db_connect.php');

    $patient_id = $_POST['id'];
    $patient_first_name = $_POST['firstName'];
    $patient_middle_name = $_POST['middleName'];
    $patient_last_name = $_POST['lastName'];

    $birthdate = $_POST['birthdate'];
    $gender = $_POST['genderId'];
    $civil_status = $_POST['civilStatusId'];
    $health_center = $_POST['healthCenterId'];

    $role_id = 6;
    $gender_id = 1;
    $civil_status_id = 1;

    $file_path = "uploads/";
    $full_path = "http://128.199.205.226/get_better/" . $file_path;
    $image_name = $_POST['imageName'];
    $profile_image_path = $full_path . $image_name;
    $image = $_POST['image'];
    $decoded_image = base64_decode($image);


    // $file = fopen($file_path . $image_name, 'wb');
    // $is_written = fwrite($file, $decoded_image);
    // fclose($file);

    // function base64_to_jpeg($base64_string, $output_file) {
    // $ifp = fopen($output_file, "wb");
    //
    // $data = explode(',', $base64_string);
    //
    // fwrite($ifp, base64_decode($data[1]));
    // fclose($ifp);
    //
    // return $output_file;
    // }
    // $image_created= imagecreatefromstring($decoded_image);

    //header('Content-type: image/jpeg');
    // imagesavealpha($image_created, true);
    // imagejpeg($image_created);
    // imagedestroy($image_created);



    if($gender_stmt = $mysqli->prepare("SELECT gender_id FROM tbl_genders WHERE gender_name = ?")) {

      $gender_stmt->bind_param('s', $gender);

      $gender_stmt->execute();

      $gender_stmt->bind_result($gender_id);

      $gender_stmt->fetch();

      $gender_stmt->close();
    }

    if($cs_stmt = $mysqli->prepare("SELECT civil_status_id FROM tbl_civil_statuses WHERE civil_status_name = ?")) {

      $cs_stmt->bind_param('s', $civil_status);

      $cs_stmt->execute();

      $cs_stmt->bind_result($civil_status_id);

      $cs_stmt->fetch();

      $cs_stmt->close();
    }
    if(file_put_contents($file_path . $image_name, $decoded_image) != false) {

      if($stmt = $mysqli->prepare("INSERT INTO tbl_users (user_id, first_name, middle_name, last_name,
      birthdate, gender_id, civil_status_id, role_id, profile_url, default_health_center)
      VALUES (?,?,?,?,?,?,?,?,?,?)")) {

        $stmt->bind_param('issssiiisi', $patient_id, $patient_first_name,
        $patient_middle_name, $patient_last_name, $birthdate, $gender_id,
        $civil_status_id, $role_id, $profile_image_path, $health_center);

        $stmt->execute();



        // $dir = fopen($file_path . $image_name, 'wb');
        // fwrite($dir, $image_created);
        // fclose($dir);
        //file_put_contents($file_path . $image_name, $decoded_image);

        $stmt->close();

      }

      echo 'Successfully Uploaded!';
    } else {
      echo 'Failed to upload image';
    }
        // $sql_query = "INSERT INTO tbl_users (user_id, first_name, middle_name, last_name,
    // birthdate, gender_id, civil_status_id, role_id, profile_url, default_health_center)
    // VALUES ('$patient_id', '$patient_first_name', '$patient_middle_name', '$patient_last_name', '$birthdate',
    // '$gender_id', '$civil_status_id', '$role_id', '$profile_image_name', '$health_center')";
    //
    // if(mysqli_query($con, $sql_query)) {
    //   file_put_contents($actual_path, base64_decode($image));
    //   echo "Successfully Uploaded";
    // } else {
    //   echo "Error Uploading Image";
    // }
    //
    // mysqli_close($con);

  $mysqli->close();

  } else {
    echo 'Error Uploading Patient Records';

  }



?>
