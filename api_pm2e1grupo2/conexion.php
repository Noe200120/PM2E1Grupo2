<?php
$conexion = new mysqli("localhost", "root", "", "pm2e1grupo2");
if ($conexion->connect_error) {
    die("Error de conexión: " . $conexion->connect_error);
}
header("Access-Control-Allow-Origin: *");
?>
