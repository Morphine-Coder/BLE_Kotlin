package kr.intin.ble_kotlin.ui.Sub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import kr.intin.ble_kotlin.MainActivity
import kr.intin.ble_kotlin.R
import kr.intin.ble_kotlin.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivitySubBinding = DataBindingUtil.setContentView(this,R.layout.activity_sub)

    }
}