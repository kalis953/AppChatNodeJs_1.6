package com.ptit.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ptit.appchatnodejs.R;
import com.ptit.model.User;
import com.ptit.supporter.mToast;
import com.ptit.utils.CircleImageBitmap;
import com.ptit.utils.ConvertImage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Toolbar toolbar;
    private ImageView imageProfile;
    private User userLogin = new User();

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);

        userLogin = (User) getArguments().getSerializable(getString(R.string.userlogin));

        addControls(v);
        addEvent();
        return v;
    }

    private void addEvent() {

    }

    private void addControls(View v) {
//        ProfileFragment fragment = getActivity().getSupportFragmentManager().findFragmentById()
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_profile);


        if (toolbar!=null)
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

//        imageProfile = (ImageView) v.findViewById(R.id.imageProfile);
//
//        ConvertImage.String_to_Image(userLogin.getAvatar(), imageProfile);
//        Bitmap bitmap = ((BitmapDrawable)imageProfile.getDrawable()).getBitmap();
//        bitmap = Bitmap.createScaledBitmap(bitmap,80,80,true);
//        bitmap = CircleImageBitmap.getCircleBitmap(bitmap);
//
//        imageProfile.setImageBitmap(bitmap);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(userLogin.getName());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.jangnara);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setHideOnContentScrollEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
