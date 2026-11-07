<?php
header("Content-Type: application/json; charset=utf-8");

if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    http_response_code(405);
    echo json_encode(["sukses" => false, "pesan" => "Gunakan method POST"]);
    exit;
}

$event_id = $_POST["event_id"] ?? "";
$nama     = trim($_POST["nama"] ?? "");
$nrp      = trim($_POST["nrp"] ?? "");

if (!ctype_digit($event_id) || $nama === "" || $nrp === "") {
    http_response_code(400);
    echo json_encode(["sukses" => false, "pesan" => "event_id, nama, dan nrp wajib diisi"]);
    exit;
}

try {
    require "koneksi.php";
    $id = (int) $event_id;

    $stmt = $conn->prepare("INSERT INTO pendaftaran (event_id, nama, nrp) VALUES (?, ?, ?)");
    $stmt->bind_param("iss", $id, $nama, $nrp);
    $stmt->execute();

    http_response_code(201);
    echo json_encode([
        "sukses" => true,
        "pesan"  => "Pendaftaran berhasil",
        "id"     => $conn->insert_id
    ]);
    $stmt->close();
    $conn->close();
} catch (mysqli_sql_exception $e) {
    error_log($e->getMessage());
    http_response_code(500);
    echo json_encode(["sukses" => false, "pesan" => "Terjadi kesalahan pada server"]);
}