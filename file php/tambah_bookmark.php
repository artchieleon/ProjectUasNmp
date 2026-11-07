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

    // Cegah bookmark ganda
    $cek = $conn->prepare("SELECT id FROM bookmark WHERE event_id = ?");
    $cek->bind_param("i", $id);
    $cek->execute();
    $cek->store_result();

    if ($cek->num_rows > 0) {
        echo json_encode(["sukses" => true, "pesan" => "Event sudah ada di bookmark"]);
        $cek->close();
        $conn->close();
        exit;
    }
    $cek->close();

    $stmt = $conn->prepare("INSERT INTO bookmark (event_id) VALUES (?)");
    $stmt->bind_param("i", $id);
    $stmt->execute();

    http_response_code(201);
    echo json_encode([
        "sukses" => true,
        "pesan"  => "Event ditambahkan ke bookmark",
        "id"     => $conn->insert_id
    ]);
    $stmt->close();
    $conn->close();
} catch (mysqli_sql_exception $e) {
    error_log($e->getMessage());
    http_response_code(500);
    echo json_encode(["sukses" => false, "pesan" => "Terjadi kesalahan pada server"]);
}