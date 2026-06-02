package com.echofit.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.echofit.app.data.local.entity.UserProfileEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserProfileDao_Impl implements UserProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserProfileEntity> __insertionAdapterOfUserProfileEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearProfile;

  public UserProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserProfileEntity = new EntityInsertionAdapter<UserProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_profile` (`id`,`name`,`age`,`gender`,`height`,`weight`,`targetWeight`,`fitnessGoal`,`budgetLevel`,`dietType`,`gymAccess`,`experienceLevel`,`preferredRestDay`,`workoutTime`,`breakfastTime`,`lunchTime`,`snackTime`,`dinnerTime`,`injuriesOrLimitations`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProfileEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getAge());
        statement.bindString(4, entity.getGender());
        statement.bindDouble(5, entity.getHeight());
        statement.bindDouble(6, entity.getWeight());
        statement.bindDouble(7, entity.getTargetWeight());
        statement.bindString(8, entity.getFitnessGoal());
        statement.bindString(9, entity.getBudgetLevel());
        statement.bindString(10, entity.getDietType());
        statement.bindString(11, entity.getGymAccess());
        statement.bindString(12, entity.getExperienceLevel());
        statement.bindString(13, entity.getPreferredRestDay());
        statement.bindString(14, entity.getWorkoutTime());
        statement.bindString(15, entity.getBreakfastTime());
        statement.bindString(16, entity.getLunchTime());
        statement.bindString(17, entity.getSnackTime());
        statement.bindString(18, entity.getDinnerTime());
        statement.bindString(19, entity.getInjuriesOrLimitations());
      }
    };
    this.__preparedStmtOfClearProfile = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM user_profile";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrUpdateProfile(final UserProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserProfileEntity.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearProfile(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearProfile.acquire();
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
          __preparedStmtOfClearProfile.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserProfileEntity> getUserProfileFlow() {
    final String _sql = "SELECT * FROM user_profile WHERE id = 'primary_profile' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_profile"}, new Callable<UserProfileEntity>() {
      @Override
      @Nullable
      public UserProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfTargetWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "targetWeight");
          final int _cursorIndexOfFitnessGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "fitnessGoal");
          final int _cursorIndexOfBudgetLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "budgetLevel");
          final int _cursorIndexOfDietType = CursorUtil.getColumnIndexOrThrow(_cursor, "dietType");
          final int _cursorIndexOfGymAccess = CursorUtil.getColumnIndexOrThrow(_cursor, "gymAccess");
          final int _cursorIndexOfExperienceLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "experienceLevel");
          final int _cursorIndexOfPreferredRestDay = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredRestDay");
          final int _cursorIndexOfWorkoutTime = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutTime");
          final int _cursorIndexOfBreakfastTime = CursorUtil.getColumnIndexOrThrow(_cursor, "breakfastTime");
          final int _cursorIndexOfLunchTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lunchTime");
          final int _cursorIndexOfSnackTime = CursorUtil.getColumnIndexOrThrow(_cursor, "snackTime");
          final int _cursorIndexOfDinnerTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dinnerTime");
          final int _cursorIndexOfInjuriesOrLimitations = CursorUtil.getColumnIndexOrThrow(_cursor, "injuriesOrLimitations");
          final UserProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final double _tmpHeight;
            _tmpHeight = _cursor.getDouble(_cursorIndexOfHeight);
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final double _tmpTargetWeight;
            _tmpTargetWeight = _cursor.getDouble(_cursorIndexOfTargetWeight);
            final String _tmpFitnessGoal;
            _tmpFitnessGoal = _cursor.getString(_cursorIndexOfFitnessGoal);
            final String _tmpBudgetLevel;
            _tmpBudgetLevel = _cursor.getString(_cursorIndexOfBudgetLevel);
            final String _tmpDietType;
            _tmpDietType = _cursor.getString(_cursorIndexOfDietType);
            final String _tmpGymAccess;
            _tmpGymAccess = _cursor.getString(_cursorIndexOfGymAccess);
            final String _tmpExperienceLevel;
            _tmpExperienceLevel = _cursor.getString(_cursorIndexOfExperienceLevel);
            final String _tmpPreferredRestDay;
            _tmpPreferredRestDay = _cursor.getString(_cursorIndexOfPreferredRestDay);
            final String _tmpWorkoutTime;
            _tmpWorkoutTime = _cursor.getString(_cursorIndexOfWorkoutTime);
            final String _tmpBreakfastTime;
            _tmpBreakfastTime = _cursor.getString(_cursorIndexOfBreakfastTime);
            final String _tmpLunchTime;
            _tmpLunchTime = _cursor.getString(_cursorIndexOfLunchTime);
            final String _tmpSnackTime;
            _tmpSnackTime = _cursor.getString(_cursorIndexOfSnackTime);
            final String _tmpDinnerTime;
            _tmpDinnerTime = _cursor.getString(_cursorIndexOfDinnerTime);
            final String _tmpInjuriesOrLimitations;
            _tmpInjuriesOrLimitations = _cursor.getString(_cursorIndexOfInjuriesOrLimitations);
            _result = new UserProfileEntity(_tmpId,_tmpName,_tmpAge,_tmpGender,_tmpHeight,_tmpWeight,_tmpTargetWeight,_tmpFitnessGoal,_tmpBudgetLevel,_tmpDietType,_tmpGymAccess,_tmpExperienceLevel,_tmpPreferredRestDay,_tmpWorkoutTime,_tmpBreakfastTime,_tmpLunchTime,_tmpSnackTime,_tmpDinnerTime,_tmpInjuriesOrLimitations);
          } else {
            _result = null;
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
  public Object getUserProfile(final Continuation<? super UserProfileEntity> $completion) {
    final String _sql = "SELECT * FROM user_profile WHERE id = 'primary_profile' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserProfileEntity>() {
      @Override
      @Nullable
      public UserProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfTargetWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "targetWeight");
          final int _cursorIndexOfFitnessGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "fitnessGoal");
          final int _cursorIndexOfBudgetLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "budgetLevel");
          final int _cursorIndexOfDietType = CursorUtil.getColumnIndexOrThrow(_cursor, "dietType");
          final int _cursorIndexOfGymAccess = CursorUtil.getColumnIndexOrThrow(_cursor, "gymAccess");
          final int _cursorIndexOfExperienceLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "experienceLevel");
          final int _cursorIndexOfPreferredRestDay = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredRestDay");
          final int _cursorIndexOfWorkoutTime = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutTime");
          final int _cursorIndexOfBreakfastTime = CursorUtil.getColumnIndexOrThrow(_cursor, "breakfastTime");
          final int _cursorIndexOfLunchTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lunchTime");
          final int _cursorIndexOfSnackTime = CursorUtil.getColumnIndexOrThrow(_cursor, "snackTime");
          final int _cursorIndexOfDinnerTime = CursorUtil.getColumnIndexOrThrow(_cursor, "dinnerTime");
          final int _cursorIndexOfInjuriesOrLimitations = CursorUtil.getColumnIndexOrThrow(_cursor, "injuriesOrLimitations");
          final UserProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final double _tmpHeight;
            _tmpHeight = _cursor.getDouble(_cursorIndexOfHeight);
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final double _tmpTargetWeight;
            _tmpTargetWeight = _cursor.getDouble(_cursorIndexOfTargetWeight);
            final String _tmpFitnessGoal;
            _tmpFitnessGoal = _cursor.getString(_cursorIndexOfFitnessGoal);
            final String _tmpBudgetLevel;
            _tmpBudgetLevel = _cursor.getString(_cursorIndexOfBudgetLevel);
            final String _tmpDietType;
            _tmpDietType = _cursor.getString(_cursorIndexOfDietType);
            final String _tmpGymAccess;
            _tmpGymAccess = _cursor.getString(_cursorIndexOfGymAccess);
            final String _tmpExperienceLevel;
            _tmpExperienceLevel = _cursor.getString(_cursorIndexOfExperienceLevel);
            final String _tmpPreferredRestDay;
            _tmpPreferredRestDay = _cursor.getString(_cursorIndexOfPreferredRestDay);
            final String _tmpWorkoutTime;
            _tmpWorkoutTime = _cursor.getString(_cursorIndexOfWorkoutTime);
            final String _tmpBreakfastTime;
            _tmpBreakfastTime = _cursor.getString(_cursorIndexOfBreakfastTime);
            final String _tmpLunchTime;
            _tmpLunchTime = _cursor.getString(_cursorIndexOfLunchTime);
            final String _tmpSnackTime;
            _tmpSnackTime = _cursor.getString(_cursorIndexOfSnackTime);
            final String _tmpDinnerTime;
            _tmpDinnerTime = _cursor.getString(_cursorIndexOfDinnerTime);
            final String _tmpInjuriesOrLimitations;
            _tmpInjuriesOrLimitations = _cursor.getString(_cursorIndexOfInjuriesOrLimitations);
            _result = new UserProfileEntity(_tmpId,_tmpName,_tmpAge,_tmpGender,_tmpHeight,_tmpWeight,_tmpTargetWeight,_tmpFitnessGoal,_tmpBudgetLevel,_tmpDietType,_tmpGymAccess,_tmpExperienceLevel,_tmpPreferredRestDay,_tmpWorkoutTime,_tmpBreakfastTime,_tmpLunchTime,_tmpSnackTime,_tmpDinnerTime,_tmpInjuriesOrLimitations);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
