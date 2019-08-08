package br.com.newoutsourcing.walletofclients.Views.Activitys;

import android.os.Handler;
import br.com.newoutsourcing.walletofclients.Tools.FunctionsTools;
import br.com.newoutsourcing.walletofclients.R;
import br.com.newoutsourcing.walletofclients.Repository.Database.Configurations.SessionDatabase;
import br.com.newoutsourcing.walletofclients.Repository.Database.Tables.TBAdditionalInformationDatabase;
import br.com.newoutsourcing.walletofclients.Repository.Database.Tables.TBAddressDatabase;
import br.com.newoutsourcing.walletofclients.Repository.Database.Tables.TBClientDatabase;
import br.com.newoutsourcing.walletofclients.Repository.Database.Tables.TBLegalPersonDatabase;
import br.com.newoutsourcing.walletofclients.Repository.Database.Tables.TBPhysicalPersonDatabase;
import br.com.newoutsourcing.walletofclients.Repository.Database.Tables.TBTasksDatabase;
import br.com.newoutsourcing.walletofclients.Tools.NofiticationMessages;
import br.com.newoutsourcing.walletofclients.Views.Bases.BaseActivity;

public class SplashActivity extends BaseActivity {

    public SplashActivity() {
        super(R.layout.activity_splash);
    }

    @Override
    protected void onConfiguration() {
        NofiticationMessages.onNotificationUse();
        this.onLoadDatabaseSession();
        this.onStartActivity();
    }

    private void onLoadDatabaseSession(){
        try{
            SessionDatabase.TB_CLIENT = TBClientDatabase.newInstance(SplashActivity.this);
            SessionDatabase.TB_PHYSICAL_PERSON = TBPhysicalPersonDatabase.newInstance(SplashActivity.this);
            SessionDatabase.TB_LEGAL_PERSON = TBLegalPersonDatabase.newInstance(SplashActivity.this);
            SessionDatabase.TB_ADDITIONAL_INFORMATION = TBAdditionalInformationDatabase.newInstance(SplashActivity.this);
            SessionDatabase.TB_ADDRESS = TBAddressDatabase.newInstance(SplashActivity.this);
            SessionDatabase.TB_TASKS = TBTasksDatabase.newInstance(SplashActivity.this);
        }catch (Exception ex){
            FunctionsTools.showAlertDialog(SplashActivity.this,"Erro","Ocorreu um erro ao inicializar a aplicação. Tente novamente!","Fechar");
            FunctionsTools.closeActivity(SplashActivity.this);
        }
    }

    private void onStartActivity(){
        new Handler().postDelayed(() -> {
            FunctionsTools.startActivity(SplashActivity.this,ListClientActivity.class,null);
            FunctionsTools.closeActivity(SplashActivity.this);
        },1000);
    }


}
