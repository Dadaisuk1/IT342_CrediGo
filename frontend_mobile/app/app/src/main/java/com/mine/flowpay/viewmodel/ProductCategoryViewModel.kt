package com.mine.flowpay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mine.flowpay.data.ProductCategory
import com.mine.flowpay.data.database.AppDatabase
import com.mine.flowpay.data.repository.ProductCategoryRepository
import com.mine.flowpay.app.FlowpayApp
import kotlinx.coroutines.launch

class ProductCategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductCategoryRepository
    private val _allCategories = MutableLiveData<List<ProductCategory>>()
    val allCategories: LiveData<List<ProductCategory>> = _allCategories

    init {
        val database = (application as FlowpayApp).database
        repository = ProductCategoryRepository(database.categoryDao())
        loadCategories()
    }

    private fun loadCategories() = viewModelScope.launch {
        _allCategories.value = repository.getAllCategories()
    }

    fun insertCategory(category: ProductCategory) = viewModelScope.launch {
        repository.insertCategory(category)
        loadCategories() // Reload categories after insert
    }

    fun updateCategory(category: ProductCategory) = viewModelScope.launch {
        repository.updateCategory(category)
        loadCategories() // Reload categories after update
    }

    fun deleteCategory(category: ProductCategory) = viewModelScope.launch {
        repository.deleteCategory(category)
        loadCategories() // Reload categories after delete
    }

    fun getCategoryById(categoryId: Long) = repository.getCategoryById(categoryId)

    fun getCategoryWithProducts(categoryId: Long) = repository.getCategoryWithProducts(categoryId)

    fun getAllCategoriesWithProducts() = repository.getAllCategoriesWithProducts()
} 