package com.echofit.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.echofit.app.data.local.entity.AttendanceEntity;
import com.echofit.app.data.local.entity.MealLogEntity;
import com.echofit.app.data.local.entity.ProgressPhotoEntity;
import com.echofit.app.data.local.entity.WaterLogEntity;
import com.echofit.app.data.local.entity.WeightLogEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DailyLogDao_Impl implements DailyLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MealLogEntity> __insertionAdapterOfMealLogEntity;

  private final EntityInsertionAdapter<WaterLogEntity> __insertionAdapterOfWaterLogEntity;

  private final EntityInsertionAdapter<WeightLogEntity> __insertionAdapterOfWeightLogEntity;

  private final EntityInsertionAdapter<AttendanceEntity> __insertionAdapterOfAttendanceEntity;

  private final EntityInsertionAdapter<ProgressPhotoEntity> __insertionAdapterOfProgressPhotoEntity;

  private final EntityDeletionOrUpdateAdapter<MealLogEntity> __deletionAdapterOfMealLogEntity;

  private final EntityDeletionOrUpdateAdapter<ProgressPhotoEntity> __deletionAdapterOfProgressPhotoEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearWaterLogs;

  public DailyLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMealLogEntity = new EntityInsertionAdapter<MealLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `meal_logs` (`id`,`foodName`,`portionSize`,`calories`,`protein`,`carbs`,`fats`,`fiber`,`photoUri`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getFoodName());
        statement.bindString(3, entity.getPortionSize());
        statement.bindLong(4, entity.getCalories());
        statement.bindLong(5, entity.getProtein());
        statement.bindLong(6, entity.getCarbs());
        statement.bindLong(7, entity.getFats());
        statement.bindLong(8, entity.getFiber());
        if (entity.getPhotoUri() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPhotoUri());
        }
        statement.bindLong(10, entity.getTimestamp());
      }
    };
    this.__insertionAdapterOfWaterLogEntity = new EntityInsertionAdapter<WaterLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `water_logs` (`id`,`glasses`,`liters`,`timestamp`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WaterLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getGlasses());
        statement.bindDouble(3, entity.getLiters());
        statement.bindLong(4, entity.getTimestamp());
      }
    };
    this.__insertionAdapterOfWeightLogEntity = new EntityInsertionAdapter<WeightLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `weight_logs` (`id`,`weight`,`timestamp`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WeightLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getWeight());
        statement.bindLong(3, entity.getTimestamp());
      }
    };
    this.__insertionAdapterOfAttendanceEntity = new EntityInsertionAdapter<AttendanceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `attendance` (`dateString`,`isDietDone`,`isWorkoutDone`,`isWaterDone`,`isProteinDone`,`isSleepTracked`,`isMealPhotosUploaded`,`isWeightLogged`,`score`,`notes`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AttendanceEntity entity) {
        statement.bindString(1, entity.getDateString());
        final int _tmp = entity.isDietDone() ? 1 : 0;
        statement.bindLong(2, _tmp);
        final int _tmp_1 = entity.isWorkoutDone() ? 1 : 0;
        statement.bindLong(3, _tmp_1);
        final int _tmp_2 = entity.isWaterDone() ? 1 : 0;
        statement.bindLong(4, _tmp_2);
        final int _tmp_3 = entity.isProteinDone() ? 1 : 0;
        statement.bindLong(5, _tmp_3);
        final int _tmp_4 = entity.isSleepTracked() ? 1 : 0;
        statement.bindLong(6, _tmp_4);
        final int _tmp_5 = entity.isMealPhotosUploaded() ? 1 : 0;
        statement.bindLong(7, _tmp_5);
        final int _tmp_6 = entity.isWeightLogged() ? 1 : 0;
        statement.bindLong(8, _tmp_6);
        statement.bindLong(9, entity.getScore());
        statement.bindString(10, entity.getNotes());
      }
    };
    this.__insertionAdapterOfProgressPhotoEntity = new EntityInsertionAdapter<ProgressPhotoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `progress_photos` (`id`,`position`,`photoUri`,`timestamp`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgressPhotoEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPosition());
        statement.bindString(3, entity.getPhotoUri());
        statement.bindLong(4, entity.getTimestamp());
      }
    };
    this.__deletionAdapterOfMealLogEntity = new EntityDeletionOrUpdateAdapter<MealLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `meal_logs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MealLogEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfProgressPhotoEntity = new EntityDeletionOrUpdateAdapter<ProgressPhotoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `progress_photos` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProgressPhotoEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfClearWaterLogs = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM water_logs";
        return _query;
      }
    };
  }

  @Override
  public Object insertMeal(final MealLogEntity meal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMealLogEntity.insert(meal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertWater(final WaterLogEntity water,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWaterLogEntity.insert(water);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertWeight(final WeightLogEntity weight,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWeightLogEntity.insert(weight);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertOrReplaceAttendance(final AttendanceEntity attendance,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAttendanceEntity.insert(attendance);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPhoto(final ProgressPhotoEntity photo,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProgressPhotoEntity.insert(photo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMeal(final MealLogEntity meal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMealLogEntity.handle(meal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePhoto(final ProgressPhotoEntity photo,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfProgressPhotoEntity.handle(photo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearWaterLogs(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearWaterLogs.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearWaterLogs.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MealLogEntity>> getAllMealsFlow() {
    final String _sql = "SELECT * FROM meal_logs ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"meal_logs"}, new Callable<List<MealLogEntity>>() {
      @Override
      @NonNull
      public List<MealLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFoodName = CursorUtil.getColumnIndexOrThrow(_cursor, "foodName");
          final int _cursorIndexOfPortionSize = CursorUtil.getColumnIndexOrThrow(_cursor, "portionSize");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
          final int _cursorIndexOfCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "carbs");
          final int _cursorIndexOfFats = CursorUtil.getColumnIndexOrThrow(_cursor, "fats");
          final int _cursorIndexOfFiber = CursorUtil.getColumnIndexOrThrow(_cursor, "fiber");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<MealLogEntity> _result = new ArrayList<MealLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MealLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFoodName;
            _tmpFoodName = _cursor.getString(_cursorIndexOfFoodName);
            final String _tmpPortionSize;
            _tmpPortionSize = _cursor.getString(_cursorIndexOfPortionSize);
            final int _tmpCalories;
            _tmpCalories = _cursor.getInt(_cursorIndexOfCalories);
            final int _tmpProtein;
            _tmpProtein = _cursor.getInt(_cursorIndexOfProtein);
            final int _tmpCarbs;
            _tmpCarbs = _cursor.getInt(_cursorIndexOfCarbs);
            final int _tmpFats;
            _tmpFats = _cursor.getInt(_cursorIndexOfFats);
            final int _tmpFiber;
            _tmpFiber = _cursor.getInt(_cursorIndexOfFiber);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new MealLogEntity(_tmpId,_tmpFoodName,_tmpPortionSize,_tmpCalories,_tmpProtein,_tmpCarbs,_tmpFats,_tmpFiber,_tmpPhotoUri,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<WaterLogEntity>> getAllWaterFlow() {
    final String _sql = "SELECT * FROM water_logs ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"water_logs"}, new Callable<List<WaterLogEntity>>() {
      @Override
      @NonNull
      public List<WaterLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGlasses = CursorUtil.getColumnIndexOrThrow(_cursor, "glasses");
          final int _cursorIndexOfLiters = CursorUtil.getColumnIndexOrThrow(_cursor, "liters");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<WaterLogEntity> _result = new ArrayList<WaterLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WaterLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpGlasses;
            _tmpGlasses = _cursor.getInt(_cursorIndexOfGlasses);
            final double _tmpLiters;
            _tmpLiters = _cursor.getDouble(_cursorIndexOfLiters);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new WaterLogEntity(_tmpId,_tmpGlasses,_tmpLiters,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<WeightLogEntity>> getAllWeightsFlow() {
    final String _sql = "SELECT * FROM weight_logs ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"weight_logs"}, new Callable<List<WeightLogEntity>>() {
      @Override
      @NonNull
      public List<WeightLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<WeightLogEntity> _result = new ArrayList<WeightLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WeightLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new WeightLogEntity(_tmpId,_tmpWeight,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AttendanceEntity>> getAllAttendanceFlow() {
    final String _sql = "SELECT * FROM attendance ORDER BY dateString DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"attendance"}, new Callable<List<AttendanceEntity>>() {
      @Override
      @NonNull
      public List<AttendanceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "dateString");
          final int _cursorIndexOfIsDietDone = CursorUtil.getColumnIndexOrThrow(_cursor, "isDietDone");
          final int _cursorIndexOfIsWorkoutDone = CursorUtil.getColumnIndexOrThrow(_cursor, "isWorkoutDone");
          final int _cursorIndexOfIsWaterDone = CursorUtil.getColumnIndexOrThrow(_cursor, "isWaterDone");
          final int _cursorIndexOfIsProteinDone = CursorUtil.getColumnIndexOrThrow(_cursor, "isProteinDone");
          final int _cursorIndexOfIsSleepTracked = CursorUtil.getColumnIndexOrThrow(_cursor, "isSleepTracked");
          final int _cursorIndexOfIsMealPhotosUploaded = CursorUtil.getColumnIndexOrThrow(_cursor, "isMealPhotosUploaded");
          final int _cursorIndexOfIsWeightLogged = CursorUtil.getColumnIndexOrThrow(_cursor, "isWeightLogged");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<AttendanceEntity> _result = new ArrayList<AttendanceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceEntity _item;
            final String _tmpDateString;
            _tmpDateString = _cursor.getString(_cursorIndexOfDateString);
            final boolean _tmpIsDietDone;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDietDone);
            _tmpIsDietDone = _tmp != 0;
            final boolean _tmpIsWorkoutDone;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsWorkoutDone);
            _tmpIsWorkoutDone = _tmp_1 != 0;
            final boolean _tmpIsWaterDone;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsWaterDone);
            _tmpIsWaterDone = _tmp_2 != 0;
            final boolean _tmpIsProteinDone;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsProteinDone);
            _tmpIsProteinDone = _tmp_3 != 0;
            final boolean _tmpIsSleepTracked;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsSleepTracked);
            _tmpIsSleepTracked = _tmp_4 != 0;
            final boolean _tmpIsMealPhotosUploaded;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsMealPhotosUploaded);
            _tmpIsMealPhotosUploaded = _tmp_5 != 0;
            final boolean _tmpIsWeightLogged;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsWeightLogged);
            _tmpIsWeightLogged = _tmp_6 != 0;
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new AttendanceEntity(_tmpDateString,_tmpIsDietDone,_tmpIsWorkoutDone,_tmpIsWaterDone,_tmpIsProteinDone,_tmpIsSleepTracked,_tmpIsMealPhotosUploaded,_tmpIsWeightLogged,_tmpScore,_tmpNotes);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAttendanceForDate(final String dateStr,
      final Continuation<? super AttendanceEntity> $completion) {
    final String _sql = "SELECT * FROM attendance WHERE dateString = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, dateStr);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AttendanceEntity>() {
      @Override
      @Nullable
      public AttendanceEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "dateString");
          final int _cursorIndexOfIsDietDone = CursorUtil.getColumnIndexOrThrow(_cursor, "isDietDone");
          final int _cursorIndexOfIsWorkoutDone = CursorUtil.getColumnIndexOrThrow(_cursor, "isWorkoutDone");
          final int _cursorIndexOfIsWaterDone = CursorUtil.getColumnIndexOrThrow(_cursor, "isWaterDone");
          final int _cursorIndexOfIsProteinDone = CursorUtil.getColumnIndexOrThrow(_cursor, "isProteinDone");
          final int _cursorIndexOfIsSleepTracked = CursorUtil.getColumnIndexOrThrow(_cursor, "isSleepTracked");
          final int _cursorIndexOfIsMealPhotosUploaded = CursorUtil.getColumnIndexOrThrow(_cursor, "isMealPhotosUploaded");
          final int _cursorIndexOfIsWeightLogged = CursorUtil.getColumnIndexOrThrow(_cursor, "isWeightLogged");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final AttendanceEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDateString;
            _tmpDateString = _cursor.getString(_cursorIndexOfDateString);
            final boolean _tmpIsDietDone;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDietDone);
            _tmpIsDietDone = _tmp != 0;
            final boolean _tmpIsWorkoutDone;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsWorkoutDone);
            _tmpIsWorkoutDone = _tmp_1 != 0;
            final boolean _tmpIsWaterDone;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsWaterDone);
            _tmpIsWaterDone = _tmp_2 != 0;
            final boolean _tmpIsProteinDone;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsProteinDone);
            _tmpIsProteinDone = _tmp_3 != 0;
            final boolean _tmpIsSleepTracked;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsSleepTracked);
            _tmpIsSleepTracked = _tmp_4 != 0;
            final boolean _tmpIsMealPhotosUploaded;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsMealPhotosUploaded);
            _tmpIsMealPhotosUploaded = _tmp_5 != 0;
            final boolean _tmpIsWeightLogged;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsWeightLogged);
            _tmpIsWeightLogged = _tmp_6 != 0;
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _result = new AttendanceEntity(_tmpDateString,_tmpIsDietDone,_tmpIsWorkoutDone,_tmpIsWaterDone,_tmpIsProteinDone,_tmpIsSleepTracked,_tmpIsMealPhotosUploaded,_tmpIsWeightLogged,_tmpScore,_tmpNotes);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProgressPhotoEntity>> getAllPhotosFlow() {
    final String _sql = "SELECT * FROM progress_photos ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"progress_photos"}, new Callable<List<ProgressPhotoEntity>>() {
      @Override
      @NonNull
      public List<ProgressPhotoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<ProgressPhotoEntity> _result = new ArrayList<ProgressPhotoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProgressPhotoEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPosition;
            _tmpPosition = _cursor.getString(_cursorIndexOfPosition);
            final String _tmpPhotoUri;
            _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new ProgressPhotoEntity(_tmpId,_tmpPosition,_tmpPhotoUri,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
