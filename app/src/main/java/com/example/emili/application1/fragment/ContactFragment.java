package com.example.emili.application1.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.emili.application1.R;


public class ContactFragment extends Fragment {
    TextView textView;

    private static final String NUMERO = "numero";
    private static final String TITRE = "titre";

    // TODO: Rename and change types of parameters
    private int numero;
    private String titre;

    //private OnFragmentInteractionListener mListener;
/*
    public ContactFragment() {
        // Required empty public constructor
    }*/


    public static ContactFragment newInstance(int numero, String titre) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putInt(NUMERO, numero);
        args.putString(TITRE, titre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numero = getArguments().getInt(NUMERO);
            titre = getArguments().getString(TITRE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        textView = (TextView) view.findViewById(R.id.textContact);
        textView.setText("Bienvenu Emilin");

        return view;
    }

/*
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = (TextView) view.findViewById(R.id.textContact);
        textView.setText("Bienvenu Emilin");

    }*/
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

/*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

*/
}
