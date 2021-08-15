package com.example.appchatrealtime.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchatrealtime.R;
import com.example.appchatrealtime.adapter.ChatAdapter;
import com.example.appchatrealtime.adapter.bottomgalleryAdapter;
import com.example.appchatrealtime.databinding.MessageFragmentBinding;
import com.example.appchatrealtime.model.Chat;
import com.example.appchatrealtime.model.firebase;
import com.example.appchatrealtime.viewmodels.ChatViewModel;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    private static final int STORAGE_PERMISSION_CODE = 1;
    ChatAdapter chatAdapter;
    Chat chat;
    firebase fb =new firebase();
    DatabaseReference databaseReference=fb.getDatabaseReference();
    public static ChatFragment newInstance() {

        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        MessageFragmentBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.message_fragment, container, false);
        View view = binding.getRoot();
        ChatViewModel loginViewModel=new ViewModelProvider(getActivity()).get(ChatViewModel.class);
        loginViewModel.setContext(getActivity());
         chat=new Chat();
       // binding.setLifecycleOwner(getActivity());
        binding.setViewmodel(loginViewModel);

        loginViewModel.getArrayListLiveData(getActivity()).observe(getActivity(), new Observer<ArrayList<ChatViewModel>>() {
            @Override
            public void onChanged(ArrayList<ChatViewModel> chatViewModels) {
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
               chatAdapter=new ChatAdapter(chatViewModels,getActivity());
                binding.recyclerviewmessage.setAdapter(chatAdapter);
                binding.recyclerviewmessage.setLayoutManager(mLayoutManager);
                binding.recyclerviewmessage.scrollToPosition(chatViewModels.size()-1);

           }
        });

        binding.storageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatViewModel chatViewModel = new ChatViewModel();
                chatViewModel.setContext(getActivity());
                if (!chatViewModel.CheckPermission()) {
                    if (binding.recBot.getVisibility() == View.GONE) {
                        binding.storageImg.setImageResource(R.drawable.photoblue);
                        binding.recBot.setVisibility(View.VISIBLE);
                        chatViewModel.getArrayListGalleryLiveData().observe(getActivity(), new Observer<ArrayList<String>>() {
                            @Override
                            public void onChanged(ArrayList<String> strings) {
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                                bottomgalleryAdapter galleryAdapter = new bottomgalleryAdapter(strings, getActivity());
                                binding.recBot.setAdapter(galleryAdapter);
                                binding.recBot.setLayoutManager(gridLayoutManager);


                            }
                        });
                    } else {
                        binding.storageImg.setImageResource(R.drawable.photo);
                        binding.recBot.setVisibility(View.GONE);
                    }


                }
            }
        });
        loginViewModel.getLinkPhotoLiveData(getActivity()).observe(getActivity(), new Observer<Chat>() {
            @Override
            public void onChanged(Chat s) {

                Glide.with(getActivity()).load(s.getLinkphoto()).placeholder(R.drawable.personal1).into(binding.imgAvataTop);
                binding.namefull.setText(s.getStatus()+"");
            }
        });

        return view;
    }


}