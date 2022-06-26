package com.sal.snowsoft.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sal.snowsoft.api.DataService
import com.sal.snowsoft.model.ContentData
import com.sal.snowsoft.repository.MainRepository
import com.sal.snowsoft.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

public class MainViewModel(context: Context) : ViewModel(){
    var dataService: DataService
    init {
        dataService = DataService(context)
    }

    fun getListData(): Flow<PagingData<ContentData>> {
        return Pager (config = PagingConfig(pageSize = 20, maxSize = 200),
            pagingSourceFactory = {MainRepository(dataService)}).flow.cachedIn(viewModelScope)
    }
    fun getSearchListData(query:String): Flow<PagingData<ContentData>> {
        return Pager (config = PagingConfig(pageSize = 20, maxSize = 200),
            pagingSourceFactory = {SearchRepository(dataService,query)}).flow.cachedIn(viewModelScope)
    }
}