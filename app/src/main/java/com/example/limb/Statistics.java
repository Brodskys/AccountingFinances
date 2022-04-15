package com.example.limb;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
public class Statistics extends AppCompatActivity {
PieChart pieChart;
private  PieDataSet pieDataSet;
        ArrayList<PieEntry> pieEntries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        pieChart = findViewById(R.id.pieChart);
        pieEntries = new ArrayList<>();
        final String ctg1 = getString(R.string.expense_text);
        final String ctg2 = getString(R.string.income_text);
        final String ctg3 = getString(R.string.transfer_text);
        pieEntries.clear();
        final ArrayList<PieEntry> pieEntries = new ArrayList<>();
        SharedPreferences prefs1 = getSharedPreferences("expense", MODE_PRIVATE);
        String value1 = prefs1.getString("expense", "0");
        SharedPreferences prefs2 = getSharedPreferences("income", MODE_PRIVATE);
        String value2 = prefs2.getString("income", "0");
        SharedPreferences prefs3 = getSharedPreferences("transfer", MODE_PRIVATE);
        String value3 = prefs3.getString("transfer", "0");
        if (!value1.equals("0.00")) {
            pieEntries.add(new PieEntry(Float.parseFloat(value1), ctg1)); }
         if (!value2.equals("0.00")){
            pieEntries.add(new PieEntry(Float.parseFloat(value2), ctg2)); }
         if (!value3.equals("0.00")){
            pieEntries.add(new PieEntry(Float.parseFloat(value3), ctg3)); }
        pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16);
        pieDataSet.setValueTextColor(Color.parseColor("#014d81"));
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(dateText);
        pieChart.setHoleColor(Color.parseColor("#014d81"));
        pieChart.setCenterTextSize(23);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.animateXY(700,700);
        pieChart.invalidate(); }
    public void onClick_Back(View v) {
        onBackPressed();
    }}
