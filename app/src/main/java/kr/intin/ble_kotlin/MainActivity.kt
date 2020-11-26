package kr.intin.ble_kotlin

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import kr.intin.ble_kotlin.adapter.BLEAdapter
import kr.intin.ble_kotlin.databinding.ActivityMainBinding
import kr.intin.ble_kotlin.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    val model : MainViewModel by viewModels()
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        it[Manifest.permission.ACCESS_FINE_LOCATION].let { fineBool ->
            val date = SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(Date())
            Toast.makeText(applicationContext, "위치권한 : $date", Toast.LENGTH_SHORT).show()
            model.scan()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        binding.lifecycleOwner = this

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        requestPermission.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) or super.onSupportNavigateUp()
    }

}