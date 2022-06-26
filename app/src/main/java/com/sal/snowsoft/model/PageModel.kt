package com.sal.snowsoft.model

import com.google.gson.annotations.SerializedName

data class PageModel(
    val page: Page
)
data class Page(
    @SerializedName(value="content-items")
    val content_items: ContentItems,
    @SerializedName(value="page-num")
    val page_num: String,
    @SerializedName(value="page-size")
    val page_size: String,
    @SerializedName(value="title")
    val title: String,
    @SerializedName(value="total-content-items")
    val total_content_items: String
)
data class ContentItems(
    val content: List<ContentData>
)
data class ContentData(
    @SerializedName(value="name")
    val name: String,
    @SerializedName(value="poster-image")
    val poster_image: String
)