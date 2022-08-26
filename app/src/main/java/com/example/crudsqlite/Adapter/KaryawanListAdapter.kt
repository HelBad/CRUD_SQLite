package com.example.crudsqlite.Adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudsqlite.Model.Karyawan
import com.example.crudsqlite.R
import kotlinx.android.synthetic.main.dialog_add_person.view.*
import kotlinx.android.synthetic.main.listmenu.view.*

class KaryawanListAdapter(
    private var context: Context,
    private var onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<KaryawanListAdapter.ViewHolder>() {
    private var mArrayList: ArrayList<Karyawan> = ArrayList()

    fun setList(mArrayList: ArrayList<Karyawan>) {
        this.mArrayList.clear()
        this.mArrayList = mArrayList
        notifyDataSetChanged()
    }

    fun update(pos: Int, karyawanBean: Karyawan) {
        this.mArrayList[pos] = karyawanBean
        notifyItemChanged(pos)
    }

    fun removeAt(pos: Int) {
        this.mArrayList.removeAt(pos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.listmenu, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mArrayList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(karyawanBean: Karyawan) {
            itemView.namaListmenu.text = karyawanBean.nama
            itemView.alamatListmenu.text = karyawanBean.alamat
            itemView.telpListmenu.text = karyawanBean.telp

//            val recordImage: ByteArray = karyawanBean.image!!
//            val bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.size)
//            itemView.imgView.setImageBitmap(bitmap)

            itemView.deleteListmenu.setOnClickListener {
                onItemClickListener.onDeleteClicked(adapterPosition, karyawanBean)
            }

            itemView.editListmenu.setOnClickListener {
                onItemClickListener.onEditClicked(adapterPosition, karyawanBean)
            }
        }
    }
}