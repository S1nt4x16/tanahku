package com.example.tanahku.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tanahku.R
import com.example.tanahku.database.DatabaseHelper
import com.example.tanahku.model.Land
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TanahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tanah_card, parent, false)
        return TanahViewHolder(view)
    }

    override fun onBindViewHolder(holder: TanahViewHolder, position: Int) {
        val land = listTanah[position]
        
        // 3. Pemformatan Mata Uang (Currency Formatting)
        holder.tvHarga.text = formatRupiah(land.harga.toDoubleOrNull() ?: 0.0)
        holder.tvNama.text = land.nama
        holder.tvSertifikat.text = land.sertifikat

        // 1. Penanganan Foto (Tampil Foto dari URI)
        if (!land.foto.isNullOrEmpty()) {
            try {
                holder.ivFoto.setImageURI(Uri.parse(land.foto))
            } catch (e: Exception) {
                holder.ivFoto.setImageResource(R.drawable.lahan_kavling)
            }
        } else {
            holder.ivFoto.setImageResource(R.drawable.lahan_kavling)
        }

        // 2. Fitur Hapus (Delete Data)
        holder.btnDelete.setOnClickListener {
            val landId = land.id
            if (landId != null) {
                val success = dbHelper.deleteTanah(landId)
                if (success > 0) {
                    val currentPos = holder.adapterPosition
                    if (currentPos != RecyclerView.NO_POSITION) {
                        listTanah.removeAt(currentPos)
                        notifyItemRemoved(currentPos)
                        notifyItemRangeChanged(currentPos, listTanah.size)
                        Toast.makeText(holder.itemView.context, "Lahan berhasil dihapus", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(holder.itemView.context, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = listTanah.size

    fun updateData(newList: List<Land>) {
        listTanah = newList.toMutableList()
        notifyDataSetChanged()
    }

    // Helper Pemformatan Mata Uang (Rupiah)
    private fun formatRupiah(number: Double): String {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(number)
    }
}
