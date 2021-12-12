package com.felix.resp

import androidx.room.*
import com.felix.utils.AppProxy

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.resp
 * @ClassName: Mp3Cache
 * @Author: 80341341
 * @CreateDate: 2021/8/4 19:21
 * @Description: Mp3Cache 类作用描述
 */
@Entity
data class Mp3Cache(
    @PrimaryKey
    val id: String,
    val data: String,
    val expireTime: Long = 0
)

@Dao
interface Mp3CacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mp3Bean: Mp3Cache)

    @Query("SELECT * FROM Mp3Cache WHERE id=:id")
    fun query(id: String): Mp3Cache?
}

@Database(entities = arrayOf(Mp3Cache::class), version = 1, exportSchema = false)
public abstract class RespDatabase : RoomDatabase() {
    abstract fun mp3CacheDao(): Mp3CacheDao

    companion object {
        val instance by lazy {
            Room.databaseBuilder(
                AppProxy,
                RespDatabase::class.java,
                "resp_database"
            ).build()
        }
    }
}