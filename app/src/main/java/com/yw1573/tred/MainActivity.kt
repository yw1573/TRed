package com.yw1573.tred

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yw1573.tred.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import util.SqliteDB


private const val DB_VERSION = 1

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        @SuppressLint("StaticFieldLeak")
        var dbHelper: SqliteDB? = null
        var phaseStr: Array<String> = arrayOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = SqliteDB(this, DB_VERSION)
        phaseStr = dbHelper!!.queryPhaseStr()
        lifecycleScope.launch {
            delay(1000)
            jumpToHome()
        }
    }

    private fun jumpToHome() {
        startActivity(Intent(this, HomeActivity::class.java));
    }
}