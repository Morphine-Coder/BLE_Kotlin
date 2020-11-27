package kr.intin.ble_kotlin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kr.intin.ble_kotlin.R
import kr.intin.ble_kotlin.databinding.FragmentResultBinding
import kr.intin.ble_kotlin.viewmodel.MainViewModel

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

        binding.btnReturn.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
        }
    }

}