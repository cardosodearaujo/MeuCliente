package br.com.newoutsourcing.walletofclients.Views.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.newoutsourcing.walletofclients.App.FunctionsApp;
import br.com.newoutsourcing.walletofclients.Objects.Client;
import br.com.newoutsourcing.walletofclients.R;
import br.com.newoutsourcing.walletofclients.Views.Callbacks.FragmentsCallback;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static br.com.newoutsourcing.walletofclients.Repository.Database.Configurations.SessionDatabase.TB_LEGAL_PERSON;
import static br.com.newoutsourcing.walletofclients.Repository.Database.Configurations.SessionDatabase.TB_PHYSICAL_PERSON;

public class LegalPersonFragment extends Fragment implements FragmentsCallback {
    private Toolbar idToolbar;
    private EditText idEdtClientPJSocialName;
    private EditText idEdtClientPJFantasyName;
    private EditText idEdtClientPJCNPJ;
    private EditText idEdtClientPJIE;
    private EditText idEdtClientPJIM;
    private FragmentsCallback imageCallback;
    private ImageFragment imageFragment;

    public LegalPersonFragment() {
    }

    public static LegalPersonFragment newInstance() {
        return new LegalPersonFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_legal_person, container, false);
        this.onInflate(view);
        this.onConfiguration();
        this.onLoad((Client)getArguments().getSerializable("Client"));
        return view;
    }

    private void onInflate(View view){
        this.idToolbar = this.getActivity().findViewById(R.id.idToolbar);
        this.idEdtClientPJSocialName = view.findViewById(R.id.idEdtClientPJSocialName);
        this.idEdtClientPJFantasyName = view.findViewById(R.id.idEdtClientPJFantasyName);
        this.idEdtClientPJCNPJ = view.findViewById(R.id.idEdtClientPJCNPJ);
        this.idEdtClientPJIE = view.findViewById(R.id.idEdtClientPJIE);
        this.idEdtClientPJIM = view.findViewById(R.id.idEdtClientPJIM);
    }

    private void onConfiguration(){
        this.idToolbar.setSubtitle("Pessoa juridica");
        this.idEdtClientPJCNPJ.addTextChangedListener(new MaskEditTextChangedListener(FunctionsApp.MASCARA_CNPJ, this.idEdtClientPJCNPJ));
        this.onCreateFragment(false);
    }

    private void onCreateFragment(Boolean createClean){
        this.imageFragment = ImageFragment.newInstance();
        this.imageCallback = imageFragment;
        if (createClean){
            FunctionsApp.startFragment(this.imageFragment,R.id.idFrlImg,this.getFragmentManager(),null);
        }else{
            FunctionsApp.startFragment(this.imageFragment,R.id.idFrlImg,this.getFragmentManager(),this.getArguments());
        }
    }

    @Override
    public boolean onValidate(){
        boolean save = true;
        if (this.idEdtClientPJSocialName.getText().toString().trim().isEmpty()){
            this.idEdtClientPJSocialName.setError("Informe a razão social.");
            save = false;
        }else{
            this.idEdtClientPJSocialName.setError(null);
        }

        if (this.idEdtClientPJFantasyName.getText().toString().trim().isEmpty()){
            this.idEdtClientPJFantasyName.setError("Informe o nome fantásia.");
            save = false;
        }else{
            this.idEdtClientPJFantasyName.setError(null);
        }

        if (this.idEdtClientPJCNPJ.getText().toString().trim().isEmpty()){
            this.idEdtClientPJCNPJ.setError("Informe o CNPJ.");
            save = false;
        }else{
            this.idEdtClientPJCNPJ.setError(null);
        }

        if (!this.idEdtClientPJCNPJ.getText().toString().trim().isEmpty()){
            if (FunctionsApp.formatCNPJ(this.idEdtClientPJCNPJ.getText().toString()).length() != 14){
                this.idEdtClientPJCNPJ.setError("O CNPJ deve conter 14 digitos.");
                this.idEdtClientPJCNPJ.requestFocus();
                save = false;
            }else{
                this.idEdtClientPJCNPJ.setError(null);
            }
        }

        if (!this.idEdtClientPJCNPJ.getText().toString().trim().isEmpty()){
            if (FunctionsApp.formatCNPJ(this.idEdtClientPJCNPJ.getText().toString()).length() == 14){
                if (TB_LEGAL_PERSON.CheckCNPJ(this.idEdtClientPJCNPJ.getText().toString()) > 0) {
                    this.idEdtClientPJCNPJ.setError("O CNPJ está em uso em outro cadastro!");
                    this.idEdtClientPJCNPJ.requestFocus();
                    save = false;
                }else{
                    this.idEdtClientPJCNPJ.setError(null);
                }
            }
        }


        if (this.idEdtClientPJIE.getText().toString().trim().isEmpty()){
            this.idEdtClientPJIE.setError("Informe o inscrição estadual.");
            save = false;
        }else{
            this.idEdtClientPJIE.setError(null);
        }

        return save;
    }

    @Override
    public Client onSave(Client client) {
        try{
            if (this.onValidate()) {
                client = this.imageCallback.onSave(client);
                client.getLegalPerson().setSocialName(this.idEdtClientPJSocialName.getText().toString());
                client.getLegalPerson().setFantasyName(this.idEdtClientPJFantasyName.getText().toString());
                client.getLegalPerson().setCNPJ(this.idEdtClientPJCNPJ.getText().toString());
                client.getLegalPerson().setIE(this.idEdtClientPJIE.getText().toString());
                client.getLegalPerson().setIM(this.idEdtClientPJIM.getText().toString());
                client.getLegalPerson().setSuccess(true);
            }else{
                client.getLegalPerson().setSuccess(false);
            }
            return client;
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public void onLoad(Client client){
        if (client != null){
            this.idEdtClientPJSocialName.setText(client.getLegalPerson().getSocialName());
            this.idEdtClientPJFantasyName.setText(client.getLegalPerson().getFantasyName());
            this.idEdtClientPJCNPJ.setText(client.getLegalPerson().getCNPJ());
            this.idEdtClientPJIE.setText(client.getLegalPerson().getIE());
            this.idEdtClientPJIM.setText(client.getLegalPerson().getIM());
        }
    }

    @Override
    public void onClear() {
        this.idEdtClientPJSocialName.setText("");
        this.idEdtClientPJFantasyName.setText("");
        this.idEdtClientPJCNPJ.setText("");
        this.idEdtClientPJIE.setText("");
        this.idEdtClientPJIM.setText("");
        this.onCreateFragment(true);
    }
}
