# ProGuard rules for EchoFit

# Keep Room database schemas and queries
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>(...);
}

# Keep Firebase models
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName <fields>;
    @com.google.firebase.firestore.PropertyName <methods>;
}
