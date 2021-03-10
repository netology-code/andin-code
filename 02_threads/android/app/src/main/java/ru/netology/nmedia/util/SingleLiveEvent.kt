package ru.netology.nmedia.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SingleLiveEvent<T> : MutableLiveData<T>() {
    // FIXME: упрощённый вариант, пока не прошли Atomic'и
    private var pending = false
    
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        require (!hasActiveObservers()) {
            error("Multiple observers registered but only one will be notified of changes.")
        }
        
        super.observe(owner) {
            if (pending) {
                pending = false
                observer.onChanged(it)
            }
        }
    }

    override fun setValue(t: T?) {
        pending = true
        super.setValue(t)
    }
}

