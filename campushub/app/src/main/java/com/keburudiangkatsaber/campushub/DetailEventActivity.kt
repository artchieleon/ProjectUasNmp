package com.keburudiangkatsaber.campushub

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.material.snackbar.Snackbar
import com.keburudiangkatsaber.campushub.databinding.ActivityDetailEventBinding
import com.keburudiangkatsaber.campushub.model.Event
import com.keburudiangkatsaber.campushub.network.ApiConfig
import com.keburudiangkatsaber.campushub.network.VolleySingleton
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private lateinit var event: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = IntentCompat.getParcelableExtra(intent, "event", Event::class.java)
        if (data == null) {
            finish()
            return
        }
        event = data

        tampilkanData()

        binding.btnDaftar.setOnClickListener {
            val i = Intent(this, DaftarEventActivity::class.java)
            i.putExtra("event_id", event.id)
            i.putExtra("event_judul", event.judul)
            startActivity(i)
        }
        binding.btnBookmark.setOnClickListener { tambahBookmark() }
        binding.btnLokasi.setOnClickListener { bukaLokasi() }
        binding.btnHubungi.setOnClickListener { hubungiPanitia() }
    }

    private fun tampilkanData() {
        binding.tvJudul.text = event.judul
        binding.tvInfo.text = "${event.kategori} • ${event.tipe}"
        binding.tvTanggal.text = "Tanggal: ${event.tanggal}"
        binding.tvLokasi.text = "Lokasi: ${event.lokasi}"

        val biaya = if (event.biaya == 0) "Gratis" else "Rp ${event.biaya}"
        val sertifikat = if (event.sertifikat == 1) " • Dapat sertifikat" else ""
        binding.tvBiaya.text = "Biaya: $biaya$sertifikat"

        binding.tvPembicara.text = "Pembicara: ${event.pembicara ?: "-"}"
        binding.tvDeskripsi.text = event.deskripsi

        if (!event.flyerUrl.isNullOrEmpty()) {
            Picasso.get().load(event.flyerUrl).into(binding.imgFlyer)
        }
    }

    private fun bukaLokasi() {
        val lokasi = Uri.encode(event.lokasi ?: "")
        val uri = if (event.latitude != null && event.longitude != null) {
            Uri.parse("geo:${event.latitude},${event.longitude}?q=$lokasi")
        } else {
            Uri.parse("geo:0,0?q=$lokasi")
        }
        try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(binding.root, "Aplikasi peta tidak ditemukan", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun hubungiPanitia() {
        val nomor = event.kontakPanitia
        if (nomor.isNullOrEmpty()) {
            Snackbar.make(binding.root, "Kontak panitia belum tersedia", Snackbar.LENGTH_SHORT).show()
            return
        }
        try {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$nomor")))
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(binding.root, "Aplikasi telepon tidak ditemukan", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun tambahBookmark() {
        val url = ApiConfig.BASE_URL + "tambah_bookmark.php"
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val pesan = try {
                    JSONObject(response).getString("pesan")
                } catch (e: Exception) {
                    "Berhasil"
                }
                Snackbar.make(binding.root, pesan, Snackbar.LENGTH_SHORT).show()
            },
            Response.ErrorListener {
                Snackbar.make(binding.root, "Gagal menyimpan bookmark", Snackbar.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("event_id" to event.id.toString())
            }
        }
        request.tag = "bookmark"
        VolleySingleton.getInstance(this).requestQueue.add(request)
    }

    override fun onStop() {
        super.onStop()
        VolleySingleton.getInstance(this).requestQueue.cancelAll("bookmark")
    }
}