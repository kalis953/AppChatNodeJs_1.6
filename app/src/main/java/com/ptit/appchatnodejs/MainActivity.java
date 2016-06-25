package com.ptit.appchatnodejs;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;
import com.ptit.model.ChatRoom;
import com.ptit.model.User;
import com.ptit.supporter.mToast;
import com.ptit.utils.ConnectionManager;
import com.ptit.utils.SharedPreferencesOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

//    private static ArrayList<ChatRoom> arrRoom = new ArrayList<>();

//    public static ArrayList<ChatRoom> getArrRoom() {
//        return arrRoom;
//    }

    private static Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://nodejs-chatptit.rhcloud.com/");
//            mSocket = IO.socket("http://10.0.3.2:3000/");
        } catch (URISyntaxException e) {}
    }

    public static Socket getmSocket() {
        return mSocket;
    }

    Toolbar toolbar;
    EditText txtPassword;
    AutoCompleteTextView txtUserName;
    TextView txtRegister;
    Button btnSignUp;

    EditText txtUserRegister, txtPasswordRegister, txtEmailRegister, txtPhoneRegister;

    Button btnRegister, btnCancel;

    private Dialog registerDialog;

    // user đăng nhập thành công
    private static User userLogin = new User();

    public static User getUserLogin() {
        return userLogin;
    }

    // biến cho biết đã đăng nhập thành công hay không
    private boolean isLogin = true;

    private String sharePreName = "sharePre";

    ConnectionManager checkConnectToInternet = new ConnectionManager(this);

    CheckBox checkboxSavedInfomation;

    SharedPreferences pre;

    ArrayList<User> arrayUserSaveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSocket.connect();

        addControls();
        addEvent();

        // client on server send user
        mSocket.on("result-register", onServerSendUser);
        mSocket.on("result-login", onSeverSendResultLogin);
        mSocket.on("server-send-avatar-profile", onServerSendAvataProfile);

        pre = SharedPreferencesOption.getPreferences(MainActivity.this, getString(R.string.txt_file_share_preference));

//        userLogin = SharedPreferencesOption.getUserCurrent(pre, getString(R.string.txt_current_userLogin));
//        if (userLogin!=null)
//        {
//            Intent intent = new Intent(MainActivity.this, ListChatRoomActivity.class);
//            intent.putExtra(getString(R.string.userlogin), userLogin);
//            startActivity(intent);
//        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        pre = SharedPreferencesOption.getPreferences(MainActivity.this, getString(R.string.txt_file_share_preference));
        arrayUserSaveLogin = SharedPreferencesOption.getListUserPreferences(pre,getString(R.string.txt_user_share_preference));

        userLogin = SharedPreferencesOption.getUserCurrent(pre, getString(R.string.txt_current_userLogin));
        if (userLogin!=null)
        {
            Intent intent = new Intent(MainActivity.this, ListChatRoomActivity.class);
            intent.putExtra(getString(R.string.userlogin), userLogin);
            startActivity(intent);
        }

        ArrayList<String> arrayUserName = new ArrayList<>();

        for (User user : arrayUserSaveLogin)
            arrayUserName.add(user.getName());


        ArrayAdapter<String> adapterUserName = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayUserName);
        txtUserName.setAdapter(adapterUserName);

        txtUserName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (User user : arrayUserSaveLogin)
                    if (user.getName().equalsIgnoreCase(txtUserName.getText().toString())){
                        txtPassword.setText(user.getPassword());
                        checkboxSavedInfomation.setChecked(true);
                    }
            }
        });

    }

    private void addEvent() {
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                if (checkConnectToInternet.isConnectingToInternet()){
                    mSocket.emit("client-send-login", name, password);
                }


            }
        });
    }

    private void showRegisterDialog() {
        registerDialog = new Dialog(MainActivity.this);
        registerDialog.setContentView(R.layout.layout_register);

        registerDialog.setTitle("REGISTER USER NAME");
        registerDialog.setCanceledOnTouchOutside(false);

        txtUserRegister = (EditText) registerDialog.findViewById(R.id.txtUserNameRegister);
        txtPasswordRegister = (EditText) registerDialog.findViewById(R.id.txtPassword);
        txtEmailRegister = (EditText) registerDialog.findViewById(R.id.txtEmailRegister);
        txtPhoneRegister = (EditText) registerDialog.findViewById(R.id.txtPhonenumber);

        btnRegister = (Button) registerDialog.findViewById(R.id.btnRegister);
        btnCancel = (Button) registerDialog.findViewById(R.id.btnCancel);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtUserRegister.getText().toString();
                String password = txtPasswordRegister.getText().toString();
                String email = txtEmailRegister.getText().toString();
                String phone = txtPhoneRegister.getText().toString();

                if (checkConnectToInternet.isConnectingToInternet())
                    mSocket.emit("client-send-information", name, password, email, phone);

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDialog.cancel();
            }
        });

        registerDialog.show();
    }

    // client on username from server
    private Emitter.Listener onServerSendUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    boolean ketQua = true;
                    try {
                        ketQua = (boolean) data.get("noidung");
                        Toast.makeText(MainActivity.this, String.valueOf(ketQua), Toast.LENGTH_LONG).show();
                        if (!ketQua) {
                            Toast.makeText(MainActivity.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(MainActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                            registerDialog.cancel();
                        }
                    } catch (JSONException e) {
                        Log.e("ERROR OnRegister", e.toString());
                    }

                }
            });
        }
    };

    private Emitter.Listener onServerSendAvataProfile = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String image = (String) data.get("noidung");
//                        Toast.makeText(MainActivity.this, String.valueOf(ketQua), Toast.LENGTH_LONG).show();
//                        if (!ketQua) {
//                            Toast.makeText(MainActivity.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
//                        } else {
//
//                            Toast.makeText(MainActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
//                            registerDialog.cancel();
//                        }
                        mToast.toastShort(MainActivity.this, image.toString());

                    } catch (JSONException e) {
                        Log.e("ERROR OnRegister", e.toString());
                    }

                }
            });
        }
    };

    private Emitter.Listener onSeverSendResultLogin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    int ketQua = 0;
                    JSONObject userObject = new JSONObject();
                    try {
                        JSONObject json  = data.getJSONObject("noidung");
                        ketQua = json.getInt("kiemTra");
                        try {
                            userObject = json.getJSONObject("infomation");
                            userLogin.setName(userObject.getString("username"));
                            userLogin.setPassword(userObject.getString("password"));
                            userLogin.setEmail(userObject.getString("email"));
                            userLogin.setPhone(userObject.getString("phone"));
                            userLogin.setAvatar(userObject.getString("image"));

                        }catch (JSONException e){
                            Log.e("ERROR Read json Object", e.toString());
                        }

                        if (ketQua == 1) {
                            mToast.toastShort(MainActivity.this, getString(R.string.txt_login_err_msg));
                        }
                        else if (ketQua == 2)
                        {
                            mToast.toastShort(MainActivity.this, getString(R.string.txt_login_err_msg));
                        }
                        else {

                            mToast.toastShort(MainActivity.this, getString(R.string.txt_login_success_msg));

                            //save status
//                            SharedPreferences pre = getSharedPreferences(getString(R.string.txt_user_share_preference),MODE_PRIVATE);
//                            SharedPreferences.Editor editor = pre.edit();
//                            SharedPreferences pre = SharedPreferencesOption.getPreferences(MainActivity.this, getString(R.string.txt_user_share_preference));
                            SharedPreferences.Editor editor = pre.edit();

                            SharedPreferencesOption.saveUserPreferences(pre, getString(R.string.txt_file_share_preference), userLogin, checkboxSavedInfomation.isChecked());
//                            editor.putBoolean(getString(R.string.txt_is_saved_infomation),checkboxSavedInfomation.isChecked());

                            SharedPreferencesOption.saveUserPreferences(pre, getString(R.string.txt_current_userLogin), userLogin, checkboxSavedInfomation.isChecked());
                            editor.commit();

                            // lang nghe server-send-image

                            Intent intent = new Intent(MainActivity.this, ListChatRoomActivity.class);
                            intent.putExtra(getString(R.string.userlogin), userLogin);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        Log.e("ERROR On Login", e.toString());
                        return;
                    }

                }
            });
        }
    };

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtUserName = (AutoCompleteTextView) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        btnSignUp = (Button) findViewById(R.id.btn_signup);

        checkboxSavedInfomation = (CheckBox) findViewById(R.id.checkboxSignIn);



//        checkboxSavedInfomation.setChecked(pre.getBoolean(getString(R.string.txt_user_share_preference),false));
    }

}
