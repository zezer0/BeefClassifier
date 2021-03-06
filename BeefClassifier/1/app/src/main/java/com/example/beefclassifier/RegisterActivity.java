package com.example.beefclassifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText emailText,passwordText,passwordcheckText,nameText;
    private Button RegisterBtn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activitiy);


        toolbar = findViewById(R.id.registertoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.emailedt);
        passwordText = findViewById(R.id.Passwordedt);
        passwordcheckText = findViewById(R.id.passwordcheckedt);
        nameText = findViewById(R.id.nameedt);
        RegisterBtn = findViewById(R.id.registerbutton);

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString().trim();
                String pwd = passwordText.getText().toString().trim();
                String pwdcheck = passwordcheckText.getText().toString().trim();


                if(pwd.equals(pwdcheck)) {
                    final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                    mDialog.setMessage("??????????????????...");
                    mDialog.show();


                    auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //?????? ?????????
                            if (task.isSuccessful()) {
                                mDialog.dismiss();

                                FirebaseUser user = auth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                String name = nameText.getText().toString().trim();

                                //????????? ???????????? ?????????????????? ????????????????????? ??????
                                HashMap<Object,String> hashMap = new HashMap<>();

                                hashMap.put("uid",uid);
                                hashMap.put("email",email);
                                hashMap.put("name",name);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference Usersreference = database.getReference("Users");
                                Usersreference.child(uid).setValue(hashMap);


                                //????????? ?????????????????? ?????? ????????? ????????????.
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(RegisterActivity.this, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();

                            } else {
                                mDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "?????? ???????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                                return;  //?????? ????????? ????????? ????????? ????????????.

                            }

                        }
                    });

                    //???????????? ?????????
                }else{

                    Toast.makeText(RegisterActivity.this, "??????????????? ???????????????. ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
            }

}