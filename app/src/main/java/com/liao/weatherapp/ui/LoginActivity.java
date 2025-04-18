package com.liao.weatherapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.liao.weatherapp.R;
import com.liao.weatherapp.User.UserData;
import com.liao.weatherapp.UserDatabaseHelper;


public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private UserDatabaseHelper dbHelper;

    private UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper=new UserDatabaseHelper(this);
        etUsername=findViewById(R.id.loginname);
        etPassword=findViewById(R.id.loginpassword);
        btnLogin=findViewById(R.id.loginbtn);
        btnRegister=findViewById(R.id.regibtn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  username=etUsername.getText().toString().trim();
                String  password=etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "请填写用户名和密码", Toast.LENGTH_SHORT).show();
                } else {
                    if (dbHelper.checkUser(username, password)) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this, WeatherActivity.class);

                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = etUsername.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();

                    if (username.isEmpty() || password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "请填写用户名和密码", Toast.LENGTH_SHORT).show();
                    } else {
                        if (dbHelper.checkUsername(username)) {
                            Toast.makeText(LoginActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                        } else {
                            dbHelper.addUser(username, password);
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }
    @Override
    protected void onDestroy(){
        dbHelper.close();
        super.onDestroy();
    }
}