package br.com.newoutsourcing.walletofclients.Views.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.Calendar;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.newoutsourcing.walletofclients.Tools.FunctionsTools;
import br.com.newoutsourcing.walletofclients.Objects.Client;
import br.com.newoutsourcing.walletofclients.R;
import br.com.newoutsourcing.walletofclients.Views.Bases.FragmentBase;
import br.com.newoutsourcing.walletofclients.Views.Callbacks.FragmentsCallback;
import butterknife.BindView;

import static br.com.newoutsourcing.walletofclients.Repository.Database.Configurations.SessionDatabase.TB_PHYSICAL_PERSON;

public class PhysicalPersonFragment extends FragmentBase {

    protected @BindView(R.id.idEdtClientPFName) EditText idEdtClientPFName;
    protected @BindView(R.id.idEdtClientPFNickName) EditText idEdtClientPFNickName;
    protected @BindView(R.id.idEdtClientPFCPF) EditText idEdtClientPFCPF;
    protected @BindView(R.id.idEdtClientPFRG) EditText idEdtClientPFRG;
    protected @BindView(R.id.idSpnClientPFSexo) Spinner idSpnClientPFSexo;
    protected @BindView(R.id.idEdtClientPFDate) EditText idEdtClientPFDate;
    protected Toolbar idToolbar;
    private FragmentsCallback imageCallback;
    private ImageFragment imageFragment;
    private String clientId;

    public PhysicalPersonFragment() {
        super(R.layout.fragment_physical_person);
    }

    public static PhysicalPersonFragment newInstance(){
        return new PhysicalPersonFragment();
    }

    @Override
    protected void onConfiguration(){
        this.clientId = null;
        this.idToolbar = this.getActivity().findViewById(R.id.idToolbar);
        this.idToolbar.setSubtitle("Pessoa física");
        this.idEdtClientPFDate.addTextChangedListener(new MaskEditTextChangedListener(FunctionsTools.MASCARA_DATA, this.idEdtClientPFDate));
        this.idEdtClientPFCPF.addTextChangedListener(new MaskEditTextChangedListener(FunctionsTools.MASCARA_CPF, this.idEdtClientPFCPF));
        this.idEdtClientPFDate.setText(FunctionsTools.getCurrentDate());
        this.idEdtClientPFDate.setOnClickListener(this.onClickDate);
        this.onCreateFragment(false);
        this.onLoad((Client)this.getArguments().getSerializable("Client"));
    }

    private void onCreateFragment(Boolean createClean){
        this.imageFragment = ImageFragment.newInstance();
        this.imageCallback = imageFragment;
        if (createClean){
            FunctionsTools.startFragment(this.imageFragment,R.id.idFrlImg,this.getFragmentManager(),null);
        }else{
            FunctionsTools.startFragment(this.imageFragment,R.id.idFrlImg,this.getFragmentManager(),this.getArguments());
        }
    }

    @Override
    public boolean onValidate(){
        Boolean save = true;

        if (this.idEdtClientPFName.getText().toString().trim().isEmpty()){
            this.idEdtClientPFName.setError("Informe o nome.");
            save = false;
        }else{
            this.idEdtClientPFName.setError(null);
        }

        if (this.idEdtClientPFCPF.getText().toString().trim().isEmpty()){
            this.idEdtClientPFCPF.setError("Informe o CPF.");
            this.idEdtClientPFCPF.requestFocus();
            save = false;
        }else{
            this.idEdtClientPFCPF.setError(null);
        }

        if (!this.idEdtClientPFCPF.getText().toString().trim().isEmpty()){
            if ( FunctionsTools.formatCPF(this.idEdtClientPFCPF.getText().toString()).length() != 11){
                this.idEdtClientPFCPF.setError("O CPF deve conter 11 digitos.");
                this.idEdtClientPFCPF.requestFocus();
                save = false;
            }else{
                this.idEdtClientPFCPF.setError(null);
            }
        }

        if (!this.idEdtClientPFCPF.getText().toString().trim().isEmpty()){
            if (FunctionsTools.formatCPF(this.idEdtClientPFCPF.getText().toString()).length() == 11){
                if (TB_PHYSICAL_PERSON.CheckCPF(this.idEdtClientPFCPF.getText().toString(),this.clientId ) > 0) {
                    this.idEdtClientPFCPF.setError("O CPF está em uso em outro cadastro!");
                    this.idEdtClientPFCPF.requestFocus();
                    save = false;
                }else{
                    this.idEdtClientPFCPF.setError(null);
                }
            }
        }

        if (this.idEdtClientPFRG.getText().toString().trim().isEmpty()){
            this.idEdtClientPFRG.setError("Informe o RG.");
            save = false;
        }else{
            this.idEdtClientPFRG.setError(null);
        }

        if (this.idEdtClientPFDate.getText().toString().trim().isEmpty()){
            this.idEdtClientPFDate.setError("Informe a data de nascimento.");
            save = false;
        }else{
            this.idEdtClientPFDate.setError(null);
        }

        return save;
    }

    @Override
    public Client onSave(Client client) {
        try{
            if (this.onValidate()){
                client = this.imageCallback.onSave(client);
                client.getPhysicalPerson().setName(this.idEdtClientPFName.getText().toString());
                client.getPhysicalPerson().setNickname(this.idEdtClientPFNickName.getText().toString());
                client.getPhysicalPerson().setCPF(this.idEdtClientPFCPF.getText().toString());
                client.getPhysicalPerson().setRG(this.idEdtClientPFRG.getText().toString());
                client.getPhysicalPerson().setBirthDate(this.idEdtClientPFDate.getText().toString());
                if (this.idSpnClientPFSexo.getSelectedItemPosition() == 1){
                    client.getPhysicalPerson().setSex("F");
                }else if (this.idSpnClientPFSexo.getSelectedItemPosition() == 2){
                    client.getPhysicalPerson().setSex("M");
                }else{
                    client.getPhysicalPerson().setSex("I");
                }

                client.getPhysicalPerson().setSuccess(true);
            }else{
                client.getPhysicalPerson().setSuccess(false);
            }
            return client;
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public void onLoad(Client client){
        if (client != null){
            this.clientId = String.valueOf(client.getClientId());
            this.idEdtClientPFName.setText(client.getPhysicalPerson().getName());
            this.idEdtClientPFNickName.setText(client.getPhysicalPerson().getNickname());
            this.idEdtClientPFCPF.setText(client.getPhysicalPerson().getCPF());
            this.idEdtClientPFRG.setText(client.getPhysicalPerson().getRG());
            this.idEdtClientPFDate.setText(client.getPhysicalPerson().getBirthDate());
            this.idSpnClientPFSexo.setSelection(FunctionsTools.getSex(client.getPhysicalPerson().getSex()));
        }
    }

    @Override
    public void onClear() {
        this.clientId = null;
        this.idEdtClientPFName.setError(null);
        this.idEdtClientPFNickName.setError(null);
        this.idEdtClientPFCPF.setError(null);
        this.idEdtClientPFRG.setError(null);
        this.idEdtClientPFDate.setError(null);
        this.idEdtClientPFName.setText("");
        this.idEdtClientPFNickName.setText("");
        this.idEdtClientPFCPF.setText("");
        this.idEdtClientPFRG.setText("");
        this.idSpnClientPFSexo.setSelection(0);
        this.idEdtClientPFDate.setText(FunctionsTools.getCurrentDate());
        this.setArguments(new Bundle());
        this.onCreateFragment(true);
    }

    private View.OnClickListener onClickDate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int day,month,year;

            if (!idEdtClientPFDate.getText().toString().equals("") && idEdtClientPFDate.getText().toString().split("/").length > 0 ) {
                String data[] = idEdtClientPFDate.getText().toString().split("/");
                day  = Integer.parseInt(data[0]);
                month = Integer.parseInt(data[1]) - 1;
                year = Integer.parseInt(data[2]);
            }else{
                Calendar cal = Calendar.getInstance();
                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);
            }

            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    onDateSetListener,year,month,day);

            dialog.show();

        }
    };

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String data = "";

            if (dayOfMonth < 10){
                data += "0" + dayOfMonth;
            }else{
                data += dayOfMonth;
            }

            data += "/";

            monthOfYear = monthOfYear + 1;

            if (monthOfYear < 10){
                data += "0" + monthOfYear;
            }else{
                data += (monthOfYear );
            }

            data += "/" + year;

            idEdtClientPFDate.setText(data);
        }
    };
}
