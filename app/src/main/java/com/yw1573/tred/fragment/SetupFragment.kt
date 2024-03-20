package com.yw1573.tred.fragment

import android.app.DownloadManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.yw1573.tred.R
import com.yw1573.tred.databinding.FragmentSetupBinding
import util.StringUtils


class SetupFragment : Fragment() {

    private var _binding: FragmentSetupBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mBtnUpdate: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initControls()
        controlsListen()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initControls() {
        mBtnUpdate = binding.setupBtnUpdate
    }

    private fun controlsListen() {
        mBtnUpdate?.setOnClickListener {
            val manager = com.azhon.appupdate.manager.DownloadManager.Builder(requireActivity()).run {
                apkUrl("https://github.com/yw1573/TRed/raw/master/TRed-Release.apk")
                apkName("TRed-Release.apk")
                smallIcon(R.mipmap.icon)
                apkVersionCode(2)
                apkVersionName("v1.0.1")
                apkSize("6MB")
                build()
            }
            manager.download()
            // MaterialDialog(requireActivity()).show {
            //     icon(R.mipmap.icon)
            //     title(R.string.confirm_to_update)
            //     message(R.string.update_tips)
            //
            //     positiveButton(R.string.string_confirm) {
            //
            //     }
            //     negativeButton(R.string.string_cancel) {
            //         Log.d("TRed", "取消记录操作")
            //     }
            //     getActionButton(WhichButton.POSITIVE).updateTextColor(Color.BLACK)
            //     getActionButton(WhichButton.NEGATIVE).updateTextColor(Color.BLACK)
            // }
        }
    }
}
