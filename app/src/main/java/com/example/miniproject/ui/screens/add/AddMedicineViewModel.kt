package com.example.miniproject.ui.screens.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miniproject.data.model.Medicine
import com.example.miniproject.data.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMedicineViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    fun addMedicine(medicine: Medicine) {
        viewModelScope.launch {
            medicineRepository.insertMedicine(medicine)
        }
    }
} 