package com.example.fame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG="SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.loginButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotosignUpButton).setOnClickListener(onClickListener);

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.loginButton:
                    signUp();
                    break;
                case R.id.gotosignUpButton:
                    Intent intent=new Intent(LoginActivity.this , SignUpActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
//    완료버튼 클릭
    private void signUp() {
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();

        if(email.length()>0&&password.length()>0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                startToast("로그인에 성공하였습니다.");
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                if (task.getException() != null) {
                                    startToast(task.getException().toString());
                                }
                            }
                            // ...
                        }
                    });
        }else{
            startToast("이메일 또는 비밀번호를 입력해주세요");
        }
        //입력창에 입력을 했는지 확인
    }
    private void startToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}
//https://firebase.google.com/docs/auth/android/start?hl=ko#check_current_auth_state
