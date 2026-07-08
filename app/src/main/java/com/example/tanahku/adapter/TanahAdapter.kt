package com.example.tanahku.adapter

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tanahku.R
import com.example.tanahku.database.DatabaseHelper
import com.example.tanahku.model.Land
import com.example.tanahku.ui.DetailTanahActivity
import java.text.NumberFormat
import java.util.Locale

class TanahAdapter(
    private var listTanah: MutableList<Land>,
    private val dbHelper: DatabaseHelper
) : RecyclerView.Adapter<TanahAdapter.TanahViewHolder>() {

    class TanahViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFoto: ImageView = itemView.findViewById(R.id.iv_foto_tanah)
        val tvNama: TextView = itemView.findViewById(R.id.tv_lokasi_tanah)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga_tanah)
        val tvSertifikat: TextView = itemView.findViewById(R.id.tv_badge_sertifikat)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete_tanah)

        // INI DIA YANG KETINGGALAN: Daftarin tombol "Lihat Detail" dari XML lu
        val btnDetail: Button = itemView.findViewById(R.id.btn_lihat_detail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TanahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tanah_card, parent, false)
        return TanahViewHolder(view)
    }

    override fun onBindViewHolder(holder: TanahViewHolder, position: Int) {
        val land = listTanah[position]
        val context = holder.itemView.context

        val hargaFormat = formatRupiah(land.harga.toDoubleOrNull() ?: 0.0)
        holder.tvHarga.text = hargaFormat
        holder.tvNama.text = land.nama
        holder.tvSertifikat.text = land.sertifikat

        // Penanganan Foto
        if (!land.foto.isNullOrEmpty()) {
            try {
                holder.ivFoto.setImageURI(Uri.parse(land.foto))
            } catch (e: Exception) {
                holder.ivFoto.setImageResource(R.drawable.lahan_kavling)
            }
        } else {
            holder.ivFoto.setImageResource(R.drawable.lahan_kavling)
        }

        // ==========================================
        // 1. FUNGSI KLIK SEKARANG ADA DI TOMBOL DETAIL
        // ==========================================
        holder.btnDetail.setOnClickListener {
            val intent = Intent(context, DetailTanahActivity::class.java).apply {
                putExtra("EXTRA_NAMA_TANAH", land.nama)
                putExtra("EXTRA_HARGA_TANAH", hargaFormat)
                putExtra("EXTRA_FOTO_URI", land.foto)
            }
            context.startActivity(intent)
        }

        // Opsional: Biar kalau user klik sembarang di luar tombol (di area kartunya) juga tetep masuk ke detail
        holder.itemView.setOnClickListener {
            holder.btnDetail.performClick()
        }

        // ==========================================
        // 2. FITUR HAPUS DENGAN KONFIRMASI
        // ==========================================
        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Hapus Lahan")
                .setMessage("Apakah Anda yakin ingin menghapus '${land.nama}' dari listing?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ya, Hapus") { dialog, _ ->
                    val landId = land.id
                    if (landId != null) {
                        val success = dbHelper.deleteTanah(landId)
                        if (success > 0) {
                            val currentPos = holder.adapterPosition
                            if (currentPos != RecyclerView.NO_POSITION) {
                                listTanah.removeAt(currentPos)
                                notifyItemRemoved(currentPos)
                                notifyItemRangeChanged(currentPos, listTanah.size)
                                Toast.makeText(context, "Lahan berhasil dihapus", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun getItemCount(): Int = listTanah.size

    fun updateData(newList: List<Land>) {
        listTanah = newList.toMutableList()
        notifyDataSetChanged()
    }

    private fun formatRupiah(number: Double): String {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(number)
    }
}