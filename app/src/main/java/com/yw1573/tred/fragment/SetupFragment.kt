package com.yw1573.tred.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.yw1573.tred.R
import com.yw1573.tred.databinding.FragmentSetupBinding


class SetupFragment : Fragment() {

    private var _binding: FragmentSetupBinding? = null
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
                apkVersionName("beta")
                apkSize("6MB")
                apkDescription("目前APP处理开发阶段，任何更新都将可能导致数据丢失，请谨慎选择！")
                build()
            }
            manager.download()
        }
    }
}
