package ru.netology.nmedia.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity WHERE serverId IS NOT NULL ORDER BY serverId DESC")
    fun observeServer(): Flow<List<PostEntity>>

    @Query("SELECT * FROM PostEntity WHERE serverId IS NULL ORDER BY localId DESC")
    fun observeLocal(): Flow<List<PostEntity>>

    @Query("SELECT * FROM PostEntity ORDER BY serverId DESC")
    suspend fun getAll(): List<PostEntity>

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity): Long

    @Query("SELECT * FROM PostEntity WHERE localId = :id")
    suspend fun getById(id: Long): PostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("DELETE FROM PostEntity WHERE localId = :id")
    suspend fun removeById(id: Long)
}
