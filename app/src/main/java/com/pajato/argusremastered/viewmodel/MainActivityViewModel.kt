package com.pajato.argusremastered.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.util.Log
import com.pajato.argusremastered.database.ContentDao
import com.pajato.argusremastered.database.ContentDatabase
import com.pajato.argusremastered.model.Content
import com.pajato.argusremastered.model.DeleteEvent
import com.pajato.argusremastered.model.Event
import com.pajato.argusremastered.model.UpdateEvent
import com.pajato.argusremastered.util.RxBus
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


class MainActivityViewModel(application: Application) : AndroidViewModel(application), Consumer<Event> {
    var contentDao: ContentDao = ContentDatabase.getDatabaseInstance(application).contentDao()
    var content: LiveData<MutableList<Content>> = contentDao.getAllContent()
    private val eventListeners = listOf(RxBus.subscribeToEventType(Event::class.java, this))

    fun increment() {
        val inc = Completable.fromAction {
            contentDao.insertContent(Content("A Title"))
        }
        observeAndSubscribeTo(inc)
    }

    override fun accept(t: Event?) {
        when (t) {
            is DeleteEvent -> {
                val c: Content = t.getData()
                deleteContent(c)
            }
            is UpdateEvent -> {
                val c: Content = t.getData()
                c.date = "A Date"
                updateContent(c)
            }
        }
    }

    fun updateContent(content: Content) {
        val updateCompletable = Completable.fromAction {
            contentDao.updateContent(content)
        }
        observeAndSubscribeTo(updateCompletable)
    }

    fun deleteContent(content: Content) {
        val deleteCompletable = Completable.fromAction {
            contentDao.deleteContent(content)
        }
        observeAndSubscribeTo(deleteCompletable)
    }

    fun addContent(content: Content) {
        val insertCompletable = Completable.fromAction {
            contentDao.insertContent(content)
        }
        observeAndSubscribeTo(insertCompletable)
    }

    private fun observeAndSubscribeTo(completable: Completable) {
        // The subscribeOn call ensures that the completable is done on the io thread, and the
        // observeOn call ensures that the subscribe object is done on the main thread.
        completable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        Log.v(this::class.java.canonicalName, "Content added successfully.")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(this::class.java.canonicalName,
                                "Error adding content: " + e.printStackTrace())
                    }

                    override fun onSubscribe(d: Disposable) {}
                })
    }

    override fun onCleared() {
        super.onCleared()
        for (item in eventListeners) {
            item.dispose()
        }
    }
}