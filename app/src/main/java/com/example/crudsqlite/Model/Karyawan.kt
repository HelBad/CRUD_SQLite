package com.example.crudsqlite.Model

class Karyawan(id: Int, var nama: String?, var alamat: String?, var telp: String?, var image: ByteArray?) {
    var id: Long = 0
    init {
        this.id = id.toLong()
    }
}
