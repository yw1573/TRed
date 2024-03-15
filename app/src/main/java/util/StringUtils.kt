package util

import android.text.format.DateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

object StringUtils {
    fun conversionTime(
        time: String,
        format: String = "yyyy-MM-dd HH:mm:ss"
    ): Long {

        val ofPattern = DateTimeFormatter.ofPattern(format)
        return LocalDateTime.parse(time, ofPattern).toInstant(ZoneOffset.ofHours(8))
            .toEpochMilli()
    }

    /**
     * 时间戳格式化
     * @param time Long: 时间戳
     * @param format String: 格式化字符串
     * @return String
     */
    fun conversionTime(time: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        return DateFormat.format(format, time).toString()
    }

    /**
     * 根据时间戳获取周一，周二。。。。。。
     * @param time Long: 时间戳
     * @return String
     */
    fun getWeek(time: Long): String {
        val c = Calendar.getInstance()
        c.timeInMillis = time
        return when (c[Calendar.DAY_OF_WEEK]) {
            1 -> "周日"
            2 -> "周一"
            3 -> "周二"
            4 -> "周三"
            5 -> "周四"
            6 -> "周五"
            7 -> "周六"
            else -> ""
        }
    }
}