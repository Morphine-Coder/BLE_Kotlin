package kr.intin.ble_kotlin.ui

import android.bluetooth.le.ScanResult
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.intin.ble_kotlin.R
import kr.intin.ble_kotlin.adapter.BLEAdapter
import kr.intin.ble_kotlin.databinding.FragmentMainBinding
import kr.intin.ble_kotlin.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    val model : MainViewModel by activityViewModels()
    val TAG = MainFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val model : MainViewModel = ViewModelProvider.AndroidViewModelFactory(Application()).create(MainViewModel::class.java)
        val adapter = BLEAdapter(model)
        binding.recycler.adapter = adapter

        model.scanResultLiveData.observe(requireActivity(), Observer {
            adapter.addResult(it)
        })

        adapter.setItemClickListener(object : BLEAdapter.ItemClickListener{
            override fun onClick(scanResult: ScanResult?) {
                model.connect(scanResult)
                findNavController().navigate(R.id.action_mainFragment_to_communicateFragment)
            }
        })
    }
}