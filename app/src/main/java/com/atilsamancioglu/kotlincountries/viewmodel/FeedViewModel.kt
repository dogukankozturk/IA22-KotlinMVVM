package com.atilsamancioglu.kotlincountries.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atilsamancioglu.kotlincountries.model.Country
import com.atilsamancioglu.kotlincountries.service.CountryAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class FeedViewModel : ViewModel() {
    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()
    private val countryAPIService = CountryAPIService()
    private val disposable = CompositeDisposable()

    fun refreshData() {

        getDataFromAPI()
    }

    private fun getDataFromAPI(){
        countryLoading.value=true

        disposable.add(
            countryAPIService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {
                        countries.value = t
                        countryLoading.value=false
                        countryError.value=false
                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value=false
                        countryError.value=true
                        e.printStackTrace()
                    }

                })

        )
    }
}