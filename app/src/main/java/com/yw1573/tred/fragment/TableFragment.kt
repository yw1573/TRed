package com.yw1573.tred.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yw1573.tred.MainActivity
import com.yw1573.tred.adapter.OuterAdapter
import com.yw1573.tred.databinding.FragmentTableBinding
import util.BloodSugar
import util.StringUtils
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

// data class BloodSugar(
//     var id: Int = 0,
//     var timestamp: Long = 0,
//     var phase: String = "",
//     var value: Float = 0.0f
// )

data class DisplayData(
    val id: Int,
    val date: String,
    val time: String,
    val phase: String,
    val value: String,
    val standard: String
)


class TableFragment : Fragment() {

    private var _binding: FragmentTableBinding? = null
    private val binding get() = _binding!!
    private val dbHelper = MainActivity.dbHelper
    private val bloodSugars = dbHelper!!.query(false)
    private var bloodSugarMap: Map<String, List<DisplayData>> = mapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTableBinding.inflate(inflater, container, false)
        val root: View = binding.root
        bloodSugarMap = bloodSugarNormalization(bloodSugars)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TRed", bloodSugars.size.toString())
        Log.d("TRed", bloodSugarMap.size.toString())
        val adapter = OuterAdapter(bloodSugarMap, requireActivity())
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(requireActivity())
        rv.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * 对数据进行归一化处理
     * @param bloodSugars List<BloodSugar>
     * @return Map<String, List<BloodSugar>>
     */
    private fun bloodSugarNormalization(bloodSugars: List<BloodSugar>): Map<String, List<DisplayData>> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Shanghai")
        val bloodSugarMap = mutableMapOf<String, MutableList<DisplayData>>()

        for (bloodSugar in bloodSugars) {
            val date = StringUtils.conversionTime(bloodSugar.timestamp, "yyyy年MM月dd日")
            val time = StringUtils.conversionTime(bloodSugar.timestamp, "HH:mm")
            var standard = "正常"
            val v = bloodSugar.value
            when (bloodSugar.phase) {
                "空腹" -> {
                    if (v > 6.1) {
                        standard = "偏高"
                    }
                    if (v < 4.4) {
                        standard = "偏低"
                    }
                }

                "餐前" -> {
                    if (v > 6.1) {
                        standard = "偏高"
                    }
                    if (v < 4.4) {
                        standard = "偏低"
                    }
                }

                "餐后" -> {
                    if (v > 7.2) {
                        standard = "偏高"
                    }
                    if (v < 5.0) {
                        standard = "偏低"
                    }
                }

                "睡前" -> {
                    if (v > 6.1) {
                        standard = "偏高"
                    }
                    if (v < 4.4) {
                        standard = "偏低"
                    }
                }

                "随机" -> {
                    if (v > 6.1) {
                        standard = "偏高"
                    }
                    if (v < 4.4) {
                        standard = "偏低"
                    }
                }
            }
            bloodSugarMap.getOrPut(date) { mutableListOf() }.add(
                DisplayData(
                    bloodSugar.id, date, time, bloodSugar.phase, bloodSugar.value.toString(), standard
                )
            )
        }
        return bloodSugarMap
    }
}



