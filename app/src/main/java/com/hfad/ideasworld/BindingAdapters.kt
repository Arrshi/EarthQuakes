package com.hfad.ideasworld

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.hfad.ideasworld.database.DataBaseEarthQuake

@BindingAdapter("bindImage")
fun ImageView.bindImage(data:DataBaseEarthQuake){
    val tempMagnitude=data.magnitude
    setImageResource(when{
        tempMagnitude<2->R.drawable.earthquake_light
        tempMagnitude in 2.0..5.0->R.drawable.earthquake_week
        tempMagnitude in 5.0..6.0 ->R.drawable.earthquake_danger
        else->R.drawable.earthquake_strong
    })
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setTimeByEarthQuake")
fun TextView.setTimeFromEarthQuake(string: String){
    val tempList=string.split("T")
    val tempTime=tempList.last().dropLastWhile {
        it!='.'
    }.dropLast(1)
    text=" Время : $tempTime  Дата : ${tempList.first()}"
}

@SuppressLint("SetTextI18n")
@BindingAdapter(value = ["magnitude","depth"],requireAll = false)
fun TextView.setEarthQuakeDanger(magnitude:Double,depth:Double){
    val temptext=if (depth<5){
        "Опасно"
        }else {
                when{
                    magnitude<5->"Не существенно"
                    magnitude>5&&magnitude<7->"Опасно"
                    else->{
                        setTextColor(Color.RED)
                        "Катострофично опасно"
                    }
                }
    }
    text="Квалификация угрозы: $temptext"
}

@BindingAdapter("setBarData")
fun HorizontalBarChart.setData(data:List<BarEntry>?){
    data?.let{
        val dataSetBar=BarDataSet(data,resources.getString(R.string.bar_chart_description))
        val tempData=BarData(dataSetBar)
        tempData.barWidth=100000000f
        this.data=tempData
        this.notifyDataSetChanged()
        this.invalidate()
    }
}

@BindingAdapter("setPieData")
fun PieChart.setData(data:List<PieEntry>?){
    data?.let {
        val tempDataSet=PieDataSet(data,"")
        tempDataSet?.colors= listOf(
            ContextCompat.getColor(context,R.color.tooEasy_color),
            ContextCompat.getColor(context,R.color.weak_color),
            ContextCompat.getColor(context,R.color.light_color),
            ContextCompat.getColor(context,R.color.dangerous_color),
            ContextCompat.getColor(context,R.color.strong_color),
            ContextCompat.getColor(context,R.color.critical_color),
            ContextCompat.getColor(context,R.color.finish_him_color)
        )

        tempDataSet.xValuePosition=PieDataSet.ValuePosition.OUTSIDE_SLICE
        tempDataSet.yValuePosition=PieDataSet.ValuePosition.OUTSIDE_SLICE
        tempDataSet.valueLinePart1OffsetPercentage=100f
        tempDataSet.valueLinePart1Length=0.5f
        tempDataSet.valueLinePart2Length=0.1f
        tempDataSet.sliceSpace=3f
        tempDataSet.selectionShift=40f
        this.data= PieData(tempDataSet)
        invalidate()
    }
}

@BindingAdapter("setRefresher")
fun SwipeRefreshLayout.isRefreshing(item:LiveData<Boolean>?){
    this.isRefreshing=item?.value!!
}