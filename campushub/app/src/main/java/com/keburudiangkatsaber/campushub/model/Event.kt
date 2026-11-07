package com.keburudiangkatsaber.campushub.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: Int,
    val judul: String?,
    val kategori: String?,
    val tipe: String?,
    val tanggal: String?,
    val lokasi: String?,
    val latitude: Double?,
    val longitude: Double?,
    val biaya: Int,
    val sertifikat: Int,
    val pembicara: String?,
    val deskripsi: String?,
    @SerializedName("kontak_panitia") val kontakPanitia: String?,
    @SerializedName("flyer_url") val flyerUrl: String?
) : Parcelable