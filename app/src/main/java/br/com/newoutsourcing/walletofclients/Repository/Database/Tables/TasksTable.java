package br.com.newoutsourcing.walletofclients.Repository.Database.Tables;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.newoutsourcing.walletofclients.Objects.Tasks;
import br.com.newoutsourcing.walletofclients.Views.Bases.TableConfigurationBase;

public class TasksTable extends TableConfigurationBase<Tasks> {

    public enum Fields {
        ID_TASK,
        TITLE,
        ID_CLIENT,
        ALL_DAY,
        DATE,
        HOUR,
        OBSERVATION,
        ID_NUVEM,
        UP
    }

    public TasksTable(Context context) {
        super(context);
        super.Table = "TB_TASKS";
    }

    public static TasksTable newInstance(Context context){
        return new TasksTable(context);
    }

    public List<Tasks> SelectForInsert(){
        this.SQL
                = " Select " + this.getFields()
                + " From " + this.Table
                + " Where " + Fields.ID_NUVEM.name() + " Is Null "
                + " Order by " + Fields.ID_TASK.name();
        return this.Consulta(SQL);
    }

    public List<Tasks> SelectForUpdate(){
        this.SQL
                = " Select " + this.getFields()
                + " From " + this.Table
                + " Where " + Fields.ID_NUVEM.name() + " Is Not Null "
                + " And (" + Fields.UP.name() + " = 'S' Or " + Fields.UP.name() + " Is Null)"
                + " Order by " + Fields.ID_TASK.name();
        return this.Consulta(SQL);
    }

    @Override
    public List<Tasks> Select(long id) {
        super.openDatabaseInstance();
        try{
            if (id > 0){
                this.SQL
                        = " Select " + this.getFields() + " From " + this.Table
                        + " Where " + Fields.ID_TASK + " = " + id
                        + " Order by  Cast(" + Fields.DATE.name() + " As Date) Asc,"
                        + " Cast (" + Fields.HOUR + " As Time) Asc";
            }else{
                this.SQL
                        = " Select " + this.getFields() + " From " + this.Table
                        + " Order by  Cast(" + Fields.DATE.name() + " As Date) Asc,"
                        + " Cast (" + Fields.HOUR + " As Time) Asc";
            }

            return this.Consulta(this.SQL);
        }catch (Exception ex){
            throw ex;
        }finally {
            super.closeDatabaseInstance();
        }
    }

    public List<Tasks> Select(String date){
        super.openDatabaseInstance();
        try{
            if (!date.isEmpty()) {
                this.SQL
                        = " Select " + this.getFields() + " From " + this.Table
                        + " Where " + Fields.DATE + " = '" + date + "'"
                        + " Order by  Cast(" + Fields.DATE.name() + " As Date) Asc,"
                        + " Cast (" + Fields.HOUR + " As Time) Asc";
                return this.Consulta(this.SQL);
            }
            return new ArrayList<>();
        }catch (Exception ex){
            throw ex;
        }finally {
            super.closeDatabaseInstance();
        }
    }

    private List<Tasks> Consulta(String SQL){
        if (SQL.isEmpty()) return new ArrayList<>();
        super.openDatabaseInstance();
        try{
            List<Tasks> list = new ArrayList<Tasks>();

            this.cursor = this.database.rawQuery(this.SQL,null);

            if (this.cursor.getCount()>0){
                this.cursor.moveToFirst();
                Tasks obj;
                do{
                    obj = new Tasks();

                    obj.setTasksId(this.cursor.getInt(0));
                    obj.setTitle(this.cursor.getString(1));
                    obj.setClienteId(this.cursor.getInt(2));
                    obj.setAllDay(this.cursor.getInt(3));
                    obj.setDate(this.cursor.getString(4));
                    obj.setHour(this.cursor.getString(   5));
                    obj.setObservation(this.cursor.getString(6));
                    obj.setIdNuvem(this.cursor.getLong(7));
                    obj.setUpdate(this.cursor.getString(8));

                    list.add(obj);
                }while (this.cursor.moveToNext());
            }
            this.cursor.close();

            return list;
        }catch (Exception ex){
            throw ex;
        }finally {
            super.closeDatabaseInstance();
        }
    }

    @Override
    public long Insert(Tasks tasks) {
        super.openDatabaseInstance();
        try{
            ContentValues values = new ContentValues();

            values.put(Fields.TITLE.name(),tasks.getTitle());
            values.put(Fields.ID_CLIENT.name(),tasks.getClienteId());
            values.put(Fields.ALL_DAY.name(),tasks.getAllDay());
            values.put(Fields.DATE.name(),tasks.getDate());
            values.put(Fields.HOUR.name(),tasks.getHour());
            values.put(Fields.OBSERVATION.name(),tasks.getObservation());

            return this.database.insert(this.Table,null,values);
        }catch (Exception ex){
            throw ex;
        }finally {
            super.closeDatabaseInstance();
        }
    }

    @Override
    public Boolean Update(Tasks tasks) {
        super.openDatabaseInstance();
        try {
            ContentValues values = new ContentValues();

            values.put(Fields.TITLE.name(), tasks.getTitle());
            values.put(Fields.ID_CLIENT.name(), tasks.getClienteId());
            values.put(Fields.ALL_DAY.name(), tasks.getAllDay());
            values.put(Fields.DATE.name(), tasks.getDate());
            values.put(Fields.HOUR.name(), tasks.getHour());
            values.put(Fields.OBSERVATION.name(), tasks.getObservation());
            values.put(Fields.ID_NUVEM.name(), tasks.getIdNuvem());
            values.put(Fields.UP.name(), tasks.getUpdate());

            this.database.update(this.Table, values,
                    Fields.ID_TASK.name() + " = " + tasks.getTasksId(),
                    null);

            return true;
        }catch (Exception ex){
            throw ex;
        }finally {
            super.closeDatabaseInstance();
        }
    }

    @Override
    public Boolean Delete(Tasks tasks) {
        super.openDatabaseInstance();
        try {
            if (tasks.getTasksId() > 0) {
                this.database.delete(this.Table,
                        Fields.ID_TASK.name() + " = " + tasks.getTasksId(),
                        null);
            }
            return true;
        }catch (Exception ex){
            throw ex;
        }finally {
            super.closeDatabaseInstance();
        }
    }

    public Boolean DeleteByClientId(long clientId){
        super.openDatabaseInstance();
        try{
            if (clientId > 0){
                this.database.delete(this.Table,
                        Fields.ID_CLIENT.name() + " = " + clientId,
                        null);
            }
            return true;
        }catch (Exception ex){
            throw ex;
        }finally {
            super.closeDatabaseInstance();
        }
    }

    @Override
    protected String getFields() {
        String StringFields = "";

        for(Fields Field: Fields.values()){
            StringFields += Field.name() + ",";
        }

        if (StringFields.length() > 0){
            StringFields = StringFields.substring(0,StringFields.length()-1);
        }else{
            StringFields = "*";
        }

        return StringFields;
    }
}
