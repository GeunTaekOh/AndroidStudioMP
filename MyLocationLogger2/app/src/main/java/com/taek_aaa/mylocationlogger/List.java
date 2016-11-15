package com.taek_aaa.mylocationlogger;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by taek_aaa on 2016. 11. 16..
 */

public class List extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        MainActivity mact = new MainActivity();
        int alsize = mact.alistlatitude.size();
        int studynum = 0;
        int eatnum = 0;
        int cafenum = 0;
        int walkingnum = 0;
        int ary[] = new int[4];
        String arystr[] = new String[4];
        TextView tv1 = (TextView)findViewById(R.id.tv1);
        TextView tv2 = (TextView)findViewById(R.id.tv2);
        TextView tv3 = (TextView)findViewById(R.id.tv3);
        TextView tv4 = (TextView)findViewById(R.id.tv4);


        for(int i=0; i<alsize; i++) {
            if(mact.alistcategory.get(i).toString().equals("공부")){
                studynum++;
            }
            else if (mact.alistcategory.get(i).toString().equals("식사")){
                eatnum++;
            }else if(mact.alistcategory.get(i).toString().equals("카페")){
                cafenum++;
            }else if(mact.alistcategory.get(i).toString().equals("산책")){
                walkingnum++;
            }
        }

        ary[0] = studynum;
        arystr[0] = "공부";
        ary[1] = eatnum;
        arystr[1] = "식사";
        ary[2] = cafenum;
        arystr[2] = "카페";
        ary[3] = walkingnum;
        arystr[3] = "산책";
        int tmp=0;
        String tmpstr = null;

        for(int i=0; i<ary.length-1; i++){
            for(int j=i+1; j<ary.length; j++){
                if(ary[i]>ary[j]){
                    tmp=ary[i];
                    tmpstr=arystr[i];
                    ary[i]=ary[j];
                    arystr[i] = arystr[j];
                    ary[j] = tmp;
                    arystr[j] = tmpstr;
                }
            }
        }
        //3 2 1 0 순서대로 큼
        tv1.append("1st."+arystr[3] + " : "+ary[3]);
        tv2.append("2nd."+arystr[2] + " : "+ary[2]);
        tv3.append("3th."+arystr[1] + " : "+ary[1]);
        tv4.append("4th."+arystr[0] + " : "+ary[0]);







    }
}
