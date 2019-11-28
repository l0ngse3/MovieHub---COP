package com.example.myapplication.Ui.Adapter.Admin;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.Account;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.R;
import com.example.myapplication.Service.ClientService;
import com.example.myapplication.Service.TaskDone;
import com.example.myapplication.Ui.Fragment.Admin.ManageAccountFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ManageAccountAdapter extends RecyclerView.Adapter<ManageAccountAdapter.ViewHolder> implements Filterable {

    List<Account> list, fullist;
    Context context;
    ClientService service;
    ManageAccountFragment fragment;

    ManageAccountAdapter adapter;

    public ManageAccountAdapter(List<Account> list, ManageAccountFragment fragment) {
        this.list = list;
        this.fullist = new ArrayList<>(list);
        this.fragment = fragment;
        context = fragment.getContext();
        service = new ClientService(context);
        adapter = this;
    }

    public void deleteItem(int position){
        Account acDelete = list.get(position);
        list.remove(position);
        notifyItemRemoved(position);
        String urlDelete = APIConnectorUltils.HOST_NAME+"Account/Delete";
        service.simplePoster(urlDelete, acDelete, new TaskDone() {
            @Override
            public void done(String result) {
                String s = new Gson().fromJson(result, JsonObject.class).get("announce").toString();
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account ac = list.get(position);
        holder.onBind(ac, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return accountFilter;
    }

    private Filter accountFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Account> filterList = new ArrayList<>();

            if(charSequence.length()==0 || charSequence==null)
            {
                filterList.addAll(fullist);
                for(Account a : fullist){
                    Log.d("Mine fulllist", "performFiltering: "+a.getUsername());
                }
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(Account a : fullist){
                    Log.d("Mine fulllist", "performFiltering: "+a.getUsername());
                }
                for (Account item : fullist)
                {
                    if(item.getUsername().toLowerCase().contains(filterPattern))
                    {
                        filterList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll( (List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAva, imgDelete;
        TextView txtFullname, txtUsername;
        Button btnChangePassword;

        boolean isTrueCurPass;

        Account accountBinding;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAva = itemView.findViewById(R.id.imgAva);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            txtFullname = itemView.findViewById(R.id.txtFullname);
            btnChangePassword = itemView.findViewById(R.id.btnChangePassword);
            txtUsername = itemView.findViewById(R.id.txtUsername);;
        }

        public void onBind(Account ac, int pos){
            position = pos;
            accountBinding = ac;

            if(accountBinding.getRole()==0)
            {
                String urlProfile = APIConnectorUltils.HOST_NAME+"Profile/"+ac.getUsername();
                service.simpleGetter(urlProfile, new TaskDone() {
                    @Override
                    public void done(String result) {
                        Profile profile = new Gson().fromJson(result, Profile.class);

                        String imgUrl;
//                        Log.d("Mine account image", "done: "+result);
                        if(profile.getImage().equals("null"))
                        {
                            imgUrl = APIConnectorUltils.HOST_STORAGE_IMAGE+"saitama.png";
                        }
                        else {
                            imgUrl = APIConnectorUltils.HOST_STORAGE_IMAGE + profile.getImage();
                        }

                        Glide.with(context).load(imgUrl)
                                .centerCrop()
                                .apply(new RequestOptions().circleCrop())
                                .into(imgAva);


                        txtFullname.setText(profile.getFistName()+" "+profile.getLastName());
                    }
                });
            }
            else if(accountBinding.getRole()==1)
            {
                Glide.with(context).load(R.drawable.admin)
                        .centerCrop()
                        .apply(new RequestOptions().circleCrop())
                        .into(imgAva);
                txtFullname.setText(accountBinding.getUsername());
            }
            txtUsername.setText(accountBinding.getUsername());
            ///////////////////////////// change password
            btnChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = fragment.getLayoutInflater();
                    View alertLayout = inflater.inflate(R.layout.dialog_change_password, null);
                    final TextInputEditText txtCurrentPassword =  alertLayout.findViewById(R.id.txtCurrentPassword);
                    final TextInputEditText txtConfirmPassword =  alertLayout.findViewById(R.id.txtConfirmPassword);
                    final TextInputEditText txtNewPassword =  alertLayout.findViewById(R.id.txtNewPassword);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Account");
                    builder.setMessage("Do you want to delete this account?");
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

                            String curPass = txtCurrentPassword.getText().toString();
                            final String newPass = txtNewPassword.getText().toString();
                            final String confirmPass = txtConfirmPassword.getText().toString();

                            isTrueCurPass = false;
                            accountBinding.setPassword(curPass);

                            service.simplePoster(APIConnectorUltils.HOST_NAME + "AuthorService/authors", accountBinding, new TaskDone() {
                                @Override
                                public void done(String result) {
                                    isTrueCurPass = Boolean.parseBoolean(String.valueOf(new Gson().fromJson(result, JsonObject.class).get("result")));
                                    if(!isTrueCurPass){
                                        txtCurrentPassword.setError("Wrong password!");
                                    }
                                    else {
                                        if(!newPass.equals(confirmPass)){
                                            txtConfirmPassword.setError("Must be same new password!");
                                        }
                                        else{
                                            String urlChangePass = APIConnectorUltils.HOST_NAME+"Account/ChangePassword";
                                            Account changeAc = new Account();
                                            changeAc.setUsername(accountBinding.getUsername());
                                            changeAc.setPassword(newPass);
                                            service.simplePoster(urlChangePass, changeAc, new TaskDone() {
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

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Account");
                    builder.setMessage("Do you want to delete this account?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes, delete it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            adapter.deleteItem(position);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }


    }
    /////////////END VIEW HOLDER /////////////////////////
}
