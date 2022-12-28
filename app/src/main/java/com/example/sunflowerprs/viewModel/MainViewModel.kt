package com.example.sunflowerprs.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sunflowerprs.model.PullReqModel
import com.example.sunflowerprs.model.Resource
import com.example.sunflowerprs.network.ApiInterface
import com.example.sunflowerprs.network.Retrofit
import com.example.sunflowerprs.repository.MainRepository
import com.example.sunflowerprs.repository.MainRepositoryImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {

    private val repository: MainRepository by  lazy { MainRepositoryImp(Retrofit.getClient().create(ApiInterface::class.java)) }
    private val _loading = MutableLiveData<Boolean>(false)
    val loading : LiveData<Boolean>
    get() = _loading

    private val _pullsRequests = MutableLiveData<ArrayList<PullReqModel>>()
    val pullRequests : LiveData<ArrayList<PullReqModel>>
    get() = _pullsRequests

    private val _error = MutableLiveData<String>()
    val error : LiveData<String>
    get() = _error

    fun getClosedPullRequests(page : Int){
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.getClosedPullRequests(page)
            when(response){
                is Resource.Loading -> {}
                is Resource.Success -> {
                    _loading.postValue(false)
                    _pullsRequests.postValue(response.data)
                }
                is Resource.Error -> {
                    _loading.postValue(false)
                    _error.postValue(response.exception?.message)
                }
            }
        }
    }
}