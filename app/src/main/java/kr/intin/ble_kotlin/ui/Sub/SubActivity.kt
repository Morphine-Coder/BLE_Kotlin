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

<<<<<<< HEAD
        val binding : ActivitySubBinding = DataBindingUtil.setContentView(this,R.layout.activity_sub)

=======
        val binding = DataBindingUtil.setContentView<ActivitySubBinding>(this,R.layout.activity_sub)

        binding.btnMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
>>>>>>> c6b32022a662bda61efeb35214f1a0da5400d90c
    }
}