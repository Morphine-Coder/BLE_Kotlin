package kr.intin.ble_kotlin.ui.mainFragments

import android.bluetooth.BluetoothProfile
import android.content.Intent
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
import dagger.hilt.android.AndroidEntryPoint
import kr.intin.ble_kotlin.R
import kr.intin.ble_kotlin.adapter.ChatAdapter
import kr.intin.ble_kotlin.databinding.FragmentCommunicateBinding
import kr.intin.ble_kotlin.ui.Sub.SubActivity
import kr.intin.ble_kotlin.ui.dialogs.TimeDialog
import kr.intin.ble_kotlin.ui.dialogs.TimeDialogListener
import kr.intin.ble_kotlin.viewmodel.MainViewModel

@AndroidEntryPoint
class CommunicateFragment : Fragment() {

    private lateinit var binding: FragmentCommunicateBinding
    private val adapter : ChatAdapter = ChatAdapter()
    private val model : MainViewModel by activityViewModels()
    private val TAG = CommunicateFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_communicate, container, false)
        binding.recyclerReceiveMsg.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.model = model

        binding.btnTime.setOnClickListener {
            val dialog = TimeDialog.TimeDialogBuilder().setBtnClickListener(object : TimeDialogListener {
                override fun onClickListener(time: Int) {
                    model.setTime = time
                    model.sendTime()
                }
            }).create()

            dialog.show(parentFragmentManager, dialog.tag)
        }

        model.responseData.observe(requireActivity(), Observer {
            adapter.addResponse(it)
        })

        model.connectState.observe(requireActivity(), Observer {
            if (it == BluetoothProfile.STATE_DISCONNECTED) {
                if(findNavController().currentDestination?.id == R.id.communicateFragment){
                    Toast.makeText(context, "연결이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, SubActivity::class.java)
                    startActivity(intent)
                }
            }
        })

        model.usedTimer.observe(requireActivity(), Observer {
            binding.tvTimer.text = model.timeString(it)
        })

        model.toastingMessage.observe(requireActivity(), Observer {
            if(it != "") {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

    }

}