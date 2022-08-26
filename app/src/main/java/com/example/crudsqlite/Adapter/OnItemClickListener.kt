package com.example.crudsqlite.Adapter

import com.example.crudsqlite.Model.Karyawan

interface OnItemClickListener {
    fun onEditClicked(pos: Int, karyawan: Karyawan)
    fun onDeleteClicked(adapterPosition: Int, karyawanBean: Karyawan)
}