<?php
include "conexion.php";
$id = $_GET["id"];
$sql = "DELETE FROM contactos WHERE id=$id";
if ($conexion->query($sql)) {
    echo "OK";
} else {
    echo "ERROR";
}
?>
