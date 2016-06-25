package com.ptit.appchatnodejs;

import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.ptit.adapter.ChatRoomAdapter;
import com.ptit.fragment.ChatRoomFragment;
import com.ptit.fragment.ProfileFragment;
import com.ptit.model.ChatRoom;
import com.ptit.model.User;
import com.ptit.supporter.mToast;
import com.ptit.utils.ConvertImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListChatRoomActivity extends AppCompatActivity {

    Toolbar toolbar;

    ListView lvListChatRoom;
    ArrayList<ChatRoom> arrRoom;
    ChatRoomAdapter adapterRoom;

    private static Socket mSocket;

    private FloatingActionButton floatingActionButton;

    private User userLogin = new User();

    private static final int RESULT_LOAD_IMAGE = 1;

    ImageView imgRoom;

    Dialog dialogCreateRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat_room);

        getDataFromLoginActivity();
        addControls();
        addEvents();
        socketsServer();

//        mSocket.on("update-list-room", onServerUpdateListRoom);
//        mSocket.on("result-create-room", onServerSendResultCreateRoom);

    }

    private void socketsServer() {
        mSocket.on("result-create-room", onServerSendResultCreateRoom);
        mSocket.on("server-send-list-room", onServerSendListRoom);
        mSocket.emit("client-login-successful");
    }

    private void getDataFromLoginActivity() {
        mSocket =MainActivity.getmSocket();
        mSocket.connect();
        Intent intent = getIntent();
        userLogin = (User) intent.getSerializableExtra(getString(R.string.userlogin));
        String name = userLogin.getName();
    }

    private void addEvents() {

        lvListChatRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatRoom roomSelected = arrRoom.get(position);
                userLogin.setRoom(roomSelected.getTitle());

                mSocket.emit("client-join-room", roomSelected.getTitle());
                ChatRoomClick(roomSelected);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCreateRoom();
            }
        });
    }

    private void ChatRoomClick(ChatRoom roomSelected) {
        ChatRoomFragment chatRoomFragment = new ChatRoomFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.userlogin), userLogin);
        bundle.putSerializable(getString(R.string.roomSelected), roomSelected);
        chatRoomFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_layout_chatroom,chatRoomFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private void addControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialogCreateRoom = new Dialog(ListChatRoomActivity.this);

        arrRoom = new ArrayList<>();
        lvListChatRoom = (ListView) findViewById(R.id.lvListChatRoom);

        adapterRoom = new ChatRoomAdapter(ListChatRoomActivity.this,
                R.layout.layout_custom_chat_room, arrRoom);
        lvListChatRoom.setAdapter(adapterRoom);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void showDialogCreateRoom(){
        dialogCreateRoom = new Dialog(ListChatRoomActivity.this);
        dialogCreateRoom.setContentView(R.layout.dialog_create_chatroom);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogCreateRoom.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText txtRoomName, txtRoomPassword;

        Button btnCreate, btnCancel;

        txtRoomName = (EditText) dialogCreateRoom.findViewById(R.id.txtRoomName);
        txtRoomPassword = (EditText) dialogCreateRoom.findViewById(R.id.txtRoomPassword);
        imgRoom = (ImageView) dialogCreateRoom.findViewById(R.id.imgRoom);
        btnCreate = (Button) dialogCreateRoom.findViewById(R.id.btnCreate);
        btnCancel = (Button) dialogCreateRoom.findViewById(R.id.btnCancel);

        imgRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomName = txtRoomName.getText().toString();
                String roomPassword = txtRoomPassword.getText().toString();
                String roomImage = ConvertImage.Image_to_String(imgRoom);

//                mToast.toastShort(ListChatRoomActivity.this, userLogin.getName());

                mSocket.emit("client-create-room", userLogin.getName(), roomName, roomPassword, roomImage );

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCreateRoom.dismiss();
            }
        });
        dialogCreateRoom.setCanceledOnTouchOutside(false);
        dialogCreateRoom.show();
        dialogCreateRoom.getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RESULT_LOAD_IMAGE:
                if (null != data){
                    Uri uri = data.getData();
                    imgRoom.setImageURI(uri);
                }
        }
    }

//    private Emitter.Listener onServerSendResultCreateRoom = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mToast.toastShort(ListChatRoomActivity.this, "Dang create");
//                    JSONObject data = (JSONObject) args[0];
//                    int ketQua = 0;
//                    try {
//                        ketQua = data.getInt("noidung");
//
//                        if (ketQua == 0) {
//                            mToast.toastShort(ListChatRoomActivity.this, getString(R.string.txt_success_create_room));
////                            dialogCreateRoom.cancel(); // new 1.2
//                        }
//                        else if (ketQua == 1)
//                        {
//                            mToast.toastShort(ListChatRoomActivity.this, getString(R.string.txt_err2_create_room));
//                            dialogCreateRoom.cancel(); // new 1.2
//                        }
//                        else {
//                            mToast.toastShort(ListChatRoomActivity.this, getString(R.string.txt_err1_create_room));
//                        }
//                    } catch (JSONException e) {
//                        Log.e("ERROR On Create Room", e.toString());
//                        return;
//                    }
//
//                }
//            });
//        }
//    };

    private Emitter.Listener onServerSendResultCreateRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
//                            JSONObject s = (JSONObject) data.get("noidung");
                        JSONObject obj = (JSONObject) data.get("noidung");
                        ChatRoom room = new ChatRoom();
                        room.setOwned(obj.getString("owner"));
                        room.setTitle(obj.getString("name"));
                        room.setImage(obj.getString("image"));

                        arrRoom.add(room);
                        adapterRoom.notifyDataSetChanged();

                    } catch (JSONException e) {
                        mToast.toastShort(ListChatRoomActivity.this, "Ban chi duoc tao 1 phong");
                        e.printStackTrace();
                    }
                    dialogCreateRoom.dismiss();
                }
            });
        }
    };

    private Emitter.Listener onServerSendListRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    arrRoom.clear();
                    JSONObject data = (JSONObject) args[0];
                        try {
                            JSONArray array = (JSONArray) data.get("noidung");
                            for (int i = 0; i < array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                ChatRoom room = new ChatRoom();
                                room.setOwned(obj.getString("owner"));
                                room.setTitle(obj.getString("name"));
                                room.setImage(obj.getString("image"));
                                arrRoom.add(room);
                            }
                            adapterRoom.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                }
            });
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_infomation:
                showProfilePage(userLogin);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
////        finish();
//        moveTaskToBack(true);
//         ChatRoomFragment fragment = (ChatRoomFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listRoom);
        Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.container_layout_chatroom);
        if (f instanceof ChatRoomFragment) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
//            super.onBackPressed();

        }
        else {
            moveTaskToBack(true);
        }
    }

    private void showProfilePage(User userLogin) {

        ProfileFragment profileFragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.userlogin), userLogin);
        profileFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_layout_chatroom,profileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
