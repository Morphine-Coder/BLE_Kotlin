package kr.intin.ble_kotlin.ui

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.intin.ble_kotlin.R
import kr.intin.ble_kotlin.databinding.FragmentResultBinding
import kr.intin.ble_kotlin.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class ResultFragment : Fragment() {

    private val model : MainViewModel by activityViewModels()
    private lateinit var binding: FragmentResultBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val today = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

        binding.btnReturn.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
        }

        lifecycleScope.launch (Dispatchers.IO) {
            model.db.getTime(today).observe(requireActivity(), androidx.lifecycle.Observer { times ->
                if(times[times.size-1] >= 60) binding.tvTime.text = "${times[times.size-1] / 60} : ${times[times.size-1] % 60}"
                else binding.tvTime.text = "0 : ${times[times.size-1] % 60}"
            })
        }

    }

}