package com.example.hp.activitytest.activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.example.hp.activitytest.R;
import com.example.hp.activitytest.util.myApplication;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ManagerMapActivity extends AppCompatActivity {
    //private PieChart pieChart2;
    private PieChart pieChart1;
    private BarChart barChart;
    private XAxis xAxis;
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication.myActivity.add(this);
        setContentView(R.layout.activity_manager_map);
        mContext = this.getBaseContext();


        barChart= (BarChart) findViewById(R.id.barChart);
        //1、基本设置
        xAxis=barChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放
        //2、y轴和比例尺

        barChart.setDescription("打卡情况");// 数据描述
        barChart.getAxisLeft().setEnabled(true);
        barChart.getAxisRight().setEnabled(true);

        Legend legend = barChart.getLegend();//隐藏比例尺
        legend.setEnabled(false);


        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//数据位于底部






        //第二个图
        pieChart1 = (PieChart) findViewById(R.id.pie_chart1);
        //1、基本设置
        pieChart1.setDrawCenterText(false);  //饼状图中间文字不显示
        pieChart1.setDescription("");
        pieChart1.setDrawHoleEnabled(false);    //设置实心
        pieChart1.setRotationAngle(90); // 初始旋转角度

        new dailyreport1(pieChart1,barChart,xAxis).execute();

    }
    @Override
    protected void onResume() {
        super.onResume();
        myApplication.currentActivity = this;
    }
}


class dailyreport1 extends dailyReport {
    private PieChart pieChart2;
    private PieChart pieChart1;
    private BarChart barChart;
    private XAxis xAxis;


    @Override
    protected List<Integer> doInBackground(Void... voids) {
        return super.doInBackground(voids);
    }

    @Override
    protected void onPostExecute(List<Integer> integers) {
        super.onPostExecute(integers);
        //第一个图
        //3、x轴数据,和显示位置
        ArrayList<String> xValues = new ArrayList<String>();
        xValues.add("已打卡");
        xValues.add("未打卡");
        xValues.add("请假");

        //4、y轴数据
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(integers.get(1), 0));
        yValues.add(new BarEntry(integers.get(2), 1));
        yValues.add(new BarEntry(integers.get(3), 2));

        //5、设置显示的数字为整形
        BarDataSet barDataSet=new BarDataSet(yValues,"");
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                int n = (int) v;
                return n + "";
            }
        });
        //6、设置柱状图的颜色
        barDataSet.setColors(new int[]{Color.rgb(104, 202, 37), Color.rgb(192, 32, 32),
                Color.rgb(34, 129, 197)});
        //7、显示，柱状图的宽度和动画效果
        BarData barData = new BarData(xValues, barDataSet);
        barDataSet.setBarSpacePercent(40f);//值越大，柱状图就越宽度越小；
        barChart.animateY(3000);
        barChart.setData(barData);





        //第二个图
        List<Float> DailySignRate = new ArrayList<>();
        for(int i = 1;i<integers.size();i++)
        {

//            nf.setMaximumFractionDigits(2);//设置保留小数位
//            nf.setRoundingMode(RoundingMode.HALF_UP); //设置舍入模式
            DailySignRate.add(((float)integers.get(i)/integers.get(0))*100);
        }

        //2、添加数据
        ArrayList<String> xValues1 = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

        xValues1.add("打卡比例");
        xValues1.add("未打卡比例");
        xValues1.add("请假比例");

        ArrayList<Entry> yValues1 = new ArrayList<Entry>();
        yValues1.add(new Entry(DailySignRate.get(0), 0));
        yValues1.add(new Entry(DailySignRate.get(1), 1));
        yValues1.add(new Entry(DailySignRate.get(2), 2));

        //3、y轴数据
        PieDataSet pieDataSet1 = new PieDataSet(yValues1, ""/*显示在比例图上*/);
        pieDataSet1.setSliceSpace(0f); //设置个饼状图之间的距离
        //4、设置颜色
        ArrayList<Integer> colors1 = new ArrayList<Integer>();
        colors1.add(Color.rgb(205, 205, 205));
        colors1.add(Color.rgb(114, 188, 223));
        colors1.add(Color.rgb(255, 123, 124));

        pieDataSet1.setColors(colors1);
        //5、 设置数据
        PieData pieData1 = new PieData(xValues1, pieDataSet1);
        DisplayMetrics metrics1 = ManagerMapActivity.mContext.getResources().getDisplayMetrics();

        float px1 = 4 * (metrics1.densityDpi / 160f);
        pieDataSet1.setSelectionShift(px1); // 选中态多出的长度
        pieData1.setValueFormatter(new PercentFormatter());//显示百分比
        //6、去掉比例尺和说明
        Legend legend1 = pieChart1.getLegend();//下标说明，false
        legend1.setEnabled(false);
        pieChart1.setDescription("");

        //7、显示百分比
        pieData1.setValueFormatter(new PercentFormatter());
        //8、显示
        pieChart1.setData(pieData1);



    }
    dailyreport1 (PieChart pieChart1,BarChart barChart,XAxis xAxis){
        this.pieChart1= pieChart1;
        this.barChart = barChart;
        this.xAxis = xAxis;
    }
}

