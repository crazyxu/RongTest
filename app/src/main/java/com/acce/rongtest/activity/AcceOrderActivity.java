package com.acce.rongtest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acce.rongtest.R;

public class AcceOrderActivity extends ActionBarActivity {
    private Button btnOrderSend;
    private EditText etOrderNo;
    private EditText etOrderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acce_order);
        btnOrderSend=(Button)super.findViewById(R.id.btn_acce_order_send);
        etOrderNo=(EditText)super.findViewById(R.id.et_acce_order_no);
        etOrderName=(EditText)super.findViewById(R.id.et_acce_order_name);
        btnOrderSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AcceOrderActivity.this,"订单已生成",Toast.LENGTH_SHORT).show();
                Intent it=new Intent();
                it.putExtra("orderNo", etOrderNo.getText().toString());
                it.putExtra("orderName", etOrderName.getText().toString());
                setResult(Activity.RESULT_OK,it);
                AcceOrderActivity.this.finish();
            }
        });
    }

}
