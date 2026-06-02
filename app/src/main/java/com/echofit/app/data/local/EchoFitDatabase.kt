package com.echofit.app.data.local

import android.content.Context
import androidx.room.*
import com.echofit.app.data.local.entity.*
import kotlinx.coroutines.flow.Flow

// DAOs (Data Access Objects) for clean local query executions

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 'primary_profile' LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 'primary_profile' LIMIT 1")
    suspend fun getUserProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("DELETE FROM user_profile")
    suspend fun clearProfile()
}

@Dao
interface DailyLogDao {
    // Meal logs
    @Query("SELECT * FROM meal_logs ORDER BY timestamp DESC")
    fun getAllMealsFlow(): Flow<List<MealLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealLogEntity)

    @Delete
    suspend fun deleteMeal(meal: MealLogEntity)

    // Water logs
    @Query("SELECT * FROM water_logs ORDER BY timestamp DESC")
    fun getAllWaterFlow(): Flow<List<WaterLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWater(water: WaterLogEntity)

    @Query("DELETE FROM water_logs")
    suspend fun clearWaterLogs()

    // Weight logs
    @Query("SELECT * FROM weight_logs ORDER BY timestamp ASC")
    fun getAllWeightsFlow(): Flow<List<WeightLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeight(weight: WeightLogEntity)

    // Attendance
    @Query("SELECT * FROM attendance ORDER BY dateString DESC")
    fun getAllAttendanceFlow(): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE dateString = :dateStr LIMIT 1")
    suspend fun getAttendanceForDate(dateStr: String): AttendanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceAttendance(attendance: AttendanceEntity)

    // Progress photos
    @Query("SELECT * FROM progress_photos ORDER BY timestamp DESC")
    fun getAllPhotosFlow(): Flow<List<ProgressPhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: ProgressPhotoEntity)

    @Delete
    suspend fun deletePhoto(photo: ProgressPhotoEntity)
}

@Dao
interface DietWorkoutDao {
    // Reminders
    @Query("SELECT * FROM reminders")
    fun getRemindersFlow(): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceReminder(reminder: ReminderEntity)

    // Chat History
    @Query("SELECT * FROM chat_history ORDER BY timestamp ASC")
    fun getChatHistoryFlow(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_history")
    suspend fun clearChatHistory()
}

// Room Database Singleton setup

@Database(
    entities = [
        UserProfileEntity::class,
        MealLogEntity::class,
        WaterLogEntity::class,
        WorkoutLogEntity::class,
        AttendanceEntity::class,
        WeightLogEntity::class,
        ProgressPhotoEntity::class,
        ReminderEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class EchoFitDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun dailyLogDao(): DailyLogDao
    abstract fun dietWorkoutDao(): DietWorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: EchoFitDatabase? = null

        fun getDatabase(context: Context): EchoFitDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EchoFitDatabase::class.java,
                    "echofit_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
