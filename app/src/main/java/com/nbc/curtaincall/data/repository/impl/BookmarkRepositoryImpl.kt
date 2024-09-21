package com.nbc.curtaincall.data.repository.impl

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.nbc.curtaincall.domain.repository.BookmarkRepository
import com.nbc.curtaincall.presentation.Key.Companion.DB_BOOKMARKS
import com.nbc.curtaincall.presentation.Key.Companion.DB_USERS
import com.nbc.curtaincall.presentation.model.ShowItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BookmarkRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    BookmarkRepository {
    private val uid by lazy { Firebase.auth.currentUser?.uid ?: "" }
    override fun getBookmarks(): Flow<List<ShowItem.DetailShowItem>> =
        callbackFlow {
            val showRef = firestore.collection(DB_USERS)
                .document(uid)
                .collection(DB_BOOKMARKS)
            val listenerRegistration = showRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val bookmarks = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(ShowItem.DetailShowItem::class.java)
                } ?: emptyList()
                trySend(bookmarks)
            }
            awaitClose { listenerRegistration.remove() }
        }

    override suspend fun addBookmark(showItem: ShowItem.DetailShowItem) {
        val showRef =
            firestore.collection(DB_USERS).document(uid).collection(DB_BOOKMARKS)
                .document(showItem.showId.toString())
        showRef.get().addOnSuccessListener {
            if (!it.exists()) {
                val show = hashMapOf(
                    "showId" to showItem.showId,
                    "title" to showItem.title,
                    "age" to showItem.age,
                    "place" to showItem.placeName,
                    "genre" to showItem.genre,
                    "showState" to showItem.showState,
                    "castSub" to showItem.cast,
                    "posterPath" to showItem.posterPath,
                    "area" to showItem.area,
                    "facilityId" to showItem.facilityId,
                    "reserveInfo" to showItem.relateList,
                    "addDate" to Timestamp.now(),
                    "runTime" to showItem.runTime,
                    "price" to showItem.price,
                    "periodFrom" to showItem.periodFrom,
                    "periodTo" to showItem.periodTo,
                    "time" to showItem.time,
                    "productCase" to showItem.productCast,
                    "styUrl" to showItem.styUrl,
                )
                showRef.set(show)
            }
        }
    }

    override suspend fun removeBookmark(showId: String) {
        firestore.collection(DB_USERS)
            .document(uid)
            .collection(DB_BOOKMARKS)
            .document(showId)
            .delete()
    }

    override suspend fun checkBookmark(showId: String): Boolean {
        val showRef = firestore.collection(DB_USERS)
            .document(uid)
            .collection(DB_BOOKMARKS)
            .document(showId)
        return suspendCoroutine { continuation ->
            showRef.get().addOnSuccessListener {
                continuation.resume(it.exists())
            }.addOnFailureListener { continuation.resume(false) }
        }
    }
}
