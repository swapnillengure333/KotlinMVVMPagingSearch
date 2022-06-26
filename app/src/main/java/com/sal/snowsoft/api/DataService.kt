package com.sal.snowsoft.api

import android.content.Context
import com.google.gson.Gson
import com.sal.snowsoft.model.ContentData
import com.sal.snowsoft.model.ContentItems
import com.sal.snowsoft.model.Page
import com.sal.snowsoft.model.PageModel

class DataService(val context: Context) {
    private var jsonPages: ArrayList<String> = ArrayList()
    //load json pages
    init {
        jsonPages.add("page1.json")
        jsonPages.add("page2.json")
        jsonPages.add("page3.json")
    }
    //load data page
    fun loadJsonFromAssets(page: Int): PageModel {
        val inputStream = context.assets.open(jsonPages[page - 1])
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, Charsets.UTF_8)
        val gson = Gson()
        return gson.fromJson(json, PageModel::class.java)
    }
    //load data according with query
    fun loadJsonFromAssetsWithSearch(page: Int, query: String): PageModel {
        val pages: ArrayList<ContentData> = ArrayList()
        var count = 1
        for (data in jsonPages) {
            pages.addAll(loadJsonFromAssets(count).page.content_items.content)
            count++
        }
        return searchInPages(page, query, pages)
    }
    //search query in pages
    private fun searchInPages(page: Int, query: String, pages: ArrayList<ContentData>): PageModel {
        val contentList: List<ContentData> = pages.filter  { it.name.lowercase().contains(query.lowercase()) }
        val totalData = contentList.size
        val startIndex = (page*20 - 20)
        val endCount = page*20
        val endIndex = if(endCount > totalData)  totalData - 1 else (endCount - 1)
        val dataCount = endIndex-startIndex+1
        val subList: List<ContentData> =
            if(dataCount > 0)  contentList.subList(startIndex, endIndex+1) else ArrayList()
        var page = PageModel(Page(ContentItems(subList),page.toString(),dataCount.toString(),"Search",totalData.toString() ))
        return page
    }
}