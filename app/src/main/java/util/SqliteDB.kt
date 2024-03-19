package util

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.yw1573.tred.R
import java.io.FileOutputStream

const val BS_TABLE_NAME = "BloodSugars"
const val PS_TABLE_NAME = "PhaseString"

// 血糖数据以及数据库
data class BloodSugar(
    var id: Int = 0,
    var timestamp: Long = 0,
    var phase: String = "",
    var value: Float = 0.0f
)

class DatabaseHelper(private val context: Context) {
    companion object {
        const val DBNAME = "blood_sugar.db"
    }

    /**
     * 从assets目录中导入数据库
     */
    fun importDatabaseFromAssets() {
        val dataAssertFile = context.getDatabasePath(DBNAME)
        if (dataAssertFile.exists()) {
            return
        }
        dataAssertFile.parentFile?.mkdir()
        context.assets.open(DBNAME).use { inputStream ->
            FileOutputStream(dataAssertFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}

class SqliteDB(
    private val mContext: Context, private val version: Int
) : SQLiteOpenHelper(mContext, DatabaseHelper.DBNAME, null, version) {
    init {
        DatabaseHelper(mContext).importDatabaseFromAssets()
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("TRed", "Sqlite-onCreate")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("TRed", "Sqlite-onUpgrade")
    }

    /**
     * sql语句执行
     * @param sql String: sql语句
     */
    fun exec(sql: String) {
        val db = this.writableDatabase
        db.execSQL(sql)
        db.close()
    }

    /**
     * sql批量执行
     * @param sqlStatements List<String>: sql语句List
     */
    fun exec(sqlStatements: List<String>) {
        val db = this.writableDatabase
        db.beginTransaction() // 开始事务
        try {
            for (sql in sqlStatements) {
                try {
                    db.execSQL(sql)
                } catch (e: Exception) {
                    // 如果某条数据插入出错，捕获异常，打印日志，然后继续循环
                    Log.e("TRed", "Error inserting data: ${e.message}")
                }
            }
            db.setTransactionSuccessful() // 设置事务标记为成功
        } finally {
            db.endTransaction() // 结束事务
        }
        db.close()
    }

    /**
     * 插入
     * @param bs BloodSugar: BloodSugar结构数据
     */
    fun insert(bs: BloodSugar) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("id", bs.id)
        cv.put("timestamp", bs.timestamp)
        cv.put("phase", bs.phase)
        cv.put("value", bs.value)
        db.insert(BS_TABLE_NAME, null, cv)
        db.close()
    }

    /**
     * 查询
     * @return List<BloodSugar>
     */
    @SuppressLint("Range", "Recycle")
    fun query(order: Boolean): List<BloodSugar> {
        val list = mutableListOf<BloodSugar>()
        val db = this.readableDatabase
        val query = if (order) {
            """
                SELECT * FROM $BS_TABLE_NAME  ORDER BY timestamp ASC
                 """.trimIndent()
        } else {
            """
                SELECT * FROM $BS_TABLE_NAME  ORDER BY timestamp DESC
                 """.trimIndent()
        }

        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val bs = BloodSugar()
            bs.id = cursor.getInt(cursor.getColumnIndex("id"))
            bs.timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"))
            bs.phase = cursor.getString(cursor.getColumnIndex("phase"))
            bs.value = cursor.getFloat(cursor.getColumnIndex("value"))
            list.add(bs)
        }
        db.close()
        return list
    }

    /**
     * 查询: WHERE phase
     * @param phase String: 指定标签
     * @return List<BloodSugar>
     */
    @SuppressLint("Range", "Recycle")
    fun query(phase: String): List<BloodSugar> {
        val list = mutableListOf<BloodSugar>()
        val db = this.readableDatabase
        val all = mContext.getString(R.string.string_all)
        if (phase == all) {
            return this.query(true)
        } else {
            val sql = """
                SELECT * FROM $BS_TABLE_NAME WHERE phase = ? ORDER BY timestamp ASC
                """.trimIndent()
            val cursor = db.rawQuery(sql, arrayOf(phase))
            while (cursor.moveToNext()) {
                val bs = BloodSugar()
                bs.id = cursor.getInt(cursor.getColumnIndex("id"))
                bs.timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"))
                bs.phase = cursor.getString(cursor.getColumnIndex("phase"))
                bs.value = cursor.getFloat(cursor.getColumnIndex("value"))
                list.add(bs)
            }
            db.close()
            return list
        }
    }

    /**
     * 查询: 分页
     * @param lastSeenTimestamp Long: 本次查询时间戳起点
     * @param pageSize Int: 分页大小
     * @return Pair<List<BloodSugar>, Long>: <数据, 下一次查询时间戳起点>
     */
    @SuppressLint("Range", "Recycle")
    fun query(lastSeenTimestamp: Long, pageSize: Int): Pair<List<BloodSugar>, Long> {
        val list = mutableListOf<BloodSugar>()
        val db = this.readableDatabase
        var nextSeenTimestamp: Long = 0
        val sql = """
            SELECT * FROM BloodSugars WHERE timestamp > ?
            ORDER BY timestamp ASC
            LIMIT ?
        """.trimIndent()
        val cursor = db.rawQuery(sql, arrayOf(lastSeenTimestamp.toString(), pageSize.toString()))
        while (cursor.moveToNext()) {
            val bs = BloodSugar()
            bs.id = cursor.getInt(cursor.getColumnIndex("id"))
            bs.timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"))
            bs.phase = cursor.getString(cursor.getColumnIndex("phase"))
            bs.value = cursor.getFloat(cursor.getColumnIndex("value"))
            nextSeenTimestamp = bs.timestamp
            list.add(bs)
        }
        db.close()
        return Pair(list, nextSeenTimestamp)
    }

    /**
     * 删除
     * @param id Int: id
     */
    fun delete(id: Int) {
        val db = this.writableDatabase
        db.delete(BS_TABLE_NAME, "id=?", arrayOf(id.toString()))
        db.execSQL("UPDATE $BS_TABLE_NAME SET 'id'=(id-1) WHERE id > $id;")
        db.close()
    }

    /**
     * 清除数据库
     */
    fun clear() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $BS_TABLE_NAME")
        db.close()
    }

    /**
     * 数据计数
     * @return Int
     */
    @SuppressLint("Recycle")
    fun count(): Int {
        val db = this.readableDatabase
        val cursor = db.query(BS_TABLE_NAME, null, null, null, null, null, null, null)
        return cursor.count
    }

    /**
     * sql 导出
     * @return List<String>
     */
    fun sqlExport(): List<String> {
        val list = mutableListOf<String>()
        val bloodSugarList = this.query(true)
        for (bs in bloodSugarList) {
            val str =
                "INSERT INTO BloodSugars (timestamp, phase, value) VALUES (${bs.timestamp}, '${bs.phase}', ${bs.value});"
            list.add(str)
        }
        return list
    }

    /**
     * 查询标签
     * @return Array<String>
     */
    @SuppressLint("Recycle", "Range")
    fun queryPhaseStr(): Array<String> {
        var list: Array<String> = arrayOf()
        val db = this.readableDatabase
        val query = """ 
            SELECT * FROM $PS_TABLE_NAME ORDER BY id ASC             
        """.trimIndent()
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val str = cursor.getString(cursor.getColumnIndex("phase"))
            list += str
        }
        db.close()
        return list
    }

}