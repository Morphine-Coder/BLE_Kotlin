package kr.intin.ble_kotlin.data.dao

<<<<<<< HEAD

=======
import androidx.lifecycle.LiveData
>>>>>>> c6b32022a662bda61efeb35214f1a0da5400d90c
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kr.intin.ble_kotlin.data.entity.UseTime

@Dao
interface UseTimeDAO {

    @Insert
    fun insertTime(useTime: UseTime)

    @Query("SELECT used_time FROM UseTime WHERE used_date == (:used_date)")
    fun getTime(used_date : String): List<Int>

    @Query("SELECT * FROM UseTime")
    fun getAll(): List<UseTime>

    @Delete
    fun delete(useTime: UseTime)

}