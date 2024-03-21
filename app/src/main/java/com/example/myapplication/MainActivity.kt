package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Utils.CalculateTotalTip
import com.example.myapplication.Utils.calculateTotalPerPerson
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.widgets.RoundIconButton
import kotlin.math.absoluteValue



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Text(text = "Hello Again")
            }
        }
    }
}
@Composable
fun MyApp(content: @Composable ()->Unit){
    var totalBillState by remember { mutableStateOf("") }
    val sliderPositions= remember {
        mutableStateOf(0f)
    }
    val tipPercentage = (sliderPositions.value * 100 ).toInt()
    val tipAmountState= remember {
        mutableDoubleStateOf(0.0)
    }
    val totalPerPersonState= remember {
        mutableDoubleStateOf(0.0)
    }



    var totalPeople = remember { mutableIntStateOf(1) }

    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                TopHeader(totalBillState=totalPerPersonState.value.toString())
                MainContent(totalBillState =totalBillState,tipPercentage=tipPercentage, totalPeople = totalPeople, sliderPositions =sliderPositions, tipAmountState = tipAmountState, totalPerPersonState = totalPerPersonState){
                    bill-> totalBillState=bill
                }
            }



        }
    }

}

@Composable
fun MainContent(
    totalBillState:String,
    sliderPositions: MutableState<Float>,
    tipPercentage: Int,
    totalPeople:MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableDoubleState,
    onTotalBillChange: (String) -> Unit){

    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)

        .padding(2.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(1.dp, color = Color.Black),


        ) {
            Column {




                OutlinedTextField(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    value = totalBillState,
                    enabled = true,
                    onValueChange = {
                        if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                            onTotalBillChange(it)
                            Log.d("TAG", totalBillState)

                        }
                    },
                    label = { Text("Enter Bill") }
                )
                Row(modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start) {
                        Text(text ="Split",
                            modifier = Modifier.align(
                                alignment = Alignment.CenterVertically
                            ))
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(modifier = Modifier
                        .padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End)
                    {
                        RoundIconButton(

                            imageVector = Icons.Default.ArrowBack,
                            onClick = {
                                if(totalBillState!=null)
                                {
                                    Log.d("Icon","Removed")
                                    if(totalPeople.value!=1){
                                        totalPeople.value-=1
                                    }
                                }



                            })
                        Text(
                            text = "${totalPeople.value}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )

                        RoundIconButton(

                            imageVector = Icons.Default.ArrowForward,
                            onClick = {
                                if(totalBillState!=null){

                                    Log.d("Icon2","added")
                                    totalPeople.value+=1
                                }

                            })


                    }

                }
                //tip row
                Row(modifier=Modifier
                    .padding(horizontal = 3.dp,vertical=12.dp)
                    ){
                    Text(text = "Tip",
                        modifier=Modifier.align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(200.dp))
                    Text(text="$ ${tipAmountState.value}",
                        Modifier.align(alignment = Alignment.CenterVertically))
                }
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Text(text = "${tipPercentage}%")
                    Spacer(modifier = Modifier.height(14.dp))

                    Slider(
                        value = sliderPositions.value,
                        enabled = (totalBillState.isEmpty()==false),onValueChange = {


                            newVal->
                            Log.d("RDG",(totalBillState.isEmpty()).toString())
                            if(totalBillState!=null){

                                sliderPositions.value=newVal
                                tipAmountState.value= CalculateTotalTip(totalBillState=totalBillState.toString().toDouble(),tipPercentage=tipPercentage)
                                var valueof: Double =totalBillState.toDouble()
                                totalPerPersonState.value= calculateTotalPerPerson(totalBill = valueof, splitBy = totalPeople.value,tipPercentage=tipPercentage)
                            }

                    }
                        ,
                        modifier = Modifier.padding(start=16.dp,end=16.dp),
                        steps = 100,

                    )
                }


            }
    }
}



@Composable
fun TopHeader(totalBillState:String){
    Surface (modifier= Modifier
        .fillMaxWidth()
        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
        .clip(shape = CircleShape.copy(all = CornerSize(13.dp)))

        .height(150.dp)
    ,
        color = Color(0xFFE9D7F7)
    ){
        var totalPerPerson: Double=134.00
        val total = totalBillState.toDoubleOrNull()
       Column(modifier = Modifier.padding(12.dp),
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center) {
           Text(text = "Total Per Person",
               color = Color.Black,
               fontStyle = FontStyle(value = 45),
               fontSize = 20.sp,
              fontWeight = FontWeight(40),
               style= MaterialTheme.typography.bodyMedium
           )
           if (total != null) {
               Text(
                   text = "$${"%.2f".format(total)}",
                   style = MaterialTheme.typography.bodySmall,
                   color = Color.Black,
                   fontSize = 18.sp,
                   fontWeight = FontWeight.ExtraBold
               )
           }

       }
    }
}







