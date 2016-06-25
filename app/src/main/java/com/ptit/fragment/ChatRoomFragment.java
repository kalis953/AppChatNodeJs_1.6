package com.ptit.fragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.ptit.appchatnodejs.ListChatRoomActivity;
import com.ptit.appchatnodejs.MainActivity;
import com.ptit.appchatnodejs.R;
import com.ptit.model.ChatRoom;
import com.ptit.model.User;
import com.ptit.supporter.mToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatRoomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatRoomFragment extends Fragment {

    /// server
    private Socket mSocket;



    private Toolbar chatRoomToolBar;
    private Button btnSendMessage;
    private EditText txtMessage;
    private ListView lvChatRoom;
    private ArrayList<String> arrChatRoom;
    private ArrayAdapter adapter;

    private ChatRoom roomSelected;
    private User userLogin;

    View v;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public ChatRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatRoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatRoomFragment newInstance(String param1, String param2) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_chat_room,container,false);

        userLogin = (User) getArguments().getSerializable(getString(R.string.userlogin));
        roomSelected = (ChatRoom) getArguments().getSerializable(getString(R.string.roomSelected));

        addControls(v);
        addEvents();
        return v;
    }

    private void addEvents() {
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mToast.toastShort(getActivity(),"I'm here");
                sendMessageMethod();
            }
        });
//        btnSendMessage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mToast.toastShort(getActivity(),"Touched");
//                return false;
//            }
//        });
    }


    private void sendMessageMethod() {
//        mToast.toastShort(getActivity(),"I'm here");
        if (!txtMessage.getText().toString().equalsIgnoreCase(""))
        {
            mSocket.emit("client-send-message", userLogin.getRoom(), userLogin.getName(),
                                                txtMessage.getText().toString(),
                                                userLogin.getAvatar());
            txtMessage.setText("");
        }
        else
        {
//            mToast.toastLong(getActivity(),"Vui lòng nhập nội dung ");
        }

    }

    private void addControls(View v) {

        txtMessage = (EditText) v.findViewById(R.id.txtMessage);

        chatRoomToolBar = (Toolbar) v.findViewById(R.id.toolbarChatRoom);
        btnSendMessage = (Button) v.findViewById(R.id.btnSendMessage);
        lvChatRoom = (ListView) v.findViewById(R.id.lvChatRoom);
        arrChatRoom = new ArrayList();
        adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,arrChatRoom);
        lvChatRoom.setAdapter(adapter);

        mSocket = MainActivity.getmSocket();
        mSocket.on("server-send-message",onSeverSendMessage);

//        ((AppCompatActivity)getActivity()).setSupportActionBar(chatRoomToolBar);
//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.setIcon(android.R.drawable.dialog_frame);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Emitter.Listener onSeverSendMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null){

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject arg = (JSONObject) args[0];
                        try {
                            JSONObject obj = arg.getJSONObject("noidung");
                            String user = obj.getString("name");
                            String message = obj.getString("message");
                            arrChatRoom.add(message);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("ERROR OnRegister", e.toString());
                            return;
                        }

                    }
                });
            }
        }
    };



}
