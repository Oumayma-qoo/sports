package com.example.events_live.presentation.home


import android.util.Log
import androidx.lifecycle.*
import com.example.events_live.common.utils.DataState
import com.example.events_live.data.retrofit.NoConnectionException
import com.example.events_live.data.retrofit.NoInternetException
import com.example.events_live.domain.Repository
import com.example.events_live.presentation.event.EventScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    LifecycleObserver {

    val TAG: String = "HomeViewModel"

    private val state = MutableStateFlow<HomeScreenState>(HomeScreenState.Init)
    val mState: StateFlow<HomeScreenState> get() = state


    private fun setLoading() {

        state.value = HomeScreenState.IsLoading(true)
    }

    private fun hideLoading() {

        state.value = HomeScreenState.IsLoading(false)
    }



    fun getMatches(locale : String, pageNumber : String) {
        viewModelScope.launch {
            try {
                repository.getHomeMatches(locale, pageNumber).onStart {
                    Log.d(TAG, " Called on start")
                    setLoading()

                }
                    .collect{
                        hideLoading()
                        Log.d(TAG, " Called collect")
                        when (it) {
                            is DataState.GenericError -> {
                                Log.d(TAG, " Called Generic error")
                                state.value = HomeScreenState.StatusFailed(it.error!!.message.toString())
                            }

                            is DataState.Success -> {
                                Log.d(TAG, "Enter SUCCESS")
                                state.value = it.value.let { it1 ->
                                    HomeScreenState.Response(it1)
                                }
                            }
                        }
                    }
            }
            catch (e : Exception){
                when (e) {
                    is NoInternetException->{
                        state.value = HomeScreenState.NoInternetException(e.message)
                    }
                    is NoConnectionException -> {
                        state.value = HomeScreenState.NoInternetException(e.message)
                    }
                    else -> {
                        state.value =
                            HomeScreenState.GeneralException(e.message ?: "Exception Occurred")
                    }
                }
            }
        }
    }

}