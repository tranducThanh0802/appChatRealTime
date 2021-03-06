package com.example.appchatrealtime.UI.listFriendScreen;

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

import com.example.appchatrealtime.UI.bottomScreen.BottomViewModel;
import com.example.appchatrealtime.UI.tabLayoutScreen.ListFriendViewModel;
import com.example.appchatrealtime.UI.Base.ChooseMessageListerner;
import com.example.appchatrealtime.R;
import com.example.appchatrealtime.databinding.ListFriendFragmentBinding;
import com.example.appchatrealtime.service.model.ListFriend;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListFriendFragment extends Fragment {
    StikyHeaderAdapter stickyListHeadersAdapter;

    public static ListFriendFragment newInstance() {

        Bundle args = new Bundle();

        ListFriendFragment fragment = new ListFriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ListFriendFragmentBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.list_friend_fragment, container, false);
        View view = binding.getRoot();
        ListFriendViewModel listFriendViewModel = new ViewModelProvider(getActivity()).get(ListFriendViewModel.class);
        binding.setLifecycleOwner(getActivity());
        BottomViewModel bottomViewModel = new ViewModelProvider(getActivity()).get(BottomViewModel.class);
        binding.setViewmodel1(bottomViewModel);
        binding.setViewmodel(listFriendViewModel);
        listFriendViewModel.getListMutableLiveData(getActivity()).observe(getActivity(), new Observer<ArrayList<ListFriend>>() {
            @Override
            public void onChanged(ArrayList<ListFriend> listFriendViewModels) {
                stickyListHeadersAdapter = new StikyHeaderAdapter(listFriendViewModels);
                binding.stickyListFriend.setAdapter(stickyListHeadersAdapter);
                stickyListHeadersAdapter.setChooseMessageListerner(new ChooseMessageListerner() {
                    @Override
                    public void id_sender(String id) {

                    }

                    @Override
                    public void find(int count) {
                        if (count == 0) {
                            binding.findErr.setVisibility(View.VISIBLE);
                        } else binding.findErr.setVisibility(View.GONE);
                    }
                });
            }
        });
        bottomViewModel.getTransitionData().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stickyListHeadersAdapter.getFilter().filter(s);


            }
        });
        return view;
    }
}
