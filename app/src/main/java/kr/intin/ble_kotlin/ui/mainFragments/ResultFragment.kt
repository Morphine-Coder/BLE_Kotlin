package kr.intin.ble_kotlin.ui.mainFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
<<<<<<< HEAD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.intin.ble_kotlin.MainActivity
import kr.intin.ble_kotlin.R
import kr.intin.ble_kotlin.data.dao.UseTimeDAO
import kr.intin.ble_kotlin.databinding.FragmentResultBinding
import kr.intin.ble_kotlin.di.DBModule
=======
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.intin.ble_kotlin.R
import kr.intin.ble_kotlin.databinding.FragmentResultBinding
>>>>>>> c6b32022a662bda61efeb35214f1a0da5400d90c
import kr.intin.ble_kotlin.ui.Sub.SubActivity
import kr.intin.ble_kotlin.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
<<<<<<< HEAD
import javax.inject.Inject

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding

    @Inject
    private lateinit var db : UseTimeDAO

=======

class ResultFragment : Fragment() {

    private val model : MainViewModel by activityViewModels()
    private lateinit var binding: FragmentResultBinding

>>>>>>> c6b32022a662bda61efeb35214f1a0da5400d90c
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val today = SimpleDateFormat("yyyy-MM-dd").format(Date())

        binding.btnReturn.setOnClickListener {
            if(findNavController().currentDestination?.id == R.id.resultFragment){
<<<<<<< HEAD
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
=======
                findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
>>>>>>> c6b32022a662bda61efeb35214f1a0da5400d90c
            }
        }

        binding.btnDb.setOnClickListener {
            if(findNavController().currentDestination?.id == R.id.resultFragment){
                findNavController().navigate(R.id.action_resultFragment_to_DBFragment)
            }
        }

<<<<<<< HEAD

        lifecycleScope.launch (Dispatchers.IO){
            val times = db.getTime(today)
            binding.tvTime.text = if(times[times.size-1] >= 60){
                when {
                    ((times[times.size-1] / 60) < 10) and ((times[times.size-1] % 60) < 10) -> {
                        "0${times[times.size-1] / 60}:0${times[times.size-1]%60}"
                    }
                    ((times[times.size-1] / 60) >= 10) and ((times[times.size-1] % 60) < 10) -> {
                        "${times[times.size-1] / 60}:0${times[times.size-1]%60}"
                    }
                    ((times[times.size-1] / 60) < 10) and ((times[times.size-1] % 60) >= 10) -> {
                        "0${times[times.size-1] / 60}:${times[times.size-1]%60}"
                    }
                    else -> {
                        "${times[times.size-1] / 60}:${times[times.size-1]%60}"
                    }
                }
            } else {
                if(times[times.size-1]% 60 < 10) "00:0${times[times.size-1]%60}"
                else "00:${times[times.size-1]%60}"
            }

        }
=======
        binding.btnIntent.setOnClickListener {
            val intent = Intent(context, SubActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        lifecycleScope.launch (Dispatchers.IO){
            val times = model.db.getTime(today)
            binding.tvTime.text = model.timeString(times[times.size-1])

        }

>>>>>>> c6b32022a662bda61efeb35214f1a0da5400d90c
    }

}