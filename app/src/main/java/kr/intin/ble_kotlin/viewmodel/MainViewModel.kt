package kr.intin.ble_kotlin.viewmodel


import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.health.TimerStat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kr.intin.ble_kotlin.MainActivity
import kr.intin.ble_kotlin.data.dao.UseTimeDAO
import kr.intin.ble_kotlin.data.entity.UseTime
import kr.intin.ble_kotlin.di.annotation.RXService
import kr.intin.ble_kotlin.di.annotation.TXChat
import kr.intin.ble_kotlin.ui.dialogs.TimeDialog
import kr.intin.ble_kotlin.ui.dialogs.TimeDialogListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.experimental.and


class MainViewModel @ViewModelInject constructor(
    private val scanner: BluetoothLeScanner,
    @RXService private val RX_SERVICE_UUID: UUID,
    @TXChat private val TX_CHAR_UUID: UUID,
    val db: UseTimeDAO
) : ViewModel() {

    private val TAG = MainViewModel::class.java.simpleName

    val scanResultLiveData = MutableLiveData<ScanResult?>()
    private val scanCallback = object :ScanCallback(){
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            scanResultLiveData.postValue(result)
            Log.d(TAG, "addResult")
        }
    }

    private var bluetoothGatt: BluetoothGatt? = null
    private var characteristic: BluetoothGattCharacteristic? = null

    val responseData = MutableLiveData<String>()
    private val sendData = MutableLiveData<String>()
    val connectState = MutableLiveData<Int>()
    val usedTimer = MutableLiveData<Int>(0)
    val toastingMessage = MutableLiveData<String>("")
    var setTime = 0

    private val gattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    bluetoothGatt = gatt
                    bluetoothGatt?.discoverServices()
                    connectState.postValue(newState)
                    Log.d(TAG, "onConnectionStateChange : $newState / $status")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    connectState.postValue(newState)
                    bluetoothGatt?.disconnect()
                    bluetoothGatt = null
                    Log.d(TAG, "onConnectionStateChange : $newState / $status")
                }
                else -> {
                    Log.d(TAG, "onConnectionStateChange: $newState")
                }
            }

        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            val service = gatt?.getService(RX_SERVICE_UUID)
            characteristic = service?.getCharacteristic(TX_CHAR_UUID)
            gatt?.setCharacteristicNotification(characteristic, true)
            Log.d(TAG, "onServicesDiscovered")
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
            Log.d(TAG, "onCharacteristicRead")
            characteristic.let {
                if (it != null) {
                    viewModelScope.launch (Dispatchers.IO) {
                        showAdapter(it.value)
                    }

                }
                else {
                    viewModelScope.launch (Dispatchers.IO) {
                        showAdapter("errorCode : NULL".toByteArray())
                    }
                }
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            Log.d(TAG, "onCharacteristicWrite")
            characteristic.let {
                if (it != null) {
                    viewModelScope.launch (Dispatchers.IO) {
                        showAdapter(it.value)
                    }

                }
                else {
                    viewModelScope.launch (Dispatchers.IO) {
                        showAdapter("errorCode : NULL".toByteArray())
                    }
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            Log.d(TAG, "onCharacteristicChanged")
            characteristic.let {
                if (it != null) {
                    viewModelScope.launch (Dispatchers.IO) {
                        showAdapter(it.value)
                    }

                }
                else {
                    viewModelScope.launch (Dispatchers.IO) {
                        showAdapter("errorCode : NULL".toByteArray())
                    }
                }
            }
        }
    }


    private val timer = Timer()
    private var timerTask : TimerTask? = null
    private val today : String = SimpleDateFormat("yyyy-MM-dd").format(Date())
    private lateinit var startedTime: String

    fun scan() {
        scanner.startScan(scanCallback)
        Log.d(TAG, "startScan")
    }

    fun connect(result: ScanResult?, context: Context?) {
        stop()
        scanResultLiveData.value = result

        if(bluetoothGatt != null) {
            bluetoothGatt?.disconnect()
            bluetoothGatt = null
            Log.d(TAG, "connect: ble is not disconnected")
        }
        result?.device?.connectGatt(context, false, gattCallback)
    }

    private fun stop () {
        scanner.stopScan(scanCallback)
        Log.d(TAG, "stopScan")
    }

    private fun String2Byte(s: String?) : ByteArray? {
        return s?.toByteArray()
    }

    private suspend fun showAdapter(data: ByteArray) {
        val stringBuilder = StringBuilder()
        var s = String(data)
        for (b in data) {
            stringBuilder.append(String.format("%02X", b and 0xff.toByte()))
        }

        val date = SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(Date())

        if (s == "START-OK") {
            timerStart()
            startedTime = date
        }

        if (s == "TIME UP!!") {
            timerTask?.cancel()
            val usedTime = usedTimer.value
            val useTime = UseTime(index = Date().time.toInt(), usedTime = usedTime, usedDate = today, startedTime = startedTime)

            viewModelScope.launch (Dispatchers.IO){
                db.insertTime(useTime)
            }

            usedTimer.postValue(0)
        }

        s = "$s / $date"
        Log.d(TAG, s)
        responseData.postValue(s)
    }

    private fun sendData (data : String) {
        if(characteristic == null) {
            toastingMessage.postValue("연결이 되지 않았습니다.")
        }
        else{
            toastingMessage.postValue("")
            characteristic?.value = String2Byte(data)
            characteristic?.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            bluetoothGatt?.writeCharacteristic(characteristic)
        }
    }

    fun sendStart() {
        sendData("START")
        sendData.value = "START"
    }

    fun sendPause() {
        sendData("PAUSE")
        sendData.value = "PAUSE"
        timerTask?.cancel()
    }


    fun sendEnd() {
        sendData("END")
        sendData.value = "END"
        timerTask?.cancel()
        val usedTime = usedTimer.value
        val useTime = UseTime(index = Date().time.toInt(), usedTime = usedTime, usedDate = today, startedTime = startedTime)

        viewModelScope.launch (Dispatchers.IO){
            db.insertTime(useTime)
        }

        usedTimer.value = 0
    }

    fun sendTime() {

        when (setTime) {
            in 1..9 -> {
                sendData("TIME:0${setTime}")
                sendData.value = "TIME:0${setTime}"
            }

            0 -> toastingMessage.value = "시간을 설정해 주세요."

            else -> {
                sendData("TIME:${setTime}")
                sendData.value = "TIME:${setTime}"
            }
        }

    }

    fun sendInfo() {
        sendData("INFO")
        sendData.value = "INFO"
    }

    fun sendOff() {
        sendData("OFF")
        sendData.value = "OFF"
        
        timerTask?.cancel()
        val usedTime = usedTimer.value
        if(usedTime != 0) {
            val useTime = UseTime(index = Date().time.toInt(), usedTime = usedTime, usedDate = today, startedTime = startedTime)
            viewModelScope.launch (Dispatchers.IO){
                db.insertTime(useTime)
            }
            usedTimer.value = 0
        }

    }

    private fun makeTimerTask () : TimerTask {

        return object : TimerTask() {
            override fun run() {
                viewModelScope.launch {
                    usedTimer.postValue(usedTimer.value?.plus(1))
                    Log.d(TAG, "TIMER")
                }
            }
        }

    }

    private fun timerStart() {
        timerTask = makeTimerTask()
        timer.schedule(timerTask, 0, 1000)
    }

    fun timeString (sec : Int) : String {
       return if(sec >= 60){
            when {
                ((sec / 60) < 10) and ((sec % 60) < 10) -> {
                    "0${sec / 60}:0${sec%60}"
                }
                ((sec / 60) >= 10) and ((sec % 60) < 10) -> {
                    "${sec / 60}:0${sec%60}"
                }
                ((sec / 60) < 10) and ((sec % 60) >= 10) -> {
                    "0${sec / 60}:${sec%60}"
                }
                else -> {
                    "${sec / 60}:${sec%60}"
                }
            }
        } else {
            if(sec % 60 < 10) "00:0${sec%60}"
            else "00:${sec%60}"
        }
    }
}