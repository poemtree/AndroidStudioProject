package com.example.student.tcsphone.listview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.student.tcsphone.R;
import com.example.student.tcsphone.listview.ListVO.MyCar;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<MyCar> myCars;

    public ListViewAdapter() {
        myCars = new ArrayList<>();
    }

    @Override
    public int getCount() {
        // 리스트 크기 반환
        return myCars.size();
    }

    @Override
    public Object getItem(int index) {
        return myCars.get(index);
    }

    @Override
    public long getItemId(int id) {
        return 0;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        final int position = index;
        final Context context = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.mycar_list, viewGroup, false);
        }

        ImageView car_img = view.findViewById(R.id.car_img);
        TextView car_num = view.findViewById(R.id.car_num);
        TextView car_memo = view.findViewById(R.id.car_memo);

        MyCar list_item = myCars.get(index);

        car_img.setImageDrawable(list_item.getCar_img());
        car_num.setText(list_item.getCar_num());
        car_memo.setText(list_item.getCar_memo());

  /*      view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
*/
        return view;
    }

    public void addVO(Drawable icon, String car_num, String car_memo) {
        MyCar new_item = new MyCar();

        new_item.setCar_img(icon);
        new_item.setCar_num(car_num);
        new_item.setCar_memo(car_memo);

        myCars.add(new_item);
    }

}
