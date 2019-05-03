package br.com.newoutsourcing.walletofclients.Views.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.newoutsourcing.walletofclients.App.FunctionsApp;
import br.com.newoutsourcing.walletofclients.Objects.Client;
import br.com.newoutsourcing.walletofclients.R;
import br.com.newoutsourcing.walletofclients.Views.Callbacks.FragmentsCallback;

import static br.com.newoutsourcing.walletofclients.Repository.Database.Configurations.SessionDatabase.TB_ADDRESS;

public class AddressFragment extends Fragment implements FragmentsCallback {

    private EditText idEdtClientAddressCEP;
    private EditText idEdtClientAddressStreet;
    private EditText idEdtClientAddressNumber;
    private EditText idEdtClientAddressNeighborhood;
    private EditText idEdtClientAddressCity;
    private Spinner idSpnClientAddressState;
    private EditText idEdtClientAddressCounty;

    public AddressFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        this.onInflate(view);
        this.onConfiguration();
        this.onLoad((Client) getArguments().getSerializable("Client"));
        return view;
    }

    public static AddressFragment newInstance() {
        return new AddressFragment();
    }

    private void onInflate(View view){
        this.idEdtClientAddressCEP = view.findViewById(R.id.idEdtClientAddressCEP);
        this.idEdtClientAddressStreet = view.findViewById(R.id.idEdtClientAddressStreet);
        this.idEdtClientAddressNumber = view.findViewById(R.id.idEdtClientAddressNumber);
        this.idEdtClientAddressNeighborhood = view.findViewById(R.id.idEdtClientAddressNeighborhood);
        this.idEdtClientAddressCity = view.findViewById(R.id.idEdtClientAddressCity);
        this.idSpnClientAddressState = view.findViewById(R.id.idSpnClientAddressState);
        this.idEdtClientAddressCounty = view.findViewById(R.id.idEdtClientAddressCounty);
    }

    private void onConfiguration(){
        this.idEdtClientAddressCEP.addTextChangedListener(new MaskEditTextChangedListener(FunctionsApp.MASCARA_CEP, this.idEdtClientAddressCEP));
    }

    @Override
    public boolean onValidate(){
        boolean save = true;

        if (!this.idEdtClientAddressCEP.getText().toString().isEmpty()){
            if (FunctionsApp.formatCEP(this.idEdtClientAddressCEP.getText().toString()).length()!=8){
                this.idEdtClientAddressCEP.setError("O CEP deve conter ");
                save = false;
            }else{
                this.idEdtClientAddressCEP.setError(null);
            }
        }

        return save;
    }

    @Override
    public Client onSave(Client client) {
        try{
            if (this.onValidate()){
                client.getAddress().setCEP(this.idEdtClientAddressCEP.getText().toString());
                client.getAddress().setStreet(this.idEdtClientAddressStreet.getText().toString());
                if (!this.idEdtClientAddressNumber.getText().toString().isEmpty()) client.getAddress().setNumber(Integer.parseInt(this.idEdtClientAddressNumber.getText().toString()));
                client.getAddress().setNeighborhood(this.idEdtClientAddressNeighborhood.getText().toString());
                client.getAddress().setCity(this.idEdtClientAddressCity.getText().toString());
                if (!this.idSpnClientAddressState.getSelectedItem().toString().isEmpty()) client.getAddress().setState(this.idSpnClientAddressState.getSelectedItem().toString().substring(0,2));
                client.getAddress().setCountry(this.idEdtClientAddressCounty.getText().toString());
                client.getAddress().setSuccess(true);
            }else{
                client.getAddress().setSuccess(false);
            }
            return client;
        }catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void onLoad(Client client){
        if (client != null){
            this.idEdtClientAddressCEP.setText(client.getAddress().getCEP());
            this.idEdtClientAddressStreet.setText(client.getAddress().getStreet());
            this.idEdtClientAddressNumber.setText(String.valueOf(client.getAddress().getNumber()));
            this.idEdtClientAddressNeighborhood.setText(client.getAddress().getNeighborhood());
            this.idEdtClientAddressCity.setText(client.getAddress().getCity());
            this.idSpnClientAddressState.setSelection(FunctionsApp.getState(client.getAddress().getState()));
            this.idEdtClientAddressCounty.setText(client.getAddress().getCountry());
        }
    }

    @Override
    public void onClear() {
        this.idEdtClientAddressCEP.setText("");
        this.idEdtClientAddressStreet.setText("");
        this.idEdtClientAddressNumber.setText("");
        this.idEdtClientAddressNeighborhood.setText("");
        this.idEdtClientAddressCity.setText("");
        this.idSpnClientAddressState.setSelection(0);
        this.idEdtClientAddressCounty.setText("Brasil");
    }
}
