package kr.intin.ble_kotlin.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.intin.ble_kotlin.R
import kr.intin.ble_kotlin.databinding.DialogTimeBinding

class TimeDialog : DialogFragment() {

    private lateinit var binding : DialogTimeBinding
    private var listener: TimeDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_time, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSet.setOnClickListener {
            listener?.onClickListener(binding.editMin.text.toString().toInt())
            dismiss()
        }

    }


    class TimeDialogBuilder {
        private val dialog = TimeDialog()

        fun create() : TimeDialog {
            return dialog
        }

        fun setBtnClickListener(listener: TimeDialogListener) : TimeDialogBuilder {
            dialog.listener = listener
            return this
        }

    }

}