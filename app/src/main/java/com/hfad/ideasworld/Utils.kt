package com.hfad.ideasworld


import android.annotation.SuppressLint
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import com.hfad.ideasworld.database.PerDate
import com.hfad.ideasworld.database.PerRange
import java.text.SimpleDateFormat
import java.util.*


fun Map<String, Int>.toListPerRange(k: String): List<PerRange> {
    return this.map { PerRange(k,it.key.toInt(),it.value) }
}

fun Map<String,Int>.toListOfDatePairs():List<PerDate>
{
    return this.map { PerDate(it.key,it.value) }
}

@SuppressLint("SimpleDateFormat")
fun List<PerDate>.toHorizontalBarEntry():List<BarEntry>{
    val sdf= SimpleDateFormat("yyyy-MM-dd")
    return map { BarEntry(sdf.parse(it.date).time.toFloat(),it.count.toFloat()) }
}

fun List<PerRange>.toPieEntry():List<PieEntry>{
    val resList=ArrayList<PieEntry>(7)
    var firstCount=0f
    for (i in this)
        if(i.mmi<3)
            firstCount+=i.count
        else
            break
    resList.add(PieEntry(firstCount, chooseStringByMMI(0)))
    for (i in this)
            resList.add(
                if (i.mmi>3) {
                    PieEntry(i.count.toFloat(), chooseStringByMMI(i.mmi))
                }
                else
                    continue)
    return resList
}

fun chooseStringByMMI(mmi:Int)=when(mmi){
    8->App.context.getString(R.string.eight_mmi)
    7->App.context.getString(R.string.seventh_mmi)
    6->App.context.getString(R.string.six_mmi)
    5->App.context.getString(R.string.five_mmi)
    4->App.context.getString(R.string.four_mmi)
    3->App.context.getString(R.string.three_mmi)
    else->App.context.getString(R.string.unnotice)
}