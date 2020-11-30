package kr.intin.ble_kotlin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["used_time", "used_date"], unique = true)])
data class UseTime(
    @PrimaryKey val index: Int,
    @ColumnInfo(name = "used_time") val usedTime: Int?,
    @ColumnInfo(name = "used_date") val usedDate: String?
)
