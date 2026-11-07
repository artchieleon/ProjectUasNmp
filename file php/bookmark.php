<?php
header("Content-Type: application/json; charset=utf-8");

try {
    require "koneksi.php";
    $sql = "SELECT e.* FROM events e
            JOIN bookmark b ON e.id = b.event_id
            ORDER BY b.id DESC";
    $result = $conn->query($sql);
    echo json_encode($result->fetch_all(MYSQLI_ASSOC));
    $result->free();
    $conn->close();
} catch (mysqli_sql_exception $e) {
    error_log($e->getMessage());
    http_response_code(500);
    echo json_encode(["pesan" => "Terjadi kesalahan pada server"]);
}