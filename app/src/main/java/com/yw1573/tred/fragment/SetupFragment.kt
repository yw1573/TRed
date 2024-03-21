package com.yw1573.tred.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.fragment.app.Fragment
import com.yw1573.tred.SplashActivity
import com.yw1573.tred.R
import com.yw1573.tred.databinding.FragmentSetupBinding
import util.StringUtils
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader


class SetupFragment : Fragment() {

    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!
    private lateinit var mBtnUpdate: Button
    private lateinit var mBtnExport: Button
    private lateinit var mBtnImport: Button

    private val sqlExportActivity =
        registerForActivityResult(CreateDocument("text/plain")) { uri: Uri? ->
            uri?.let {
                Log.d("TRed", "sqlExportActivity-Uri: $uri")
                val list = SplashActivity.dbHelper!!.sqlExport()
                var content = ""
                for (l in list) {
                    content += l
                    content += "\n"
                }
                writeFile(uri, content)
            }
        }
    private val sqlImportActivity =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                Log.d("TRed", "sqlImportActivity-Uri: $uri")
                val sqlList = readFile(uri)
                SplashActivity.dbHelper?.exec(sqlList)
            }
        }


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
        mBtnExport = binding.setupBtnExport
        mBtnImport = binding.setupBtnImport
    }

    private fun controlsListen() {
        // 监听更新按钮
        mBtnUpdate.setOnClickListener {
            val manager =
                com.azhon.appupdate.manager.DownloadManager.Builder(requireActivity()).run {
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
        // 监听导出按钮
        mBtnExport.setOnClickListener {
            sqlExportActivity.launch(
                "TRed-" + StringUtils.conversionTime(
                    System.currentTimeMillis(),
                    "yyyyMMddHHmmss"
                )
            )
        }
        // 监听导入按钮
        mBtnImport.setOnClickListener {
            sqlImportActivity.launch(arrayOf("text/plain"))
        }
    }

    private fun writeFile(uri: Uri, content: String) {
        try {
            requireActivity().contentResolver.openFileDescriptor(uri, "w")?.use { pfd ->
                FileOutputStream(pfd.fileDescriptor).use { fileOutputStream ->
                    fileOutputStream.write(content.toByteArray())
                }
            }
        } catch (e: IOException) {
            Log.e("TRed", e.toString())
        }
    }

    private fun readFile(uri: Uri): List<String> {
        val lines = mutableListOf<String>()
        try {
            requireActivity().contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String? = reader.readLine()
                    while (line != null) {
                        if (line.isNotEmpty()) { // 确保不添加空行
                            lines.add(line)
                        }
                        line = reader.readLine()
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("TRed", e.toString())
        }
        return lines
    }
}
