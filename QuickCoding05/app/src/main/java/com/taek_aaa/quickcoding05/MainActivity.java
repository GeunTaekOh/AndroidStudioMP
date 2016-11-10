package com.taek_aaa.quickcoding05;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    TextView resultstr;
    TextView resultnum;
    EditText editvalue;
    static LinkedList<String> strList;
    static LinkedList<Integer> intList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        strList = new LinkedList<String>();
        intList = new LinkedList<Integer>();
        resultstr =(TextView)findViewById(R.id.resultstring);
        resultnum = (TextView)findViewById(R.id.resultnumber);
        editvalue = (EditText)findViewById(R.id.editvalue);
    }
    public void clickenterbtn(View v){
        try {
            String gvalue = editvalue.getText().toString();
            int gintvalue = Integer.parseInt(gvalue);
            String s = String.valueOf(gintvalue);
            intList.add(gintvalue);
        }catch(Exception e){
            String gvalue = editvalue.getText().toString();
            strList.add(gvalue);
            Log.d("확인2",gvalue);
        }finally {
            editvalue.setText("");
        }
    }
    public void clickfinishbtn(View v){
        resultstr.setText("입력된 문자열 : ");
        resultnum.setText("입력된 숫자 : ");
        resultstr.append(strList.toString());
        resultnum.append(intList.toString());
    }
}
