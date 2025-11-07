<?php
include "conexion.php";

$id = $_POST["id"];
$nombre = $_POST["nombre"];
$telefono = $_POST["telefono"];
$correo = $_POST["correo"];
$lat = $_POST["lat"];
$lng = $_POST["lng"];

$sql = "UPDATE contactos 
        SET nombre='$nombre', telefono='$telefono', correo='$correo', lat='$lat', lng='$lng' 
        WHERE id=$id";

if ($conexion->query($sql)) {
    echo "OK";
} else {
    echo "ERROR";
}
?>
