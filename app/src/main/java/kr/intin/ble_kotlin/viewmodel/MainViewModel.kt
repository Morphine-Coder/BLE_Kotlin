package kr.intin.ble_kotlin.viewmodel


import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.intin.ble_kotlin.di.annotation.RXService
import kr.intin.ble_kotlin.di.annotation.TXChat
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.experimental.and


class MainViewModel @ViewModelInject constructor(
    private val scanner: BluetoothLeScanner,
    @RXService private val RX_SERVICE_UUID: UUID,
    @TXChat private val TX_CHAR_UUID: UUID
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
    val connectState = MutableLiveData<Int>()
    val usedTimer = MutableLiveData<Int>(0)

    private val gattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if(newState == 2){
                bluetoothGatt = gatt
                bluetoothGatt?.discoverServices()
                connectState.postValue(newState)
                Log.d(TAG, "onConnectionStateChange : $newState / $status")
            }
            else if (newState == 0){
                connectState.postValue(newState)
                bluetoothGatt = null
                Log.d(TAG, "onConnectionStateChange : $newState / $status")
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
    private lateinit var timerTask : TimerTask
    fun scan() {
        scanner.startScan(scanCallback)
        Log.d(TAG, "startScan")
    }

    fun connect(result: ScanResult?, context: Context?) {
        stop()
        scanResultLiveData.value = result
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
        val s = String(data)
        for (b in data) {
            stringBuilder.append(String.format("%02X", b and 0xff.toByte()))
        }
        Log.d(TAG, s)
        responseData.postValue(s)
    }

    private fun sendData (data : String) {
        if(characteristic == null) {
            usedTimer.value = 99999
        }
        else{
            characteristic?.value = String2Byte(data)
            characteristic?.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            bluetoothGatt?.writeCharacteristic(characteristic) // 수정해야 함. 11.27
        }
    }

    fun sendStart() {
        sendData("START")
        timerTask = makeTimerTask()
        timer.schedule(timerTask, 0, 1000)
    }

    fun sendPause() {
        timerTask.cancel()
        sendData("PAUSE")
    }

    fun sendEnd() {
        sendData("END")
        timerTask.cancel()
        usedTimer.value = 0
    }

    fun sendTime() {

    }

    fun sendInfo() {
        sendData("INF")
    }

    fun sendOff() {
        sendData("OFF")
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

}