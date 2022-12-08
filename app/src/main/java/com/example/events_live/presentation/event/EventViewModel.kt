package com.example.events_live.presentation.event

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.example.events_live.common.utils.DataState
import com.example.events_live.data.retrofit.NoConnectionException
import com.example.events_live.data.retrofit.NoInternetException
import com.example.events_live.domain.Repository
import com.example.events_live.domain.models.event.EventBase
import com.example.events_live.presentation.home.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EventViewModel  @Inject constructor(private val repository: Repository) : ViewModel(),
    LifecycleObserver {

    val TAG: String = "EventViewModel"

    private val state = MutableStateFlow<EventScreenState>(EventScreenState.Init)
    val mState: StateFlow<EventScreenState> get() = state

    var eventsLiveData= MutableLiveData<Resource<EventBase>>()

    private fun setLoading() {

        state.value = EventScreenState.IsLoading(true)
    }

    private fun hideLoading() {

        state.value = EventScreenState.IsLoading(false)
    }


    fun getEvents() {
        viewModelScope.launch {
            try {
                repository.getEvents().onStart {
                    Log.d(TAG, " Called on start")
                    setLoading()
                }
                    .collect{
                        hideLoading()
                        Log.d(TAG, " Called collect")
                        when (it) {
                            is DataState.GenericError -> {
                                Log.d(TAG, " Called Generic error")
                                state.value = EventScreenState.StatusFailed(it.error!!.message.toString())
                            }

                            is DataState.Success -> {
                                Log.d(TAG, "Enter SUCCESS")
                                state.value = it.value.let { it1 ->
                                    EventScreenState.Response(it1)
                                }
                            }
                        }
                    }
            }
            catch (e : Exception){
                when (e) {
                    is NoInternetException -> {
                        state.value = EventScreenState.NoInternetException(e.message)
                    }
                    is NoConnectionException -> {
                        state.value = EventScreenState.NoInternetException(e.message)
                    }
                    else -> {
                        state.value =
                            EventScreenState.GeneralException(e.message ?: "Exception Occurred")
                    }
                }
            }
        }
    }

}