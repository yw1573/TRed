package com.yw1573.tred.fragment

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.yw1573.tred.MainActivity
import com.yw1573.tred.R
import com.yw1573.tred.databinding.FragmentChartBinding
import util.StringUtils

class ChartFragment : Fragment() {
    private val dbHelper = MainActivity.dbHelper
    private var phaseStr = MainActivity.phaseStr
    private var lineChartView: LineChart? = null
    private var spinnerChart: Spinner? = null
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 加上一个全部数据的折线
        phaseStr += getString(R.string.string_all)
        lineChartView = binding.chartLineChart
        spinnerChart = binding.chartSpinner

        // 给Spinner添加选项
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireActivity(), R.layout.spinner_item, phaseStr
        )
        spinnerChart?.adapter = adapter
        spinnerChart?.setSelection(phaseStr.size - 1)
        spinnerChart?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                draw(phaseStr[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("TRed", "不会走到这里")
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * 绘制图表
     * @param phase String: 标签
     */
    private fun draw(phase: String) {
        // 清除之前的图表信息
        lineChartView?.clear()
        // 获取对应标签的数据
        val bbs = dbHelper?.query(phase)
        val lineSet: MutableList<Entry> = ArrayList()
        bbs?.forEachIndexed { index, bloodSugar ->
            lineSet.add(
                Entry(index.toFloat(), bloodSugar.value)
            )
        }
        val zAxisFormatter = object : ValueFormatter() {

            override fun getPointLabel(entry: Entry?): String {
                val index = entry!!.x.toInt()
                if (bbs != null) {
                    if (index >= 0 && index < bbs.size) {
                        return entry.y.toString() + "[${bbs[index].phase}]"
                    }
                }
                return ""
            }
        }
        // 数据Set
        val dataSet = LineDataSet(lineSet, null)
        dataSet.color = Color.BLUE
        dataSet.valueFormatter = zAxisFormatter
        dataSet.setDrawValues(true)
        dataSet.valueTextSize = 10f
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTypeface = Typeface.DEFAULT

        val lineData = LineData(dataSet)
        lineChartView?.setBackgroundColor(Color.WHITE)
        lineChartView?.isAutoScaleMinMaxEnabled = true
        lineChartView?.isDragEnabled = true
        lineChartView?.isDragXEnabled = true
        lineChartView?.isDragYEnabled = true
        lineChartView?.setDrawGridBackground(true)
        lineChartView?.setTouchEnabled(true)
        lineChartView?.setScaleEnabled(true)
        lineChartView?.setPinchZoom(true)
        val description: Description = lineChartView!!.description
        description.isEnabled = false
        description.textSize = 12F
        description.textColor = Color.parseColor("#8A2BE2")
        description.textAlign = Paint.Align.RIGHT
        val xAxisFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                if (bbs != null) {
                    if (index >= 0 && index < bbs.size) {
                        return StringUtils.conversionTime(
                            bbs[index].timestamp,
                            format = "MM月dd日HH:mm"
                        )
                    }
                }
                return "null"
            }
        }

        // val yAxisFormatter = object : ValueFormatter() {
        //     override fun getFormattedValue(value: Float): String {
        //         return value.toString()
        //     }
        // }
        val limitLine = LimitLine(11.1f, "血糖警戒线")
        limitLine.isEnabled = true
        limitLine.lineColor = Color.parseColor("#FF0000")
        limitLine.lineWidth = 1f
        limitLine.textColor = Color.parseColor("#FF0000")
        limitLine.textSize = 10f
        limitLine.typeface = Typeface.DEFAULT_BOLD
        limitLine.labelPosition = LimitLine.LimitLabelPosition.LEFT_BOTTOM
        // 允许X轴缩放
        lineChartView?.isScaleXEnabled = true
        // 不允许Y轴缩放
        lineChartView?.isScaleYEnabled = false

        var xAxis: XAxis
        run {
            xAxis = lineChartView!!.xAxis
            xAxis.enableGridDashedLine(10f, 10f, 0f)
            // 设置最小间隔
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(true)
            xAxis.setDrawAxisLine(true)
            // 设置X轴坐标系颜色
            xAxis.axisLineColor = Color.BLACK
            xAxis.valueFormatter = xAxisFormatter
            // 避免首尾标签被剪切
            // xAxis.setAvoidFirstLastClipping(true)
            // 设置坐标轴位置在下方
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            // 设置X轴内容角度
            xAxis.labelRotationAngle = -45f
            // 设置X轴坐标系线条宽度
            xAxis.axisLineWidth = 1f
            xAxis.typeface = Typeface.DEFAULT_BOLD
        }
        var yAxis: YAxis
        run {
            yAxis = lineChartView!!.axisLeft
            yAxis.axisLineColor = Color.BLACK
            lineChartView?.axisRight?.isEnabled = false
            yAxis.enableGridDashedLine(10f, 10f, 0f)
            yAxis.addLimitLine(limitLine)
            yAxis.axisLineWidth = 1f
            yAxis.typeface = Typeface.DEFAULT_BOLD
            // yAxis.valueFormatter = yAxisFormatter
        }

        lineChartView?.data = lineData
    }
}
