package com.echofit.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class EchoFitDatabase_Impl extends EchoFitDatabase {
  private volatile UserProfileDao _userProfileDao;

  private volatile DailyLogDao _dailyLogDao;

  private volatile DietWorkoutDao _dietWorkoutDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_profile` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `age` INTEGER NOT NULL, `gender` TEXT NOT NULL, `height` REAL NOT NULL, `weight` REAL NOT NULL, `targetWeight` REAL NOT NULL, `fitnessGoal` TEXT NOT NULL, `budgetLevel` TEXT NOT NULL, `dietType` TEXT NOT NULL, `gymAccess` TEXT NOT NULL, `experienceLevel` TEXT NOT NULL, `preferredRestDay` TEXT NOT NULL, `workoutTime` TEXT NOT NULL, `breakfastTime` TEXT NOT NULL, `lunchTime` TEXT NOT NULL, `snackTime` TEXT NOT NULL, `dinnerTime` TEXT NOT NULL, `injuriesOrLimitations` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `meal_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `foodName` TEXT NOT NULL, `portionSize` TEXT NOT NULL, `calories` INTEGER NOT NULL, `protein` INTEGER NOT NULL, `carbs` INTEGER NOT NULL, `fats` INTEGER NOT NULL, `fiber` INTEGER NOT NULL, `photoUri` TEXT, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `water_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `glasses` INTEGER NOT NULL, `liters` REAL NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `workout_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `routineName` TEXT NOT NULL, `durationMinutes` INTEGER NOT NULL, `caloriesBurned` INTEGER NOT NULL, `completedExercisesJson` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `attendance` (`dateString` TEXT NOT NULL, `isDietDone` INTEGER NOT NULL, `isWorkoutDone` INTEGER NOT NULL, `isWaterDone` INTEGER NOT NULL, `isProteinDone` INTEGER NOT NULL, `isSleepTracked` INTEGER NOT NULL, `isMealPhotosUploaded` INTEGER NOT NULL, `isWeightLogged` INTEGER NOT NULL, `score` INTEGER NOT NULL, `notes` TEXT NOT NULL, PRIMARY KEY(`dateString`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `weight_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `weight` REAL NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `progress_photos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `position` TEXT NOT NULL, `photoUri` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reminders` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `time` TEXT NOT NULL, `isEnabled` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chat_history` (`id` TEXT NOT NULL, `text` TEXT NOT NULL, `isUser` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0f595d7033b32711afb57dae9d89272b')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `user_profile`");
        db.execSQL("DROP TABLE IF EXISTS `meal_logs`");
        db.execSQL("DROP TABLE IF EXISTS `water_logs`");
        db.execSQL("DROP TABLE IF EXISTS `workout_logs`");
        db.execSQL("DROP TABLE IF EXISTS `attendance`");
        db.execSQL("DROP TABLE IF EXISTS `weight_logs`");
        db.execSQL("DROP TABLE IF EXISTS `progress_photos`");
        db.execSQL("DROP TABLE IF EXISTS `reminders`");
        db.execSQL("DROP TABLE IF EXISTS `chat_history`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUserProfile = new HashMap<String, TableInfo.Column>(19);
        _columnsUserProfile.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("age", new TableInfo.Column("age", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("gender", new TableInfo.Column("gender", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("height", new TableInfo.Column("height", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("weight", new TableInfo.Column("weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("targetWeight", new TableInfo.Column("targetWeight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("fitnessGoal", new TableInfo.Column("fitnessGoal", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("budgetLevel", new TableInfo.Column("budgetLevel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("dietType", new TableInfo.Column("dietType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("gymAccess", new TableInfo.Column("gymAccess", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("experienceLevel", new TableInfo.Column("experienceLevel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("preferredRestDay", new TableInfo.Column("preferredRestDay", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("workoutTime", new TableInfo.Column("workoutTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("breakfastTime", new TableInfo.Column("breakfastTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("lunchTime", new TableInfo.Column("lunchTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("snackTime", new TableInfo.Column("snackTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("dinnerTime", new TableInfo.Column("dinnerTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProfile.put("injuriesOrLimitations", new TableInfo.Column("injuriesOrLimitations", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserProfile = new TableInfo("user_profile", _columnsUserProfile, _foreignKeysUserProfile, _indicesUserProfile);
        final TableInfo _existingUserProfile = TableInfo.read(db, "user_profile");
        if (!_infoUserProfile.equals(_existingUserProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "user_profile(com.echofit.app.data.local.entity.UserProfileEntity).\n"
                  + " Expected:\n" + _infoUserProfile + "\n"
                  + " Found:\n" + _existingUserProfile);
        }
        final HashMap<String, TableInfo.Column> _columnsMealLogs = new HashMap<String, TableInfo.Column>(10);
        _columnsMealLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("foodName", new TableInfo.Column("foodName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("portionSize", new TableInfo.Column("portionSize", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("calories", new TableInfo.Column("calories", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("protein", new TableInfo.Column("protein", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("carbs", new TableInfo.Column("carbs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("fats", new TableInfo.Column("fats", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("fiber", new TableInfo.Column("fiber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("photoUri", new TableInfo.Column("photoUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMealLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMealLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMealLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMealLogs = new TableInfo("meal_logs", _columnsMealLogs, _foreignKeysMealLogs, _indicesMealLogs);
        final TableInfo _existingMealLogs = TableInfo.read(db, "meal_logs");
        if (!_infoMealLogs.equals(_existingMealLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "meal_logs(com.echofit.app.data.local.entity.MealLogEntity).\n"
                  + " Expected:\n" + _infoMealLogs + "\n"
                  + " Found:\n" + _existingMealLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsWaterLogs = new HashMap<String, TableInfo.Column>(4);
        _columnsWaterLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWaterLogs.put("glasses", new TableInfo.Column("glasses", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWaterLogs.put("liters", new TableInfo.Column("liters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWaterLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWaterLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWaterLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWaterLogs = new TableInfo("water_logs", _columnsWaterLogs, _foreignKeysWaterLogs, _indicesWaterLogs);
        final TableInfo _existingWaterLogs = TableInfo.read(db, "water_logs");
        if (!_infoWaterLogs.equals(_existingWaterLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "water_logs(com.echofit.app.data.local.entity.WaterLogEntity).\n"
                  + " Expected:\n" + _infoWaterLogs + "\n"
                  + " Found:\n" + _existingWaterLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsWorkoutLogs = new HashMap<String, TableInfo.Column>(6);
        _columnsWorkoutLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutLogs.put("routineName", new TableInfo.Column("routineName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutLogs.put("durationMinutes", new TableInfo.Column("durationMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutLogs.put("caloriesBurned", new TableInfo.Column("caloriesBurned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutLogs.put("completedExercisesJson", new TableInfo.Column("completedExercisesJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWorkoutLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWorkoutLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWorkoutLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWorkoutLogs = new TableInfo("workout_logs", _columnsWorkoutLogs, _foreignKeysWorkoutLogs, _indicesWorkoutLogs);
        final TableInfo _existingWorkoutLogs = TableInfo.read(db, "workout_logs");
        if (!_infoWorkoutLogs.equals(_existingWorkoutLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "workout_logs(com.echofit.app.data.local.entity.WorkoutLogEntity).\n"
                  + " Expected:\n" + _infoWorkoutLogs + "\n"
                  + " Found:\n" + _existingWorkoutLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsAttendance = new HashMap<String, TableInfo.Column>(10);
        _columnsAttendance.put("dateString", new TableInfo.Column("dateString", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("isDietDone", new TableInfo.Column("isDietDone", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("isWorkoutDone", new TableInfo.Column("isWorkoutDone", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("isWaterDone", new TableInfo.Column("isWaterDone", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("isProteinDone", new TableInfo.Column("isProteinDone", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("isSleepTracked", new TableInfo.Column("isSleepTracked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("isMealPhotosUploaded", new TableInfo.Column("isMealPhotosUploaded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("isWeightLogged", new TableInfo.Column("isWeightLogged", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("score", new TableInfo.Column("score", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttendance = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAttendance = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAttendance = new TableInfo("attendance", _columnsAttendance, _foreignKeysAttendance, _indicesAttendance);
        final TableInfo _existingAttendance = TableInfo.read(db, "attendance");
        if (!_infoAttendance.equals(_existingAttendance)) {
          return new RoomOpenHelper.ValidationResult(false, "attendance(com.echofit.app.data.local.entity.AttendanceEntity).\n"
                  + " Expected:\n" + _infoAttendance + "\n"
                  + " Found:\n" + _existingAttendance);
        }
        final HashMap<String, TableInfo.Column> _columnsWeightLogs = new HashMap<String, TableInfo.Column>(3);
        _columnsWeightLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeightLogs.put("weight", new TableInfo.Column("weight", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeightLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWeightLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWeightLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWeightLogs = new TableInfo("weight_logs", _columnsWeightLogs, _foreignKeysWeightLogs, _indicesWeightLogs);
        final TableInfo _existingWeightLogs = TableInfo.read(db, "weight_logs");
        if (!_infoWeightLogs.equals(_existingWeightLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "weight_logs(com.echofit.app.data.local.entity.WeightLogEntity).\n"
                  + " Expected:\n" + _infoWeightLogs + "\n"
                  + " Found:\n" + _existingWeightLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsProgressPhotos = new HashMap<String, TableInfo.Column>(4);
        _columnsProgressPhotos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgressPhotos.put("position", new TableInfo.Column("position", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgressPhotos.put("photoUri", new TableInfo.Column("photoUri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProgressPhotos.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProgressPhotos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProgressPhotos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProgressPhotos = new TableInfo("progress_photos", _columnsProgressPhotos, _foreignKeysProgressPhotos, _indicesProgressPhotos);
        final TableInfo _existingProgressPhotos = TableInfo.read(db, "progress_photos");
        if (!_infoProgressPhotos.equals(_existingProgressPhotos)) {
          return new RoomOpenHelper.ValidationResult(false, "progress_photos(com.echofit.app.data.local.entity.ProgressPhotoEntity).\n"
                  + " Expected:\n" + _infoProgressPhotos + "\n"
                  + " Found:\n" + _existingProgressPhotos);
        }
        final HashMap<String, TableInfo.Column> _columnsReminders = new HashMap<String, TableInfo.Column>(4);
        _columnsReminders.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("time", new TableInfo.Column("time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReminders.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReminders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReminders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReminders = new TableInfo("reminders", _columnsReminders, _foreignKeysReminders, _indicesReminders);
        final TableInfo _existingReminders = TableInfo.read(db, "reminders");
        if (!_infoReminders.equals(_existingReminders)) {
          return new RoomOpenHelper.ValidationResult(false, "reminders(com.echofit.app.data.local.entity.ReminderEntity).\n"
                  + " Expected:\n" + _infoReminders + "\n"
                  + " Found:\n" + _existingReminders);
        }
        final HashMap<String, TableInfo.Column> _columnsChatHistory = new HashMap<String, TableInfo.Column>(4);
        _columnsChatHistory.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatHistory.put("text", new TableInfo.Column("text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatHistory.put("isUser", new TableInfo.Column("isUser", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChatHistory.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChatHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChatHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChatHistory = new TableInfo("chat_history", _columnsChatHistory, _foreignKeysChatHistory, _indicesChatHistory);
        final TableInfo _existingChatHistory = TableInfo.read(db, "chat_history");
        if (!_infoChatHistory.equals(_existingChatHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "chat_history(com.echofit.app.data.local.entity.ChatMessageEntity).\n"
                  + " Expected:\n" + _infoChatHistory + "\n"
                  + " Found:\n" + _existingChatHistory);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "0f595d7033b32711afb57dae9d89272b", "0e25682077e71d0e4ee3826560f4e6a5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "user_profile","meal_logs","water_logs","workout_logs","attendance","weight_logs","progress_photos","reminders","chat_history");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `user_profile`");
      _db.execSQL("DELETE FROM `meal_logs`");
      _db.execSQL("DELETE FROM `water_logs`");
      _db.execSQL("DELETE FROM `workout_logs`");
      _db.execSQL("DELETE FROM `attendance`");
      _db.execSQL("DELETE FROM `weight_logs`");
      _db.execSQL("DELETE FROM `progress_photos`");
      _db.execSQL("DELETE FROM `reminders`");
      _db.execSQL("DELETE FROM `chat_history`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserProfileDao.class, UserProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DailyLogDao.class, DailyLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DietWorkoutDao.class, DietWorkoutDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserProfileDao userProfileDao() {
    if (_userProfileDao != null) {
      return _userProfileDao;
    } else {
      synchronized(this) {
        if(_userProfileDao == null) {
          _userProfileDao = new UserProfileDao_Impl(this);
        }
        return _userProfileDao;
      }
    }
  }

  @Override
  public DailyLogDao dailyLogDao() {
    if (_dailyLogDao != null) {
      return _dailyLogDao;
    } else {
      synchronized(this) {
        if(_dailyLogDao == null) {
          _dailyLogDao = new DailyLogDao_Impl(this);
        }
        return _dailyLogDao;
      }
    }
  }

  @Override
  public DietWorkoutDao dietWorkoutDao() {
    if (_dietWorkoutDao != null) {
      return _dietWorkoutDao;
    } else {
      synchronized(this) {
        if(_dietWorkoutDao == null) {
          _dietWorkoutDao = new DietWorkoutDao_Impl(this);
        }
        return _dietWorkoutDao;
      }
    }
  }
}
