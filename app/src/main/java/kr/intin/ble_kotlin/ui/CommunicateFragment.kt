package kr.intin.ble_kotlin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kr.intin.ble_kotlin.R
import kr.intin.ble_kotlin.adapter.ChatAdapter
import kr.intin.ble_kotlin.databinding.FragmentCommunicateBinding
import kr.intin.ble_kotlin.viewmodel.MainViewModel

class CommunicateFragment : Fragment() {

    private lateinit var binding: FragmentCommunicateBinding
    private val adapter : ChatAdapter = ChatAdapter()
    private val TAG = CommunicateFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_communicate, container, false)
        binding.recyclerReceiveMsg.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model : MainViewModel by activityViewModels()
        binding.model = model
        model.responseData.observe(requireActivity(), Observer {
            //Adapter에 추가.
            adapter.addResponse(it)
        })
        //state > int
        model.connectState.observe(requireActivity(), Observer {
            if (!it) {
                Toast.makeText(context, "연결이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                //findNavController().navigate(R.id.action_communicateFragment_to_mainFragment)
                findNavController().navigateUp()
            }
        })

        binding.btnSend.setOnClickListener {
            model.sendData(binding.editMsg.text.toString())
            //adapter.addResponse(binding.editMsg.text.toString())
            binding.editMsg.text.clear()
        }
    }

}