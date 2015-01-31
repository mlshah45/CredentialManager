package com.manthan.loginstore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainLogin extends Activity {

    private Button addButton, okShowDialogButton,editShowDialogButton,deleteShowDialogButton,okEditDialogButton;
    private ListView MainListView;
    private EditText FolderNameOrTitleEditText, addIdEditText, addPasswordEditText;
    private CheckBox isFolderCheckBox;
    List<ListElements> list;
   AlertDialog AdderD, ShowD, EditD;
    DatabaseHelper dbHelper;
    static  final String NameOfTheFolder = "FolderName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        addButton = (Button) findViewById(R.id.AddButton);
        MainListView = (ListView)findViewById(R.id.MainListView);
        dbHelper = new DatabaseHelper(MainLogin.this);
        //open Enter Password Dialog Box
        setPasswordDialogBox();
        //show the list of credentials stored

        populateListView();

        MainListView.setOnItemClickListener(new OnItemCLick());

    }

    private void setPasswordDialogBox() {
        //get passworddialogbox.xml
        LayoutInflater layoutInflator = LayoutInflater.from(MainLogin.this);
        View PasswordDialogBox = layoutInflator.inflate(R.layout.passworddialogbox, null);
        //set alertdialog view to passwroddialogbox.xml
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainLogin.this);
        alertDialog.setView(PasswordDialogBox);
        final EditText input = (EditText) PasswordDialogBox.findViewById(R.id.DialogBoxEditTextView);
        alertDialog.setCancelable(false);
        final AlertDialog alertD = alertDialog.create();
        alertD.show();
        Button dialogButton = (Button) alertD.findViewById(R.id.dialogBoxButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String time[] = sdf.format(cal.getTime()).split(":");
                String currentTime = time[0] + time[1];
                //validating the input
                if (input.getText().toString().equals(currentTime)) {
                    //if password is correct, dialogbox is removed
                    alertD.dismiss();
                } else {
                    //if password is wrong, provide a toast message
                    CharSequence text = "";
                    if (input.getText().toString().isEmpty())
                        text = "Blank Field. Enter Password!";
                    else
                        text = "Invalid Password. Try Again!";

                    int duration = Toast.LENGTH_SHORT;
                    input.setText("");
                    Toast toast = Toast.makeText(MainLogin.this, text, duration);
                    toast.show();
                }
            }
        });
    }


    private void populateListView() {
//final ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1,FRUITS);
//        MainListView.setAdapter(aa);

        List<CredentialsTable> all_credentials = dbHelper.getAllCredentials();
        List<FoldersTable> all_folders = dbHelper.getAllFolder();

        list = new ArrayList<ListElements>();
Log.d("stage 2", "populateview");
        for(int j=0;j<all_folders.size();j++)
        {
            ListElements le = new ListElements();
            le.setName(all_folders.get(j).getFolder());
            le.setFolder(true);
            list.add(le);
        }

        for(int i=0;i<all_credentials.size();i++)
        {
            ListElements le = new ListElements();
            Log.d("name",all_credentials.get(i).getTitle());
            le.setName(all_credentials.get(i).getTitle());
            le.setFolder(false);
            list.add(le);
        }

       CustomAdapter adapter = new CustomAdapter(this,R.layout.list_row,list);
        MainListView.invalidateViews();
        MainListView.setAdapter(adapter);


    }

    private class OnItemCLick implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int position, final long l) {
            ListElements le = (ListElements) adapterView.getItemAtPosition(position);
            if(le.isFolder())
            {
                Intent intent = new Intent(MainLogin.this,InFolder.class);
                intent.putExtra(NameOfTheFolder,le.getName().toString());
                startActivity(intent);
            }
            else
            {
                LayoutInflater layoutInflator = LayoutInflater.from(MainLogin.this);
                View show_details_DialogBox = layoutInflator.inflate(R.layout.show_details_dialog, null);
                //set alertdialog view to add_show_details_dialogbox.xml
                final AlertDialog.Builder ShowDetailsDialog = new AlertDialog.Builder(MainLogin.this);
                ShowDetailsDialog.setView(show_details_DialogBox);

                okShowDialogButton = (Button) show_details_DialogBox.findViewById(R.id.OkShowDialogButton);
                editShowDialogButton = (Button) show_details_DialogBox.findViewById(R.id.EditShowDialogButton);
                deleteShowDialogButton = (Button) show_details_DialogBox.findViewById(R.id.DeleteShowDialogButton);
               TextView TitleShowDialogTextView = (TextView) show_details_DialogBox.findViewById(R.id.TitleShowDialogTextView);
                TextView IdShowDialogTextView = (TextView) show_details_DialogBox.findViewById(R.id.IdShowDialogTextView);
                TextView PasswordShowDialogTextView = (TextView) show_details_DialogBox.findViewById(R.id.PasswordShowDIalogTextView);

                final CredentialsTable credential = dbHelper.getCredentials(le.getName().toString());
                TitleShowDialogTextView.setText("Title: "+credential.getTitle());
                IdShowDialogTextView.setText("ID: "+credential.getId());
                PasswordShowDialogTextView.setText("pwd: "+credential.getPwd());

                ShowDetailsDialog.setCancelable(false);
                ShowD = ShowDetailsDialog.create();
                ShowD.show();

                okShowDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowD.dismiss();
                    }
                });
             //   editShowDialogButton.setOnClickListener();
                deleteShowDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CredentialsTable credentialsToDelete = new CredentialsTable();
                        credentialsToDelete.setTitle(credential.getTitle());
                       dbHelper.deleteCredential(credentialsToDelete);
                        populateListView();
                        ShowD.dismiss();
                    }
                });

              editShowDialogButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      ShowD.dismiss();
                      LayoutInflater layoutInflator = LayoutInflater.from(MainLogin.this);
                      View edit_details_DialogBox = layoutInflator.inflate(R.layout.edit_dialog, null);
                      //set alertdialog view to add_edit_details_dialogbox.xml
                      final AlertDialog.Builder EditDetailsDialog = new AlertDialog.Builder(MainLogin.this);
                      EditDetailsDialog.setView(edit_details_DialogBox);

                      okEditDialogButton = (Button) edit_details_DialogBox.findViewById(R.id.EditDialogOkButton);
                      Button cancelEditDialogButton = (Button) edit_details_DialogBox.findViewById(R.id.EditDialogCancelButton);

                      final EditText TitleEditDialogEditText = (EditText) edit_details_DialogBox.findViewById(R.id.EditDialogTitleEditText);
                      final EditText IdEditDialogTextView = (EditText) edit_details_DialogBox.findViewById(R.id.EditDialogIdEditText);
                      final EditText PasswordEditDialogTextView = (EditText) edit_details_DialogBox.findViewById(R.id.EditDialogPasswordEditText);

                      TitleEditDialogEditText.setText(credential.getTitle());
                      IdEditDialogTextView.setText(credential.getId());
                      PasswordEditDialogTextView.setText(credential.getPwd());

                      EditDetailsDialog.setCancelable(false);
                      EditD = EditDetailsDialog.create();
                      EditD.show();

                      cancelEditDialogButton.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              EditD.dismiss();
                          }
                      });
                      okEditDialogButton.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              if(!(TitleEditDialogEditText.getText().toString().isEmpty()||IdEditDialogTextView.getText().toString().isEmpty()||PasswordEditDialogTextView.getText().toString().isEmpty()))
                              {
                                  ListElements lee = new ListElements();
                                  lee.setFolder(false);
                                  lee.setName(TitleEditDialogEditText.getText().toString());
                                  list.set(position,lee);
                                  CredentialsTable new_credential = new CredentialsTable();

                                  new_credential.setPwd(PasswordEditDialogTextView.getText().toString());
                                  new_credential.setId(IdEditDialogTextView.getText().toString());
                                  new_credential.setTitle(TitleEditDialogEditText.getText().toString());
                                  dbHelper.updateCredential(credential,new_credential);
//                CustomAdapter adapter = new CustomAdapter(MainLogin.this,R.layout.list_row,list);
//                MainListView.invalidateViews();
//                MainListView.setAdapter(adapter);
                                  CustomAdapter adapter =  (CustomAdapter)MainListView.getAdapter();
                                  adapter.notifyDataSetChanged();

                                  EditD.dismiss();
                              }
                              else{
                                  Toast toast = Toast.makeText(MainLogin.this, "Some fields are missing",Toast.LENGTH_SHORT);
                                  toast.show();
                              }
                          }
                      });
                  }
              });
            }

        }

    }





    public void addButtonOnClick(View view)
    {
        LayoutInflater layoutInflator = LayoutInflater.from(MainLogin.this);
        View addFolderFileDialogBox = layoutInflator.inflate(R.layout.add_folderfile_dialogbox, null);
        //set alertdialog view to add_folderfile_dialogbox.xml
        final AlertDialog.Builder AdderDialog = new AlertDialog.Builder(MainLogin.this);
        AdderDialog.setView(addFolderFileDialogBox);
        isFolderCheckBox = (CheckBox) addFolderFileDialogBox.findViewById(R.id.isFolderCheckBox);
         FolderNameOrTitleEditText = (EditText) addFolderFileDialogBox.findViewById(R.id.AddFolderNameOrTitleEditTextView);
        addIdEditText = (EditText) addFolderFileDialogBox.findViewById(R.id.AddIdEditTextView);
        addPasswordEditText = (EditText) addFolderFileDialogBox.findViewById(R.id.AddPasswordEditTextView);
        AdderDialog.setCancelable(false);
        AdderD = AdderDialog.create();
        AdderD.show();
        Button addOkButton = (Button) AdderD.findViewById(R.id.AddOkButton);
        Button addCancelButton = (Button)AdderD.findViewById(R.id.AddCancelButton);
        isFolderCheckBox.setOnClickListener((View.OnClickListener) isFolderCheckBoxOnClickListener);
        addCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdderD.dismiss();
            }
        });
        addOkButton.setOnClickListener((View.OnClickListener) addDialogOkButtonListener);
    }

    private View.OnClickListener isFolderCheckBoxOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if(((CheckBox)view).isChecked())
            {
                addIdEditText.setVisibility(View.GONE);
                addPasswordEditText.setVisibility(View.GONE);
                FolderNameOrTitleEditText.setHint("FolderName");
            }
            else{
                addIdEditText.setVisibility(View.VISIBLE);
                addPasswordEditText.setVisibility(View.VISIBLE);
                FolderNameOrTitleEditText.setHint("Id eg: 1234@gmail.com");
            }
        }
    };

private View.OnClickListener addDialogOkButtonListener = new View.OnClickListener() {

    @Override
    public void onClick(View view) {
        String toastText = "Enter a valid Folder Name";
        boolean toastFlag= true;
        CredentialsTable credentialT = new CredentialsTable();
        FoldersTable folderT = new FoldersTable();
        if(isFolderCheckBox.isChecked())
        {
            if(!FolderNameOrTitleEditText.getText().toString().isEmpty())
            {
                ListElements lee = new ListElements();
                lee.setFolder(true);
                lee.setName(FolderNameOrTitleEditText.getText().toString());
                list.add(lee);
                folderT.setFolder(FolderNameOrTitleEditText.getText().toString());
                dbHelper.addFolderCredential(folderT);
                CustomAdapter adapter = (CustomAdapter) MainListView.getAdapter();
                adapter.notifyDataSetChanged();
                toastFlag=false;
                AdderD.dismiss();
            }
        }
        else{
            if(!(addIdEditText.getText().toString().isEmpty()||addPasswordEditText.getText().toString().isEmpty()||FolderNameOrTitleEditText.getText().toString().isEmpty()))
            {
                ListElements lee = new ListElements();
                lee.setFolder(false);
                lee.setName(FolderNameOrTitleEditText.getText().toString());
                list.add(lee);
                credentialT.setPwd(addPasswordEditText.getText().toString());
                credentialT.setId(addIdEditText.getText().toString());
                credentialT.setTitle(FolderNameOrTitleEditText.getText().toString());
                dbHelper.addCredential(credentialT);
//                CustomAdapter adapter = new CustomAdapter(MainLogin.this,R.layout.list_row,list);
//                MainListView.invalidateViews();
//                MainListView.setAdapter(adapter);
              CustomAdapter adapter =  (CustomAdapter)MainListView.getAdapter();
                adapter.notifyDataSetChanged();
                toastFlag=false;
                AdderD.dismiss();
            }
            else{
                toastText="Some fields are missing";
            }

        }
        if(toastFlag)
        {
            Toast toast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
};


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
