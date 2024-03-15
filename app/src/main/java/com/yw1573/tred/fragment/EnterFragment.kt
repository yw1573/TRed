package com.yw1573.tred.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.input.input
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.DateTimePicker
import com.yw1573.tred.MainActivity
import com.yw1573.tred.R
import com.yw1573.tred.databinding.FragmentEnterBinding
import util.BloodSugar
import util.StringUtils
import java.util.Calendar

class EnterFragment : Fragment() {

    private var _binding: FragmentEnterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var mPicker: DateTimePicker? = null
    private var mSpinner: Spinner? = null
    private var mEdtBloodSugar: EditText? = null
    private var mBtnEnter: Button? = null
    private var mBtnDelete: Button? = null
    private val dbHelper = MainActivity.dbHelper
    private val phaseStr = MainActivity.phaseStr
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initControls()
        controlsListen()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * 从后台切换到前台调用
     */
    override fun onResume() {
        super.onResume()
        pickerSmartChoice()
        spinnerSmartChoice()
    }

    /**
     * 初始化控件
     */
    private fun initControls() {
        mPicker = binding.enterDateTimePicker
        mSpinner = binding.enterSpinner
        mEdtBloodSugar = binding.enterEdtBloodSugar
        mBtnEnter = binding.enterBtnEnter
        mBtnDelete = binding.enterBtnDelete

        mPicker?.setDisplayType(
            intArrayOf(
                DateTimeConfig.YEAR,
                DateTimeConfig.MONTH,
                DateTimeConfig.DAY,
                DateTimeConfig.HOUR,
                DateTimeConfig.MIN
            )
        )

        mPicker?.setGlobal(DateTimeConfig.GLOBAL_CHINA)
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireActivity(), R.layout.spinner_item, phaseStr
        )
        mSpinner?.adapter = adapter

        mEdtBloodSugar?.setOnClickListener {
            mEdtBloodSugar?.selectAll()
        }
        mEdtBloodSugar?.setText("0")
        spinnerSmartChoice()
    }

    /**
     * 控件监听
     */
    private fun controlsListen() {
        var phase = 0
        val bs = BloodSugar()

        mSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                phase = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("TRed", "不会走到这里")
            }
        }

        // 监听删除按钮按下事件
        mBtnDelete?.setOnClickListener {
            MaterialDialog(requireActivity()).show {
                icon(R.mipmap.icon)
                input(hint = "输入删除的序号") { _, text ->
                    dbHelper!!.delete(text.toString().toInt())
                }
                positiveButton(R.string.string_confirm) { }
                negativeButton(R.string.string_cancel) { }

                getActionButton(WhichButton.POSITIVE).updateTextColor(Color.BLACK)
                getActionButton(WhichButton.NEGATIVE).updateTextColor(Color.BLACK)
            }
        }

        // 监听记录按钮按下事件
        mBtnEnter?.setOnClickListener {
            bs.value = java.lang.Float.valueOf(mEdtBloodSugar?.text.toString())
            bs.timestamp = mPicker!!.getMillisecond()
            bs.id = dbHelper!!.count() + 1
            bs.phase = phaseStr[phase]
            MaterialDialog(requireActivity()).show {
                icon(R.mipmap.icon)
                title(R.string.confirm_enter_information)
                message(
                    text = "序号: ${bs.id}\n时间: " + StringUtils.conversionTime(
                        bs.timestamp, "yyyy年MM月dd日 HH:mm"
                    ) + "\n" + "标签: " + bs.phase + "\n" + "血糖: " + bs.value.toString()
                )

                positiveButton(R.string.string_confirm) {
                    dbHelper.insert(bs)
                    mEdtBloodSugar!!.setText("0")
                    mPicker!!.setDefaultMillisecond(System.currentTimeMillis())
                }
                negativeButton(R.string.string_cancel) {
                    Log.d("TRed", "取消记录操作")
                }
                getActionButton(WhichButton.POSITIVE).updateTextColor(Color.BLACK)
                getActionButton(WhichButton.NEGATIVE).updateTextColor(Color.BLACK)
            }
        }
    }

    /**
     * 智能选择根据当前时间段调整Spinner选择器位置
     */
    private fun spinnerSmartChoice() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) // 获取24小时制的当前小时数
        val index = when {
            hour < 10 -> 0
            hour in 10..12 -> 1 // 包括10点和12点
            hour in 13..14 -> 2 // 包括13点和14点
            hour in 17..19 -> 1 // 包括17点和19点
            hour in 20..21 -> 2 // 包括20点和21点
            hour in 22..23 || hour in 0..1 -> 3 // 包括22点到23点和0点到1点
            else -> 4
        }
        mSpinner?.setSelection(index)
    }

    private fun pickerSmartChoice() {
        mPicker?.setDefaultMillisecond(System.currentTimeMillis())
    }
}