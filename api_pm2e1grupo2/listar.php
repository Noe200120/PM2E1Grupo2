<?php
include "conexion.php";
$buscar = $_GET["buscar"] ?? "";

if ($buscar != "") {
    $sql = "SELECT * FROM contactos 
            WHERE nombre LIKE '%$buscar%' 
               OR telefono LIKE '%$buscar%' 
               OR correo LIKE '%$buscar%'
            ORDER BY id DESC";
} else {
    $sql = "SELECT * FROM contactos ORDER BY id DESC";
}

$res = $conexion->query($sql);
$datos = [];

while ($fila = $res->fetch_assoc()) {
    $datos[] = $fila;
}

echo json_encode($datos);
?>
