package com.gira.rizalfireshop.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigInteger
import java.sql.Timestamp

data class Product(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("description")
    val description: String? = null,
    @field:SerializedName("stock")
    val stock: Int,
    @field:SerializedName("price")
    val price: BigInteger,
    @field:SerializedName("category")
    val category: String,
    @field:SerializedName("image")
    val image: String? = null,
    @field:SerializedName("weight")
    val weight: Int,
    @field:SerializedName("sale")
    val sale: String? = null,
    @field:SerializedName("created_at")
    val created_at: String? = null,
)
