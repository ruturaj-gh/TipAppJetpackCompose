package com.example.myapplication.Utils

fun CalculateTotalTip(totalBillState: Double, tipPercentage: Int): Double {
    return if (totalBillState > 1 && totalBillState.toString().isNotEmpty()) (totalBillState * tipPercentage /100) else 0.0
}
fun calculateTotalPerPerson(
    totalBill: Double,
    splitBy: Int,
    tipPercentage: Int
): Double {
    val bill= CalculateTotalTip(totalBillState = totalBill, tipPercentage = tipPercentage) +totalBill
    return (bill/splitBy)
}