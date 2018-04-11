package com.pajato.argusremastered.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.pajato.argusremastered.database.ContentDao
import com.pajato.argusremastered.database.ContentDatabase
import com.pajato.argusremastered.model.Content
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.disposables.Disposable


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    var contentDao: ContentDao = ContentDatabase.getDatabaseInstance(application).contentDao()
    var content: LiveData<MutableList<Content>> = contentDao.getAllContent()
    var count = MutableLiveData<Int>()

    init {
        count.value = 0
    }

    fun increment() {
        count.value = content.value?.size ?: count.value
        if (count.value != null) {
            count.value = count.value!! + 1
        }

        Completable.fromAction {
            contentDao.insertContent(Content("A Title")) // the subscribeOn call ensures that this is done on the io thread.
        }.observeOn(AndroidSchedulers.mainThread()) // All successive operations to be done on the main thread.
                .subscribeOn(Schedulers.io()) // All prior and successive operations to be done on the io thread.
                // ObserveOn takes precedence, so this is done on the main ui thread.
                .subscribe(object : CompletableObserver {
                    override fun onComplete() { Log.v(this::class.java.canonicalName, "Content added successfully.") }

                    override fun onError(e: Throwable) { Log.e(this::class.java.canonicalName, "Error adding content: " + e.printStackTrace()) }

                    override fun onSubscribe(d: Disposable) {}
                })
    }
}