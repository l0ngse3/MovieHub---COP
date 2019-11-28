package com.example.myapplication.Ui.Adapter.Admin;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.BitmapUltils;
import com.example.myapplication.Model.Comment;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.R;
import com.example.myapplication.Service.ClientService;
import com.example.myapplication.Service.TaskDone;
import com.example.myapplication.Ui.Activity.Admin.ManageCommentActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ManageCommentAdapter extends RecyclerView.Adapter<ManageCommentAdapter.ViewHolder> implements Filterable {
    private List<Comment> list, fullist;
    private ManageCommentActivity context;

    private Profile profile;
    private Comment comment;
    private ClientService service;
    private ManageCommentAdapter adapter;

    public ManageCommentAdapter(List<Comment> list, ManageCommentActivity context) {
        this.list = list;
        this.context = context;
        service = new ClientService(context);
        adapter = this;
        fullist = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_comment_manage, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        comment = list.get(position);
        holder.onBind(comment, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void deleteItem(int position) {
        Comment deleteCmt = list.get(position);
        list.remove(position);
        notifyItemRemoved(position);

        String urlDeleteComment = APIConnectorUltils.HOST_STORAGE_COMMENT + "DeleteComment";
        service.simplePoster(urlDeleteComment, deleteCmt, new TaskDone() {
            @Override
            public void done(String result) {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Filter getFilter() {
        return commentFilter;
    }

    private Filter commentFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Comment> filterList = new ArrayList<>();

            if(charSequence.length()==0 || charSequence==null)
            {
                filterList.addAll(fullist);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Comment item : fullist)
                {
                    if(item.getContent().toLowerCase().contains(filterPattern))
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


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvaCmt, imgMoreAction;
        TextView txtFullnameCmt, txtContentCmt;
        Comment comment;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvaCmt = itemView.findViewById(R.id.imgAvaCmt);
            txtContentCmt = itemView.findViewById(R.id.txtContentCmt);
            txtFullnameCmt = itemView.findViewById(R.id.txtFullnameCmt);
            imgMoreAction = itemView.findViewById(R.id.imgMoreAction);
        }

        public void onBind(Comment c, int pos)
        {
            comment = c;
            position = pos;
            service.getInfoProfile(comment.getUsername(), new TaskDone() {
                @Override
                public void done(String result) {
                    profile = new Gson().fromJson(result, Profile.class);
                    if(context!=null)
                    {
                        String imgUrl;
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
                                .into(imgAvaCmt);
                    }
                    txtFullnameCmt.setText(profile.getFistName()+" "+profile.getLastName());
                }
            });

            txtContentCmt.setText(comment.getContent());


            imgMoreAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete comment!");
                    builder.setMessage("Do you want to delete this comment?");
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
///////////////////////////////////

}
