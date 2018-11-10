package cn.edu.nc.music.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.edu.nc.music.R;

public class FragmentPaihang extends android.support.v4.app.Fragment implements View.OnClickListener {
    private LinearLayout custom11;
    private LinearLayout custom12;
    private LinearLayout custom13;
    private LinearLayout custom14;
    private LinearLayout custom15;
    private LinearLayout custom16;
    private LinearLayout custom17;
    private LinearLayout custom18;

    private Context context ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();

        View view = inflater.inflate(R.layout.activity_fragment_paihang, container, false);
        custom11 = view.findViewById(R.id.custom1);
        custom12 = view.findViewById(R.id.custom2);
        custom13 = view.findViewById(R.id.custom3);
        custom14 = view.findViewById(R.id.custom4);
        custom15 = view.findViewById(R.id.custom5);
        custom16 = view.findViewById(R.id.custom6);
        custom17 = view.findViewById(R.id.custom7);
        custom18 = view.findViewById(R.id.custom8);
        /*第一名*/
        custom11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.picture);
                builder.setTitle("您的分数:");
                builder.setMessage("95！历史新高啊~");
                builder.setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /*第二名*/
        custom12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.picture);
                builder.setTitle("您的分数:");
                builder.setMessage("93！还不错啊，继续加油！");
                builder.setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /*第三名*/
        custom13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.picture);
                builder.setTitle("您的分数:");
                builder.setMessage("89！努力奋进");
                builder.setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        /*第四名*/
        custom14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.picture);
                builder.setTitle("您的分数:");
                builder.setMessage("84！加油吧，少年！");
                builder.setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /*第五名*/
        custom15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.picture);
                builder.setTitle("您的分数:");
                builder.setMessage("82！紧追前人！！！");
                builder.setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /*第六名*/
        custom16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.picture);
                builder.setTitle("您的分数:");
                builder.setMessage("79！努力可以改变一切~");
                builder.setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /*第七名*/
        custom17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.picture);
                builder.setTitle("您的分数:");
                builder.setMessage("74！别人唱歌要钱~");
                builder.setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /*第八名*/
        custom18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.picture);
                builder.setTitle("您的分数:");
                builder.setMessage("66！分如其人~");
                builder.setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {

    }

}
