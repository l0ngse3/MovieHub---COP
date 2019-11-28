package com.example.myapplication.Ui.Fragment.Admin;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Account;
import com.example.myapplication.Model.Genre;
import com.example.myapplication.R;
import com.example.myapplication.Service.ClientService;
import com.example.myapplication.Service.TaskDone;
import com.example.myapplication.Ui.Adapter.Admin.ManageAccountAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ManageAccountFragment extends Fragment {

    EditText edtSearch;
    FloatingActionButton fabAdd;
    ShimmerFrameLayout shimmer_view_container;

    RecyclerView rcyListAccount;
    Spinner spinnerRole;
    Context context;
    List<Account> accountList, fullAccountList;
    ManageAccountAdapter adapter;

    SwipeRefreshLayout swipeRefreshHome;
    ClientService service;
    ManageAccountFragment fragment;
    Account addAccount;
    boolean isRegistable;

    View view;

    public ManageAccountFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        process();
    }

    private void process() {

        //add new account
        fabAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LayoutInflater inflater = fragment.getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog_add_account, null);
                final TextInputEditText txtUsername =  alertLayout.findViewById(R.id.txtUsername);
                final TextInputEditText txtConfirmPassword =  alertLayout.findViewById(R.id.txtConfirmPassword);
                final TextInputEditText txtPassword =  alertLayout.findViewById(R.id.txtPassword);
                final CheckBox checkboxAdmin =  alertLayout.findViewById(R.id.checkboxAdmin);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Create account");
                builder.setMessage("Fill the blank.");
                builder.setCancelable(false);
                builder.setView(alertLayout);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int i) {

                    }
                });
                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Try to display", Toast.LENGTH_SHORT).show();
                        final String username = txtUsername.getText().toString();
                        final String password = txtPassword.getText().toString();
                        final String confirmPass = txtConfirmPassword.getText().toString();
                        final boolean isAdmin = checkboxAdmin.isChecked();

                        isRegistable = false;
                        addAccount.setUsername(username);
                        addAccount.setPassword("");

                        service.simplePoster(APIConnectorUltils.HOST_NAME + "AuthorService/CheckUsername", addAccount, new TaskDone() {
                            @Override
                            public void done(String result) {
                                Toast.makeText(context, "Try to display 2", Toast.LENGTH_SHORT).show();
                                isRegistable = Boolean.parseBoolean(String.valueOf(new Gson().fromJson(result, JsonObject.class).get("result")));
                                if(!isRegistable){
                                    txtUsername.setError("Username has already been taken!");
                                }
                                else {
                                    if(!password.equals(confirmPass)){
                                        txtConfirmPassword.setError("Must be same new password!");
                                    }
                                    else{
                                        String urlAddAccount = APIConnectorUltils.HOST_NAME;
                                        Account addAccount = new Account();
                                        addAccount.setUsername(username);
                                        addAccount.setPassword(password);
                                        int role = isAdmin ? 1 : 0;
                                        addAccount.setRole(role);
                                        if(role == 0){
                                            urlAddAccount+="AuthorService/register";
                                        }
                                        else {
                                            urlAddAccount+="Account/Add";
                                        }

                                        service.simplePoster(urlAddAccount, addAccount, new TaskDone() {
                                            @Override
                                            public void done(String result) {
                                                String s = new Gson().fromJson(result, JsonObject.class).get("announce").toString();
                                                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                            }
                                        });
                                    }
                                }
                            }
                        });
                        /////////// change password////////////////
                    }
                });
            }
        });

        swipeRefreshHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init(view);
            }
        });

        //select user
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                accountList.clear();
                if(selectedItem.equals("All role"))
                {
                    if(adapter != null){
                        accountList.addAll(fullAccountList);
                        adapter.notifyDataSetChanged();
                    }
                }
                else if(selectedItem.equals("Administrator")) {
                    for( Account account : fullAccountList)
                    {
                        if(account.getRole()==1){
                            accountList.add(account);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                else if(selectedItem.equals("User")) {
                    for( Account account : fullAccountList)
                    {
                        if(account.getRole()==0){
                            accountList.add(account);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ///////////
        ///////filter list account ///////////////////
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void init(View view) {
        fabAdd = view.findViewById(R.id.fabAdd);
        shimmer_view_container = view.findViewById(R.id.shimmer_view_container);
        edtSearch = view.findViewById(R.id.edtSearch);
        fragment = this;
        this.view = view;
        isRegistable = false;

        rcyListAccount = view.findViewById(R.id.rcyListAccount);
        spinnerRole = view.findViewById(R.id.spinnerRole);
        context = this.getContext();
        swipeRefreshHome = view.findViewById(R.id.swipeRefreshHome);
        service = new ClientService(context);
        accountList = new ArrayList<>();
        addAccount = new Account();
        fullAccountList = new ArrayList<>();
        shimmer_view_container.setVisibility(View.VISIBLE);
        shimmer_view_container.startShimmer();

        ArrayAdapter<CharSequence> ar_adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.role_array, R.layout.spinner_item);
        ar_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(ar_adapter);

        // init account list
        String urlAllAccount = APIConnectorUltils.HOST_NAME+"Account/All";
        service.simpleGetter(urlAllAccount, new TaskDone() {
            @Override
            public void done(String result) {
                Account[] listAc = new Gson().fromJson(result, Account[].class);
                accountList.addAll(Arrays.asList(listAc));
                adapter = new ManageAccountAdapter(accountList, fragment);
                rcyListAccount.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                rcyListAccount.setAdapter(adapter);
                fullAccountList.addAll(accountList);

                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmer();
                rcyListAccount.setVisibility(View.VISIBLE);
                swipeRefreshHome.setRefreshing(false);
            }
        });

        //////////
    }
}
