package kr.hs.dgsw.ahnt3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import kr.hs.dgsw.ahnt3.JsonClass.JsonConverter;
import kr.hs.dgsw.ahnt3.JsonClass.ResponseLoginJson;
import kr.hs.dgsw.ahnt3.Networks.AsyncResponse;
import kr.hs.dgsw.ahnt3.Networks.HttpAsyncTask;
import kr.hs.dgsw.ahnt3.Util.DBHelper;
import kr.hs.dgsw.ahnt3.Util.PatternChecker;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    DBHelper dbHelper;
    String uri = "http://flow.cafe24app.com/";
    Button sign_in_button;
    Button register_button;
    ResponseLoginJson resultJson = null;
    PatternChecker pc = new PatternChecker();
    String FIrebaseToken =  FirebaseInstanceId.getInstance().getToken();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        sign_in_button = findViewById(R.id.sign_in_button);
        register_button = findViewById(R.id.register_button);
        InitLoginButtonListener();
        AutoLoginProcess();

    }
    private void AutoLoginProcess(){
        if(dbHelper.CheckTokenExist()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("toast", "로그인되엇습니다.");
            startActivity(intent);
            //finish();
        }
    }
    private void InitLoginButtonListener(){
        sign_in_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = ((EditText)findViewById(R.id.email)).getText().toString();
                        String pw = ((EditText)findViewById(R.id.password)).getText().toString();
                        JSONObject json = new JSONObject();
                        try {
                            if(!pc.EmailChecker(email)) {
                                Toast.makeText(LoginActivity.this, "이메일 양식이 부적절합니다.(정규식)", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(!pc.PassworkChecker(pw)) {
                                Toast.makeText(LoginActivity.this, "비밀번호 양식이 부적절합니다.(정규식)", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            json.put("email", email);
                            json.put("pw", pw);
                            json.put("registration_token", FIrebaseToken);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        new HttpAsyncTask(new AsyncResponse(){
                            @Override
                            public void processFinish(String output) {
                                JsonConverter converter = new JsonConverter();
                                resultJson = (ResponseLoginJson)converter.ConvertObjectToJson(output, 1);
                                if(resultJson.getStatus() == 400)
                                    Toast.makeText(LoginActivity.this, "입력 양식이 부적절합니다.", Toast.LENGTH_SHORT).show();
                                else if(resultJson.getStatus() == 500)
                                    Toast.makeText(LoginActivity.this, "서버 문제로 로그인 할 수 없습니다.\n 잠시 후 다시 시도하십시오", Toast.LENGTH_SHORT).show();
                                else if(resultJson.getStatus() == 401)
                                    Toast.makeText(LoginActivity.this, "계정 또는 비밀번호가 불일치 또는 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                else if(resultJson.getStatus() == 200){
                                    //TODO: 로그인 성공 시.
                                    //Toast.makeText(LoginActivity.this, "로그인에 성공하셧습니다.", Toast.LENGTH_SHORT).show();
                                    //Log.i("response",resultJson.toString());
                                    dbHelper.insertTokenData(resultJson.getToken());
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("toast", "로그인에 성공하셧습니다.");
                                    startActivity(intent);
                                    //finish();

                                } else{
                                    Toast.makeText(LoginActivity.this, "뭔가...뭔가 잘못됨,...", Toast.LENGTH_SHORT).show();
                                    Log.i("info" , resultJson.toString());
                                }

                            }
                        }).execute(uri+"auth/signin", json.toString(), "");
                    }
                }
        );
        register_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}

