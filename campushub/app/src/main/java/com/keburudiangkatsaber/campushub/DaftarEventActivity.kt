package com.keburudiangkatsaber.campushub

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.material.snackbar.Snackbar
import com.keburudiangkatsaber.campushub.databinding.ActivityDaftarEventBinding
import com.keburudiangkatsaber.campushub.network.ApiConfig
import com.keburudiangkatsaber.campushub.network.VolleySingleton
import org.json.JSONObject

class DaftarEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarEventBinding
    private var eventId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eventId = intent.getIntExtra("event_id", 0)
        binding.tvJudulEvent.text = intent.getStringExtra("event_judul")

        binding.btnKirim.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val nrp = binding.etNrp.text.toString().trim()

            if (nama.isEmpty() || nrp.isEmpty()) {
                Snackbar.make(binding.root, "Nama dan NRP wajib diisi", Snackbar.LENGTH_SHORT).show()
            } else {
                kirimPendaftaran(nama, nrp)
            }
        }
    }

    private fun kirimPendaftaran(nama: String, nrp: String) {
        binding.btnKirim.isEnabled = false

        val url = ApiConfig.BASE_URL + "daftar_event.php"
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val pesan = try {
                    JSONObject(response).getString("pesan")
                } catch (e: Exception) {
                    "Pendaftaran berhasil"
                }
                Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()
                finish()
            },
            Response.ErrorListener {
                binding.btnKirim.isEnabled = true
                Snackbar.make(binding.root, "Gagal mengirim pendaftaran", Snackbar.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "event_id" to eventId.toString(),
                    "nama" to nama,
                    "nrp" to nrp
                )
            }
        }
        request.tag = "daftar_event"
        VolleySingleton.getInstance(this).requestQueue.add(request)
    }

    override fun onStop() {
        super.onStop()
        VolleySingleton.getInstance(this).requestQueue.cancelAll("daftar_event")
    }
}