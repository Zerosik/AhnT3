package kr.hs.dgsw.ahnt3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import kr.hs.dgsw.ahnt3.JsonClass.JsonConverter;
import kr.hs.dgsw.ahnt3.JsonClass.ResponseLoginJson;
import kr.hs.dgsw.ahnt3.Networks.AsyncResponse;
import kr.hs.dgsw.ahnt3.Networks.HttpAsyncTask;
import kr.hs.dgsw.ahnt3.Util.PatternChecker;

public class RegisterActivity extends AppCompatActivity {

    String uri = "http://flow.cafe24app.com/";
    private Spinner spinner;
    private Button ConfirmButton;
    PatternChecker pc = new PatternChecker();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ConfirmButton = findViewById(R.id.RegisterConfirmButton);
        Intent intent = getIntent();
        InitButtonListener();
        InitGenderSpinner();
    }

    private void InitButtonListener(){
        ConfirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dataset data = RegisterDataRequest();
                JSONObject json = new JSONObject();
                try{
                    if(!data.PW.equals(data.REPW)) {
                        Toast.makeText(RegisterActivity.this, "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!pc.EmailChecker(data.Email))
                        return;
                    if(!pc.PassworkChecker(data.PW))
                        return;

                    json.put("email", data.Email);
                    json.put("pw", data.PW);
                    json.put("name", data.Name);
                    json.put("gender", data.Gender);
                    json.put("mobile", data.Mobile);
                    json.put("class_idx", data.sccls);
                    json.put("class_number", data.scnum);
                }catch(Exception e){
                    e.printStackTrace();
                }
                new HttpAsyncTask(new AsyncResponse(){
                    @Override
                    public void processFinish(String output) {
                        JsonConverter converter = new JsonConverter();
                        ResponseLoginJson resultJson = (ResponseLoginJson)converter.ConvertObjectToJson(output, 1);
                        if(resultJson.getStatus() == 400)
                            Toast.makeText(RegisterActivity.this, "입력 양식이 부적절합니다.", Toast.LENGTH_SHORT).show();
                        if(resultJson.getStatus() == 500)
                            Toast.makeText(RegisterActivity.this, "서버 문제로 가입할 수 없습니다.\n 잠시후 다시 시도해주십시오.", Toast.LENGTH_SHORT).show();
                        if(resultJson.getStatus() == 409)
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        if(resultJson.getStatus() == 200){
                            //TODO: 가입 성공
                            //sqlite로 ResponseLoginJson 저장 - 특히 토큰, 아이디(이메일), 비밀번호 저장.
                            Toast.makeText(RegisterActivity.this, "계정생성에 성공하셧습니다.", Toast.LENGTH_LONG).show();
                            finish();
                        }

                    }
                }).execute(uri+"auth/signup", json.toString(), "");
            }
        });
    }

    private void InitGenderSpinner(){
        spinner = (Spinner)findViewById(R.id.Regi_Gender);

        //input array data
        final ArrayList<String> list = new ArrayList<>();
        list.add(new String("남성"));
        list.add(new String("여성"));
        //using ArrayAdapter
        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(spinnerAdapter);

    }
    public dataset RegisterDataRequest(){
        dataset data = new dataset();
        data.Name = ((TextView)findViewById(R.id.Regi_Name)).getText().toString();
        data.PW = ((TextView)findViewById(R.id.Regi_PW)).getText().toString();
        data.REPW = ((TextView)findViewById(R.id.Regi_rePW)).getText().toString();
        data.Email = ((TextView)findViewById(R.id.Regi_Email)).getText().toString();
        data.Gender = ((Spinner)findViewById(R.id.Regi_Gender)).getSelectedItem().toString();
        data.Mobile = ((TextView)findViewById(R.id.Regi_Phone)).getText().toString();
        data.scgrd = Integer.parseInt(((TextView)findViewById(R.id.Regi_ScGrade)).getText().toString());
        data.sccls = Integer.parseInt(((TextView)findViewById(R.id.Regi_ScClass)).getText().toString());
        data.scnum = Integer.parseInt(((TextView)findViewById(R.id.Regi_ScNum)).getText().toString());

        return data;
    }
    private class dataset{
        String Name;
        String PW;
        String REPW;
        String Email;
        String Gender;
        String Mobile;
        int scgrd;
        int sccls;
        int scnum;


    }
}
