package com.example.myapplication.Ui.Fragment.Client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.Model.APIConnectorUltils;
import com.example.myapplication.Model.BitmapUltils;
import com.example.myapplication.Model.VideoUltils;
import com.example.myapplication.Service.RequestQueueUltil;
import com.example.myapplication.Service.TaskDone;
import com.example.myapplication.Ui.Adapter.Client.FilmProfileAdapter;
import com.example.myapplication.Model.Film;
import com.example.myapplication.Model.FilmWatched;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.ShareViewModel;
import com.example.myapplication.R;
import com.example.myapplication.Service.ClientService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment{

    ImageView imgAva, imgChangeProfile;
    TextView txtUser;
    TextView txtFullname, txtNoContent;
    ShareViewModel viewModel; //view model to get username
    ShimmerFrameLayout mShimmerViewContainer;

    RecyclerView rcyFilmWatched;
    FilmProfileAdapter adapter;
    Fragment fragment;
    List<Film> listFilm;
    List<FilmWatched> listFilmWatched;

    List<String> listIdFilm; // contain list id of film to request


    Profile profile;
    Bitmap bitmap;
    int REQUEST_CODE_PHOTO = 1000;
    Context context;
    View view;
    RequestQueueUltil requestQueueUltil;
    int count;
    ClientService service;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        handleListener();
    }

    private void handleListener() {
        //select image from client's phone
        imgAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                startActivityForResult(photoPicker, REQUEST_CODE_PHOTO);
            }
        });

        txtFullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                imgChangeProfile.setVisibility(View.VISIBLE);
                imgChangeProfile.setImageResource(R.drawable.ic_menu_send);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        imgChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] name = txtFullname.getText().toString().trim().split(" ");
                if(name.length == 1)
                {
                    if(name[0].length()!=0)
                    {
                        Toast.makeText(context, "Your name must have space between firtname and last name", Toast.LENGTH_LONG).show();
                        txtFullname.setError("Can not change");
                        imgChangeProfile.setImageResource(R.drawable.icon_error_x);
                    }
                    else if(name[0].length()==0)
                    {
                        Toast.makeText(context, "Your name is empty", Toast.LENGTH_LONG).show();
                        txtFullname.setError("Empty");
                        imgChangeProfile.setImageResource(R.drawable.icon_error_x);
                    }

                }
                else if(name.length >1){
                    profile.setFistName(name[0]);
                    StringBuilder lastName = new StringBuilder();

                    for(int i=1; i<name.length; i++)
                    {
                        lastName.append(name[i]);
                    }
                    Toast.makeText(context, lastName, Toast.LENGTH_LONG).show();
                    profile.setLastName(lastName.toString());
                    profile.setImage(new Gson().fromJson(viewModel.getProfile().getValue(), Profile.class).getImage());
                    imgChangeProfile.setImageResource(R.drawable.icon_check);

                    ClientService service = new ClientService(context);
                    service.postInfoAccount(profile, context);
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_PHOTO && resultCode ==  RESULT_OK)
        {
            if (data != null) {
                final Uri dataPath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), dataPath);
                    BitmapUltils.loadCircleBitmapInto(context, bitmap, imgAva);

                    ClientService service = new ClientService(context);
                    Toast.makeText(context, "Mine image: "+VideoUltils.getRealPathFromUriImage(dataPath, context), Toast.LENGTH_LONG).show();
                    service.postUploadImage(VideoUltils.getRealPathFromUriImage(dataPath, context), profile.getUsername() + ".png",
                            new TaskDone() {
                                @Override
                                public void done(String result) {
                                    Toast.makeText(context, "Avatar Updated!", Toast.LENGTH_SHORT).show();
                                }
                            });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void init(final View view) {
        imgAva = view.findViewById(R.id.imgAva);
        txtFullname = view.findViewById(R.id.txtFullname);
        txtUser = view.findViewById(R.id.txtUser);
        rcyFilmWatched = view.findViewById(R.id.rcyFilmWatched);
        imgChangeProfile = view.findViewById(R.id.imgChangeProfile);
        txtNoContent = view.findViewById(R.id.txtNoContent);
        txtNoContent.setVisibility(View.GONE);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmer();

        count = 0;
        context = this.getContext();
        fragment = this;
        this.view = view;
        listFilm = new ArrayList<>();
        listFilm.clear();
        listIdFilm = new ArrayList<>();
        listIdFilm.clear();
        listFilmWatched = new ArrayList<>();
        requestQueueUltil = RequestQueueUltil.getInstance(context);
        service = new ClientService(context);

        //get user name from view model
        viewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
        viewModel.getUsername().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                txtUser.setText(s);
            }
        });


        viewModel.getProfile().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                profile = new Gson().fromJson(s, Profile.class);
                txtFullname.setText(profile.getFistName() + " " + profile.getLastName());

                if(profile.getImage().equals("null"))
                {
                    String urlImg = APIConnectorUltils.HOST_STORAGE_IMAGE + "saitama.png";
                    BitmapUltils.loadCircleImageInto(context, urlImg, imgAva);
                }
                else {
                    String urlImg = APIConnectorUltils.HOST_STORAGE_IMAGE + profile.getImage();
                    BitmapUltils.loadCircleImageInto(context, urlImg, imgAva);
                }

                Log.d("Mine update image", "onChanged: Update image imgAva");
            }
        });

        //load infor of account
        loadInforAccount();

        //get id film watched before load infor of film into view
        // then
        //load infor of film watched by id
        getIdFilm();

        loadInforOfFilmById();
    }


    private void loadInforAccount()
    {
        service.getInfoProfile(viewModel.getUsername().getValue(), new TaskDone() {
            @Override
            public void done(String result) {
                viewModel.setProfile(result);
            }
        });
    }

    private void getIdFilm()
    {
        service.getIdFilmWatched(viewModel.getUsername().getValue(), new TaskDone() {
            @Override
            public void done(String result) {
                FilmWatched[] filmWatcheds = new Gson().fromJson(result, FilmWatched[].class);
                for(int i=0; i<filmWatcheds.length; i++)
                {
                    listIdFilm.add(filmWatcheds[i].getIdFilm());
                    listFilmWatched.add(filmWatcheds[i]);
                }
                if(listIdFilm.size()>0){
                    loadInforOfFilmById();
                }
                else {
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    txtNoContent.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void loadInforOfFilmById()
    {
        for (String id : listIdFilm) {
            service.getFilmById(id, new TaskDone() {
                @Override
                public void done(String result) {
                    Film film = null;
                    film = new Gson().fromJson(result, Film.class);
                    listFilm.add(film);
//                    Log.e("Mine listFilm", "done: "+listFilm.size() );
                    if(adapter == null)
                    {
                        count++;
                        adapter = new FilmProfileAdapter(listFilm, listFilmWatched, fragment);
                        rcyFilmWatched.setAdapter(adapter);
                        rcyFilmWatched.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    }
                    else {
                        adapter.notifyItemInserted(listFilm.size());
                        count++;
                    }

                    if (count == listIdFilm.size()) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        count = 0;
//                        Log.d("Mine films", count + "");
                    }
                }
            });
        }
    }

}
