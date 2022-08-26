package com.example.crudsqlite.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.widget.Toast
import com.example.crudsqlite.Model.Karyawan

class DatabaseQueryClass(private val context: Context) {
    val allKaryawanLists: ArrayList<Karyawan>
        @SuppressLint("Range")
        get() {
            val databaseHelper = DatabaseHelper.getInstance(context)
            val sqLiteDatabase = databaseHelper.readableDatabase
            var cursor: Cursor? = null
            try {
                cursor = sqLiteDatabase.query(Config.TABLE_KARYAWAN,
                    null, null, null, null, null, null, null)
                if (cursor != null)
                    if (cursor.moveToFirst()) {
                        val karyawanList = ArrayList<Karyawan>()
                        do {
                            val id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ID))
                            val nama = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NAMA))
                            val alamat = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ALAMAT))
                            val telp = cursor.getString(cursor.getColumnIndex(Config.COLUMN_TELP))
                            val image = cursor.getBlob(cursor.getColumnIndex(Config.COLUMN_IMAGE))

                            karyawanList.add(Karyawan(id, nama, alamat, telp, image))
                        } while (cursor.moveToNext())

                        return karyawanList
                    }
            } catch (e: Exception) {
                Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show()
            } finally {
                cursor?.close()
                sqLiteDatabase.close()
            }
            return ArrayList()
        }

    fun insertKaryawan(karyawan: Karyawan): Long {
        var id: Long = -1
        val databaseHelper = DatabaseHelper.getInstance(context)
        val sqLiteDatabase = databaseHelper.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Config.COLUMN_NAMA, karyawan.nama)
        contentValues.put(Config.COLUMN_ALAMAT, karyawan.alamat)
        contentValues.put(Config.COLUMN_TELP, karyawan.telp)
        contentValues.put(Config.COLUMN_IMAGE, karyawan.image)

        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_KARYAWAN, null, contentValues)
        } catch (e: SQLiteException) {
            Toast.makeText(context, "Operation failed: " + e.message, Toast.LENGTH_LONG).show()
        } finally {
            sqLiteDatabase.close()
        }
        return id
    }

    @SuppressLint("Range")
    fun getKaryawanById(idOfKaryawan: Long): Karyawan? {

        val databaseHelper = DatabaseHelper.getInstance(context)
        val sqLiteDatabase = databaseHelper.readableDatabase

        var cursor: Cursor? = null
        var karyawan: Karyawan? = null
        try {

            cursor = sqLiteDatabase.query(
                Config.TABLE_KARYAWAN, null,
                Config.COLUMN_ID + " = ? ", arrayOf(idOfKaryawan.toString()), null, null, null
            )

            if (cursor!!.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ID))
                val nama = cursor.getString(cursor.getColumnIndex(Config.COLUMN_NAMA))
                val alamat = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ALAMAT))
                val telp = cursor.getString(cursor.getColumnIndex(Config.COLUMN_TELP))
                val image = cursor.getBlob(cursor.getColumnIndex(Config.COLUMN_IMAGE))

                karyawan = Karyawan(id, nama, alamat, telp, image)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show()
        } finally {
            cursor?.close()
            sqLiteDatabase.close()
        }

        return karyawan
    }

    fun updateKaryawan(karyawan: Karyawan): Long {

        var rowCount: Long = 0
        val databaseHelper = DatabaseHelper.getInstance(context)
        val sqLiteDatabase = databaseHelper.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Config.COLUMN_NAMA, karyawan.nama)
        contentValues.put(Config.COLUMN_ALAMAT, karyawan.alamat)
        contentValues.put(Config.COLUMN_TELP, karyawan.telp)
        contentValues.put(Config.COLUMN_IMAGE, karyawan.image)

        try {
            rowCount = sqLiteDatabase.update(
                Config.TABLE_KARYAWAN, contentValues,
                Config.COLUMN_ID + " = ? ",
                arrayOf(karyawan.id.toString())
            ).toLong()
        } catch (e: SQLiteException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        } finally {
            sqLiteDatabase.close()
        }
        return rowCount
    }

    fun deleteKaryawanById(idOfKaryawan: Long): Long {
        var deletedRowCount: Long = -1
        val databaseHelper = DatabaseHelper.getInstance(context)
        val sqLiteDatabase = databaseHelper.writableDatabase

        try {
            deletedRowCount = sqLiteDatabase.delete(
                Config.TABLE_KARYAWAN,
                Config.COLUMN_ID + " = ? ",
                arrayOf(idOfKaryawan.toString())
            ).toLong()
        } catch (e: SQLiteException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        } finally {
            sqLiteDatabase.close()
        }
        return deletedRowCount
    }
}