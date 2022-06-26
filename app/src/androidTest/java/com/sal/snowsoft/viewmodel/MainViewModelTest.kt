package com.sal.snowsoft.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest :TestCase(){
    lateinit var mainViewModel:MainViewModel

    @Before
    public override fun setUp(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        mainViewModel = MainViewModel(context)
    }

    @Test
    fun test(){
       val data = mainViewModel.getListData();
        assert(data!=null)
    }
}