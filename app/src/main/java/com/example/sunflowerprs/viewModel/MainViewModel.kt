package com.example.sunflowerprs.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunflowerprs.model.PullReqModel
import com.example.sunflowerprs.model.Resource
import com.example.sunflowerprs.repository.MainRepository
import com.example.sunflowerprs.repository.MainRepositoryImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {

    private var currentPage = 1

    private val currentTotalList = arrayListOf<PullReqModel>()

    private val repository: MainRepository by lazy { MainRepositoryImp() }
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _pullsRequests = MutableLiveData<ArrayList<PullReqModel>>()
    val pullRequests: LiveData<ArrayList<PullReqModel>>
        get() = _pullsRequests

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception>
        get() = _error

    fun getClosedPullRequests(page: Int) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getClosedPullRequests(page)
            when (response) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    _loading.postValue(false)
                    response.data?.let { currentTotalList.addAll(it) }
                    _pullsRequests.postValue(response.data)
                }
                is Resource.Error -> {
                    _loading.postValue(false)
                    _error.postValue(response.exception)
                }
            }
        }
    }

    fun updatePullRequestList() {
        _pullsRequests.value = currentTotalList
    }

    fun updateCurrentPage(currentPage: Int) = run { this.currentPage = currentPage }
    fun getCurrentPage(): Int = currentPage

    fun getTotalList() = currentTotalList
    fun clearList() {
        currentTotalList.clear()
    }
}