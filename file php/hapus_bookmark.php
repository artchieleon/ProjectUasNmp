<?php
header("Content-Type: application/json; charset=utf-8");

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    http_response_code(405);
    echo json_encode(["sukses" => false, "pesan" => "Gunakan method POST"]);
    exit;
}

$event_id = $_POST["event_id"] ?? "";

if (!ctype_digit($event_id)) {
    http_response_code(400);
    echo json_encode(["sukses" => false, "pesan" => "event_id (angka) wajib diisi"]);
    exit;
}

try {
    require "koneksi.php";
    $id = (int) $event_id;

    $stmt = $conn->prepare("DELETE FROM bookmark WHERE event_id = ?");
    $stmt->bind_param("i", $id);
    $stmt->execute();

    echo json_encode(["sukses" => true, "pesan" => "Bookmark dihapus"]);
    $stmt->close();
    $conn->close();
} catch (mysqli_sql_exception $e) {
    error_log($e->getMessage());
    http_response_code(500);
    echo json_encode(["sukses" => false, "pesan" => "Terjadi kesalahan pada server"]);
}