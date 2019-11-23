package com.example.myapplication.Ui.Adapter.Client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.BitmapUltils;
import com.example.myapplication.Model.Comment;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.R;
import com.example.myapplication.Service.ClientService;
import com.example.myapplication.Service.TaskDone;
import com.example.myapplication.Ui.Activity.Client.FilmActivity;
import com.google.gson.Gson;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> list;
    private FilmActivity context;

    private Profile profile;
    String ownerUser;
    Comment comment;
    ClientService  service;
//    int posDelete=0;

    public CommentAdapter(List<Comment> list, FilmActivity context, String ownerUser) {
        this.list = list;
        this.context = context;
        this.ownerUser = ownerUser;
        service = new ClientService(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        comment = list.get(position);
        holder.onBind(comment, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

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
                        if(profile.getImage().equals("null"))
                        {
                            String imgUrl = APIConnectorUltils.HOST_STORAGE_IMAGE+"saitama.png";
                            BitmapUltils.loadCircleImageInto(context, imgUrl, imgAvaCmt);
                        }
                        else {
                            String imgUrl = APIConnectorUltils.HOST_STORAGE_IMAGE + profile.getImage();
                            BitmapUltils.loadCircleImageInto(context, imgUrl, imgAvaCmt);
                        }
                    }
                    txtFullnameCmt.setText(profile.getFistName()+" "+profile.getLastName());
                }
            });

            txtContentCmt.setText(comment.getContent());
            if(comment.getUsername().equals(ownerUser))
            {
                imgMoreAction.setVisibility(View.VISIBLE);
            }

            imgMoreAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context,view);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId())
                            {
                                case R.id.action_edit:
                                    context.getTxtCmtText().setText(comment.getContent());
                                    context.getTxtCmtText().requestFocus();
                                    context.setFlagEdit(1);
                                    context.setComment(comment);
                                    return true;

                                case R.id.action_delete:
                                    context.deleteComment(position);
                                    return true;
                            }
                            return false;
                        }
                    });// to implement on click event on items of menu
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.more_action_comment, popup.getMenu());
                    popup.show();
                }
            });
        }
    }
}
