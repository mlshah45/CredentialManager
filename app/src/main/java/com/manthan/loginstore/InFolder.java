package com.manthan.loginstore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Manthan on 1/29/2015.
 */
public class InFolder extends Activity {

    ListView InFolderlistView;
    Button addButton, okShowDialogButton,editShowDialogButton,deleteShowDialogButton,okEditDialogButton;
    DatabaseHelper dbHelper;
    List<ListElements> list;
    EditText TitleEditText, addIdEditText, addPasswordEditText;
    AlertDialog AdderD,EditD,ShowD;
    String folderName;
    HashMap<String,String> TitleNamesHm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
        Intent intent = getIntent();
        folderName = intent.getStringExtra(MainLogin.NameOfTheFolder);
        InFolderlistView = (ListView) findViewById(R.id.MainListView);
        addButton = (Button) findViewById(R.id.AddButton);
        dbHelper = new DatabaseHelper(InFolder.this);

        populateInFolderListView(folderName);
        addButton.setOnClickListener(addButtonOnClickListener);
        InFolderlistView.setOnItemClickListener(new OnItemCLick());

    }

    private void populateInFolderListView(String folderName) {
        List<FoldersTable> all_folders = dbHelper.getAllFolderCredentials(folderName);
TitleNamesHm = new HashMap<String, String>();
        list = new ArrayList<ListElements>();
        Log.d("stage 2", "populateInFolderListview");
        for (int j = 0; j < all_folders.size(); j++) {
            if(!all_folders.get(j).getTitle().equals("no_title"))
            {
                ListElements le = new ListElements();
                le.setName(all_folders.get(j).getTitle());
                le.setFolder(false);
                list.add(le);
                TitleNamesHm.put(all_folders.get(j).getTitle(),all_folders.get(j).getTitle());
            }
        }
        CustomAdapter adapter = new CustomAdapter(this, R.layout.list_row, list);
        InFolderlistView.invalidateViews();
        InFolderlistView.setAdapter(adapter);
    }

    private View.OnClickListener addButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LayoutInflater layoutInflator = LayoutInflater.from(InFolder.this);
            View addFileDialogBox = layoutInflator.inflate(R.layout.add_folderfile_dialogbox, null);
            //set alertdialog view to add_folderfile_dialogbox.xml
            final AlertDialog.Builder AdderDialog = new AlertDialog.Builder(InFolder.this);
            AdderDialog.setView(addFileDialogBox);
            TitleEditText = (EditText) addFileDialogBox.findViewById(R.id.AddFolderNameOrTitleEditTextView);
            addIdEditText = (EditText) addFileDialogBox.findViewById(R.id.AddIdEditTextView);
            addPasswordEditText = (EditText) addFileDialogBox.findViewById(R.id.AddPasswordEditTextView);
            Button addOkButton = (Button) addFileDialogBox.findViewById(R.id.AddOkButton);
            Button addCancelButton = (Button) addFileDialogBox.findViewById(R.id.AddCancelButton);
            CheckBox isFolderCheckBox = (CheckBox) addFileDialogBox.findViewById(R.id.isFolderCheckBox);
            isFolderCheckBox.setVisibility(View.GONE);
            AdderDialog.setCancelable(false);
            AdderD = AdderDialog.create();
            AdderD.show();

            addCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AdderD.dismiss();
                }
            });
            addOkButton.setOnClickListener((View.OnClickListener) addFileDialogOkButtonListener);
        }
    };

    private View.OnClickListener addFileDialogOkButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           // Log.d("addfileinfolder ",TitleEditText.getText().toString());
          //  TitleNamesHm.put("notitle","notitle");
            //!TitleNamesHm.containsValue(TitleEditText.getText().toString())
            if (!TitleNamesHm.containsValue(TitleEditText.getText().toString())&&!(addIdEditText.getText().toString().isEmpty() || addPasswordEditText.getText().toString().isEmpty() || TitleEditText.getText().toString().isEmpty())) {
                ListElements lee = new ListElements();
                lee.setFolder(false);
                lee.setName(TitleEditText.getText().toString());
                list.add(lee);
                FoldersTable folderT = new FoldersTable();
                folderT.setFolder(folderName);
                folderT.setPwd(addPasswordEditText.getText().toString());
                folderT.setId(addIdEditText.getText().toString());
                folderT.setTitle(TitleEditText.getText().toString());
                dbHelper.addFolderCredential(folderT);
                TitleNamesHm.put(TitleEditText.getText().toString(),TitleEditText.getText().toString());
//                CustomAdapter adapter = new CustomAdapter(MainLogin.this,R.layout.list_row,list);
//                MainListView.invalidateViews();
//                MainListView.setAdapter(adapter);
                CustomAdapter adapter = (CustomAdapter) InFolderlistView.getAdapter();
                adapter.notifyDataSetChanged();
                AdderD.dismiss();
            } else {
                String toastText = "Some fields are missing";
                if(TitleNamesHm.containsValue(TitleEditText.getText().toString()))
                    toastText= "Title already exists";
                Toast toast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };

    private class OnItemCLick implements android.widget.AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
            ListElements le = (ListElements) adapterView.getItemAtPosition(position);

            LayoutInflater layoutInflator = LayoutInflater.from(InFolder.this);
            View show_details_DialogBox = layoutInflator.inflate(R.layout.show_details_dialog, null);
            //set alertdialog view to add_show_details_dialogbox.xml
            final AlertDialog.Builder ShowDetailsDialog = new AlertDialog.Builder(InFolder.this);
            ShowDetailsDialog.setView(show_details_DialogBox);

            okShowDialogButton = (Button) show_details_DialogBox.findViewById(R.id.OkShowDialogButton);
            editShowDialogButton = (Button) show_details_DialogBox.findViewById(R.id.EditShowDialogButton);
            deleteShowDialogButton = (Button) show_details_DialogBox.findViewById(R.id.DeleteShowDialogButton);
            TextView TitleShowDialogTextView = (TextView) show_details_DialogBox.findViewById(R.id.TitleShowDialogTextView);
            TextView IdShowDialogTextView = (TextView) show_details_DialogBox.findViewById(R.id.IdShowDialogTextView);
            TextView PasswordShowDialogTextView = (TextView) show_details_DialogBox.findViewById(R.id.PasswordShowDIalogTextView);

            final FoldersTable folderT = dbHelper.getFolderCredentials(folderName, le.getName());
            TitleShowDialogTextView.setText("Title: " + folderT.getTitle());
            IdShowDialogTextView.setText("ID: " + folderT.getId());
            PasswordShowDialogTextView.setText("pwd: " + folderT.getPwd());

            ShowDetailsDialog.setCancelable(false);
            ShowD = ShowDetailsDialog.create();
            ShowD.show();
            okShowDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowD.dismiss();
                }
            });
            deleteShowDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbHelper.deleteFolderCredential(folderT);
                    TitleNamesHm.remove(folderT.getTitle());
                    populateInFolderListView(folderName);
                    ShowD.dismiss();
                }
            });
            editShowDialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowD.dismiss();
                    LayoutInflater layoutInflator = LayoutInflater.from(InFolder.this);
                    View edit_details_DialogBox = layoutInflator.inflate(R.layout.edit_dialog, null);
                    //set alertdialog view to add_edit_details_dialogbox.xml
                    final AlertDialog.Builder EditDetailsDialog = new AlertDialog.Builder(InFolder.this);
                    EditDetailsDialog.setView(edit_details_DialogBox);

                    okEditDialogButton = (Button) edit_details_DialogBox.findViewById(R.id.EditDialogOkButton);
                    Button cancelEditDialogButton = (Button) edit_details_DialogBox.findViewById(R.id.EditDialogCancelButton);

                    final EditText TitleEditDialogEditText = (EditText) edit_details_DialogBox.findViewById(R.id.EditDialogTitleEditText);
                    final EditText IdEditDialogTextView = (EditText) edit_details_DialogBox.findViewById(R.id.EditDialogIdEditText);
                    final EditText PasswordEditDialogTextView = (EditText) edit_details_DialogBox.findViewById(R.id.EditDialogPasswordEditText);

                    TitleEditDialogEditText.setText(folderT.getTitle());
                    IdEditDialogTextView.setText(folderT.getId());
                    PasswordEditDialogTextView.setText(folderT.getPwd());

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
                            if(!(TitleEditDialogEditText.getText().toString().isEmpty()||IdEditDialogTextView.getText().toString().isEmpty()||PasswordEditDialogTextView.getText().toString().isEmpty())&&!TitleNamesHm.containsKey(TitleEditDialogEditText.getText().toString()))
                            {
                                ListElements lee = new ListElements();
                                lee.setFolder(false);
                                lee.setName(TitleEditDialogEditText.getText().toString());
                                TitleNamesHm.remove(list.get(position).getName().toString());
                                TitleNamesHm.put(lee.getName(),lee.getName());
                                list.set(position,lee);
                                FoldersTable new_folder_credential = new FoldersTable();
                                new_folder_credential.setFolder(folderName);
                                new_folder_credential.setPwd(PasswordEditDialogTextView.getText().toString());
                                new_folder_credential.setId(IdEditDialogTextView.getText().toString());
                                new_folder_credential.setTitle(TitleEditDialogEditText.getText().toString());
                                dbHelper.updateFolderCredential(folderT,new_folder_credential);
//                CustomAdapter adapter = new CustomAdapter(MainLogin.this,R.layout.list_row,list);
//                MainListView.invalidateViews();
//                MainListView.setAdapter(adapter);
                                CustomAdapter adapter =  (CustomAdapter)InFolderlistView.getAdapter();
                                adapter.notifyDataSetChanged();

                                EditD.dismiss();
                            }
                            else{
                                String toastText="Some fields are missing";
                                if(TitleNamesHm.containsKey(TitleEditDialogEditText.getText().toString()))
                                    toastText = "Title already exists";
                                Toast toast = Toast.makeText(InFolder.this,toastText ,Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
                }
            });

        }
    }


}
