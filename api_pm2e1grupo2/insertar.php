<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: text/plain; charset=utf-8");


file_put_contents("log.txt",
    "POST: " . json_encode($_POST) .
    "\nFILES: " . json_encode($_FILES) . "\n\n",
    FILE_APPEND
);


include "conexion.php";


error_reporting(E_ALL);
ini_set('display_errors', 1);


if (
    isset($_POST["nombre"]) &&
    isset($_POST["telefono"]) &&
    isset($_POST["correo"]) &&
    isset($_POST["lat"]) &&
    isset($_POST["lng"]) &&
    isset($_FILES["firma"])
) {
    $nombre   = $_POST["nombre"];
    $telefono = $_POST["telefono"];
    $correo   = $_POST["correo"];
    $lat      = $_POST["lat"];
    $lng      = $_POST["lng"];
    $firma    = $_FILES["firma"]["name"];

   
    $verificar = $conexion->query("SELECT id FROM contactos WHERE correo='$correo'");

    if ($verificar && $verificar->num_rows > 0) {
        echo "EXISTE";
        exit; 
    }

    if (!file_exists("firmas")) {
        mkdir("firmas", 0777, true);
    }

    $nombreArchivo = uniqid("firma_") . "_" . basename($firma);
    $ruta = "firmas/" . $nombreArchivo;


    if (move_uploaded_file($_FILES["firma"]["tmp_name"], $ruta)) {

        
        $sql = "INSERT INTO contactos (nombre, telefono, correo, lat, lng, firma)
                VALUES ('$nombre','$telefono','$correo','$lat','$lng','$ruta')";

        if ($conexion->query($sql) === TRUE) {
            echo "OK";
        } else {
            echo "Error SQL: " . $conexion->error;
        }

    } else {
        echo "Error al mover archivo: " . $_FILES["firma"]["error"];
    }

} else {
    echo "Error: datos incompletos.";
}
?>
