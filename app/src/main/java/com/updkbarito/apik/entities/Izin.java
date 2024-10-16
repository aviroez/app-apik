package com.updkbarito.apik.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Izin implements Parcelable {
    private int id;
    
    @SerializedName("pengguna_id")
    private int penggunaId;
    private Pengguna pengguna = new Pengguna();

    @SerializedName("vendor_id")
    private int vendorId;

    @SerializedName("lokasi_id")
    private int lokasiId;
    private Lokasi lokasi = new Lokasi();
    private String nomor;
    private String nama;

    @SerializedName("no_spk")
    private String noSpk;

    @SerializedName("no_spmk")
    private String noSpmk;

    @SerializedName("pengawas_vendor")
    private String pengawasVendor;

    @SerializedName("pengawas_vendor_telp")
    private String pengawasVendorTelp;

    @SerializedName("pengawas_k3")
    private String pengawasK3;

    @SerializedName("pengawas_k3_telp")
    private String pengawasK3Telp;

    @SerializedName("direksi_lapangan")
    private String direksiLapangan;

    @SerializedName("direksi_lapangan_telp")
    private String direksiLapanganTelp;

    @SerializedName("direksi_lapangan_id")
    private int direksiLapanganId;

    @SerializedName("direksi_pekerjaan")
    private String direksiPekerjaan;

    @SerializedName("direksi_pekerjaan_telp")
    private String direksiPekerjaanTelp;

    @SerializedName("direksi_pekerjaan_id")
    private int direksiPekerjaanId;

    @SerializedName("direksi_lapangan_pengguna")
    private Pengguna direksiLapanganPengguna = new Pengguna();

    @SerializedName("lokasi_lengkap")
    private String lokasiLengkap;
    private String jenis;
    private String status;

    @SerializedName("tanggal_mulai")
    private String tanggalMulai;

    @SerializedName("tanggal_sampai")
    private String tanggalSampai;

    @SerializedName("tanggal_pengajuan")
    private String tanggalPengajuan;

    @SerializedName("jam_mulai")
    private String jamMulai;

    @SerializedName("jam_sampai")
    private String jamSampai;

    @SerializedName("lokasi_nama")
    private String lokasiNama;

    @SerializedName("lokasi_kode")
    private String lokasiKode;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("deleted_at")
    private String deletedAt;

    @SerializedName("button")
    private String button;

    @SerializedName("izin_persetujuan_list")
    private ArrayList<IzinPersetujuan> izinPersetujuanList = new ArrayList<>();

    @SerializedName("izin_detail_list")
    private ArrayList<IzinDetail> izinDetailList = new ArrayList<>();

    public Izin() {
    }

    protected Izin(Parcel in) {
        id = in.readInt();
        penggunaId = in.readInt();
        pengguna = in.readParcelable(Pengguna.class.getClassLoader());
        vendorId = in.readInt();
        lokasiId = in.readInt();
        lokasi = in.readParcelable(Lokasi.class.getClassLoader());
        nomor = in.readString();
        nama = in.readString();
        noSpk = in.readString();
        noSpmk = in.readString();
        pengawasVendor = in.readString();
        pengawasVendorTelp = in.readString();
        pengawasK3 = in.readString();
        pengawasK3Telp = in.readString();
        direksiLapangan = in.readString();
        direksiLapanganTelp = in.readString();
        direksiLapanganId = in.readInt();
        direksiPekerjaan = in.readString();
        direksiPekerjaanTelp = in.readString();
        direksiPekerjaanId = in.readInt();
        direksiLapanganPengguna = in.readParcelable(Pengguna.class.getClassLoader());
        lokasiLengkap = in.readString();
        jenis = in.readString();
        status = in.readString();
        tanggalMulai = in.readString();
        tanggalSampai = in.readString();
        tanggalPengajuan = in.readString();
        jamMulai = in.readString();
        jamSampai = in.readString();
        lokasiNama = in.readString();
        lokasiKode = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
        button = in.readString();
        izinPersetujuanList = in.createTypedArrayList(IzinPersetujuan.CREATOR);
        izinDetailList = in.createTypedArrayList(IzinDetail.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(penggunaId);
        dest.writeParcelable(pengguna, flags);
        dest.writeInt(vendorId);
        dest.writeInt(lokasiId);
        dest.writeParcelable(lokasi, flags);
        dest.writeString(nomor);
        dest.writeString(nama);
        dest.writeString(noSpk);
        dest.writeString(noSpmk);
        dest.writeString(pengawasVendor);
        dest.writeString(pengawasVendorTelp);
        dest.writeString(pengawasK3);
        dest.writeString(pengawasK3Telp);
        dest.writeString(direksiLapangan);
        dest.writeString(direksiLapanganTelp);
        dest.writeInt(direksiLapanganId);
        dest.writeString(direksiPekerjaan);
        dest.writeString(direksiPekerjaanTelp);
        dest.writeInt(direksiPekerjaanId);
        dest.writeParcelable(direksiLapanganPengguna, flags);
        dest.writeString(lokasiLengkap);
        dest.writeString(jenis);
        dest.writeString(status);
        dest.writeString(tanggalMulai);
        dest.writeString(tanggalSampai);
        dest.writeString(tanggalPengajuan);
        dest.writeString(jamMulai);
        dest.writeString(jamSampai);
        dest.writeString(lokasiNama);
        dest.writeString(lokasiKode);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
        dest.writeString(button);
        dest.writeTypedList(izinPersetujuanList);
        dest.writeTypedList(izinDetailList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Izin> CREATOR = new Creator<Izin>() {
        @Override
        public Izin createFromParcel(Parcel in) {
            return new Izin(in);
        }

        @Override
        public Izin[] newArray(int size) {
            return new Izin[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPenggunaId() {
        return penggunaId;
    }

    public void setPenggunaId(int penggunaId) {
        this.penggunaId = penggunaId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getLokasiId() {
        return lokasiId;
    }

    public void setLokasiId(int lokasiId) {
        this.lokasiId = lokasiId;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoSpk() {
        return noSpk;
    }

    public void setNoSpk(String noSpk) {
        this.noSpk = noSpk;
    }

    public String getNoSpmk() {
        return noSpmk;
    }

    public void setNoSpmk(String noSpmk) {
        this.noSpmk = noSpmk;
    }

    public String getPengawasVendor() {
        return pengawasVendor;
    }

    public void setPengawasVendor(String pengawasVendor) {
        this.pengawasVendor = pengawasVendor;
    }

    public String getPengawasVendorTelp() {
        return pengawasVendorTelp;
    }

    public void setPengawasVendorTelp(String pengawasVendorTelp) {
        this.pengawasVendorTelp = pengawasVendorTelp;
    }

    public String getPengawasK3() {
        return pengawasK3;
    }

    public void setPengawasK3(String pengawasK3) {
        this.pengawasK3 = pengawasK3;
    }

    public String getPengawasK3Telp() {
        return pengawasK3Telp;
    }

    public void setPengawasK3Telp(String pengawasK3Telp) {
        this.pengawasK3Telp = pengawasK3Telp;
    }

    public String getDireksiLapangan() {
        return direksiLapangan;
    }

    public void setDireksiLapangan(String direksiLapangan) {
        this.direksiLapangan = direksiLapangan;
    }

    public String getDireksiLapanganTelp() {
        return direksiLapanganTelp;
    }

    public void setDireksiLapanganTelp(String direksiLapanganTelp) {
        this.direksiLapanganTelp = direksiLapanganTelp;
    }

    public int getDireksiLapanganId() {
        return direksiLapanganId;
    }

    public void setDireksiLapanganId(int direksiLapanganId) {
        this.direksiLapanganId = direksiLapanganId;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getTanggalMulai() {
        return tanggalMulai;
    }

    public void setTanggalMulai(String tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public String getTanggalSampai() {
        return tanggalSampai;
    }

    public void setTanggalSampai(String tanggalSampai) {
        this.tanggalSampai = tanggalSampai;
    }

    public String getTanggalPengajuan() {
        return tanggalPengajuan;
    }

    public void setTanggalPengajuan(String tanggalPengajuan) {
        this.tanggalPengajuan = tanggalPengajuan;
    }

    public String getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(String jamMulai) {
        this.jamMulai = jamMulai;
    }

    public String getJamSampai() {
        return jamSampai;
    }

    public void setJamSampai(String jamSampai) {
        this.jamSampai = jamSampai;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Lokasi getLokasi() {
        return lokasi;
    }

    public void setLokasi(Lokasi lokasi) {
        this.lokasi = lokasi;
    }

    public Pengguna getDireksiLapanganPengguna() {
        return direksiLapanganPengguna;
    }

    public void setDireksiLapanganPengguna(Pengguna direksiLapanganPengguna) {
        this.direksiLapanganPengguna = direksiLapanganPengguna;
    }

    public String getDireksiPekerjaan() {
        return direksiPekerjaan;
    }

    public void setDireksiPekerjaan(String direksiPekerjaan) {
        this.direksiPekerjaan = direksiPekerjaan;
    }

    public String getDireksiPekerjaanTelp() {
        return direksiPekerjaanTelp;
    }

    public void setDireksiPekerjaanTelp(String direksiPekerjaanTelp) {
        this.direksiPekerjaanTelp = direksiPekerjaanTelp;
    }

    public int getDireksiPekerjaanId() {
        return direksiPekerjaanId;
    }

    public void setDireksiPekerjaanId(int direksiPekerjaanId) {
        this.direksiPekerjaanId = direksiPekerjaanId;
    }

    public String getLokasiLengkap() {
        return lokasiLengkap;
    }

    public void setLokasiLengkap(String lokasiLengkap) {
        this.lokasiLengkap = lokasiLengkap;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLokasiNama() {
        return lokasiNama;
    }

    public void setLokasiNama(String lokasiNama) {
        this.lokasiNama = lokasiNama;
    }

    public String getLokasiKode() {
        return lokasiKode;
    }

    public void setLokasiKode(String lokasiKode) {
        this.lokasiKode = lokasiKode;
    }

    public Pengguna getPengguna() {
        return pengguna;
    }

    public void setPengguna(Pengguna pengguna) {
        this.pengguna = pengguna;
    }

    public ArrayList<IzinPersetujuan> getIzinPersetujuanList() {
        return izinPersetujuanList;
    }

    public void setIzinPersetujuanList(ArrayList<IzinPersetujuan> izinPersetujuanList) {
        this.izinPersetujuanList = izinPersetujuanList;
    }

    public ArrayList<IzinDetail> getIzinDetailList() {
        return izinDetailList;
    }

    public void setIzinDetailList(ArrayList<IzinDetail> izinDetailList) {
        this.izinDetailList = izinDetailList;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
}
