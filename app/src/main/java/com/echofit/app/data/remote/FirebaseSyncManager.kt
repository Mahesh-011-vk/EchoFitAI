package com.echofit.app.data.remote

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.echofit.app.data.local.entity.AttendanceEntity
import com.echofit.app.data.local.entity.UserProfileEntity
import com.echofit.app.data.local.entity.WaterLogEntity
import com.echofit.app.data.local.entity.WeightLogEntity
import kotlinx.coroutines.tasks.await

object FirebaseSyncManager {

    private val auth: FirebaseAuth get() = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore get() = FirebaseFirestore.getInstance()

    val isUserLoggedIn: Boolean get() = auth.currentUser != null
    val currentUserEmail: String? get() = auth.currentUser?.email
    val currentUserId: String? get() = auth.currentUser?.uid

    /**
     * Sync Local Profile to Firestore.
     */
    suspend fun syncProfileToCloud(profile: UserProfileEntity) {
        val uid = currentUserId ?: return
        try {
            firestore.collection("users")
                .document(uid)
                .collection("profiles")
                .document("primary_profile")
                .set(profile)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Sync Local Daily Attendance records to Firestore.
     */
    suspend fun syncAttendanceToCloud(attendanceList: List<AttendanceEntity>) {
        val uid = currentUserId ?: return
        try {
            val batch = firestore.batch()
            attendanceList.take(30).forEach { attend ->
                val docRef = firestore.collection("users")
                    .document(uid)
                    .collection("attendance")
                    .document(attend.dateString)
                batch.set(docRef, attend)
            }
            batch.commit().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Sync Local Hydration records to Firestore.
     */
    suspend fun syncWaterLogsToCloud(waterLogs: List<WaterLogEntity>) {
        val uid = currentUserId ?: return
        try {
            val batch = firestore.batch()
            waterLogs.take(50).forEach { log ->
                val docRef = firestore.collection("users")
                    .document(uid)
                    .collection("water_logs")
                    .document(log.id.toString())
                batch.set(docRef, log)
            }
            batch.commit().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Sync Local Weight logs to Firestore.
     */
    suspend fun syncWeightLogsToCloud(weightLogs: List<WeightLogEntity>) {
        val uid = currentUserId ?: return
        try {
            val batch = firestore.batch()
            weightLogs.take(50).forEach { log ->
                val docRef = firestore.collection("users")
                    .document(uid)
                    .collection("weight_logs")
                    .document(log.id.toString())
                batch.set(docRef, log)
            }
            batch.commit().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Firebase Sign-out
     */
    fun signOut(context: Context) {
        auth.signOut()
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

    /**
     * Clean user account deletion from Auth and Firestore.
     */
    suspend fun deleteUserAccount(): Boolean {
        val user = auth.currentUser ?: return false
        val uid = user.uid
        return try {
            // Delete Firestore user documents
            firestore.collection("users").document(uid).delete().await()
            // Delete auth profile
            user.delete().await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
