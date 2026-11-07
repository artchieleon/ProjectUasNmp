<?php
try {
    require "koneksi.php";
    $result = $conn->query("SELECT * FROM events ORDER BY tanggal ASC");
    echo json_encode($result->fetch_all(MYSQLI_ASSOC));
    $result->free();
    $conn->close();
} catch (mysqli_sql_exception $e) {
    error_log($e->getMessage());
    http_response_code(500);
    echo json_encode(["pesan" => "Terjadi kesalahan pada server"]);
}