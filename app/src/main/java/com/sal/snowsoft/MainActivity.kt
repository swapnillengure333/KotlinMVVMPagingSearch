package com.sal.snowsoft

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.sal.snowsoft.adapter.RecyclerViewAdapter
import com.sal.snowsoft.databinding.ActivityMainBinding
import com.sal.snowsoft.model.ContentData
import com.sal.snowsoft.viewmodel.MainViewModel
import com.sal.snowsoft.viewmodel.MainViewModelFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var content:ContentData
    lateinit var pagingData: PagingData<ContentData>
    val mainViewModel: MainViewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        chkOrientation(resources.configuration)
        initActions()
    }
    private fun initActions(){
        binding.searchLayout.visibility = View.GONE
        binding.ivBack.setOnClickListener(View.OnClickListener { finish() })
        binding.ivSearch.setOnClickListener(View.OnClickListener {
            binding.actionLayout.visibility = View.GONE
            binding.searchLayout.visibility = View.VISIBLE
            binding.etSearch.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etSearch, 0)
        })
        binding.ivSearchCancel.setOnClickListener(View.OnClickListener {
            binding.actionLayout.visibility = View.VISIBLE
            binding.searchLayout.visibility = View.GONE
            binding.etSearch.setText("")
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            initViewModel()
        })
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().length>2){
                    queryData(s.toString())
                    recyclerViewAdapter.setSearchQuery(s.toString())
                    recyclerViewAdapter.notifyDataSetChanged()
                }else{
                    resetData()
                    recyclerViewAdapter.setSearchQuery("")
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }
        })
    }
    //if query contain less than 3 char
    @OptIn(DelicateCoroutinesApi::class)
    fun resetData(){
        GlobalScope.launch { recyclerViewAdapter.submitData(pagingData) }
    }
    //set recyclerview
    private fun initRecyclerView(spanCount: Int) {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity,spanCount)
            val decoration  = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            recyclerViewAdapter = RecyclerViewAdapter()
            adapter = recyclerViewAdapter

        }
    }
    fun getData(){
        lifecycleScope.launchWhenCreated {
            mainViewModel.getListData().collectLatest {
                pagingData = it
                Logger.getLogger("pagingData").warning(pagingData.toString());
            }
        }
    }
    //subscribe viewmodel data
    private fun initViewModel() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.getListData().collectLatest {
                pagingData = it
                recyclerViewAdapter.submitData(it)
            }
        }
    }
    //Search by query
    private fun queryData(query:String) {
        lifecycleScope.launchWhenCreated {
            mainViewModel.getSearchListData(query).collectLatest {
                recyclerViewAdapter.submitData(it)
            }
        }
    }
    //Check orientation and int recyclerview
    private fun chkOrientation(config: Configuration){
        Logger.getLogger("orientation").warning(config.orientation.toString())
        if(config.orientation==Configuration.ORIENTATION_PORTRAIT){
            initRecyclerView(3)
            initViewModel()
        }else if (config.orientation==Configuration.ORIENTATION_LANDSCAPE){
            initRecyclerView(7)
            initViewModel()
        }
    }
    //Check for orientation change
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        chkOrientation(newConfig)
    }
}