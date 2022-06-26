package com.sal.snowsoft.module

import com.sal.snowsoft.api.DataService
import com.sal.snowsoft.repository.MainRepository
import com.sal.snowsoft.repository.SearchRepository
import com.sal.snowsoft.viewmodel.MainViewModel
import com.sal.snowsoft.viewmodel.MainViewModelFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { DataService(get()) }
    single { MainRepository(get()) }
    single { SearchRepository(get(), get()) }
    single { MainViewModelFactory(get()) }
    // MyViewModel ViewModel
    viewModel { MainViewModel(get()) }
}