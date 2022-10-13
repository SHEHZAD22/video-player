package com.shehzad.gifsvideo.allFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.shehzad.gifsvideo.R;
import com.shehzad.gifsvideo.adapter.MyAdapter;
import com.shehzad.gifsvideo.api.Retro;
import com.shehzad.gifsvideo.model.GifsModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView recyclerView;
    MyAdapter adapter;
    ArrayList<GifsModel> gifsList = new ArrayList<>();
    ProgressDialog dialog;
    SwipeRefreshLayout refreshLayout;
    View view;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        setHasOptionsMenu(true);

        //recycler view
        recyclerView = view.findViewById(R.id.homeRecView);

        //refreshing the screen
        refreshLayout = view.findViewById(R.id.refreshHome);
        refreshLayout.setOnRefreshListener(this);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.dialog_msg));

        fetchVideos();

        return view;
    }

    private void fetchVideos() {
        dialog.show();
        Call<List<GifsModel>> call = Retro.getRetroInstance().getApi().getFunny(1);
        call.enqueue(new Callback<List<GifsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<GifsModel>> call, @NonNull Response<List<GifsModel>> response) {
                dialog.hide();
                if (response.isSuccessful() && response.body() != null) {
                    gifsList.addAll(response.body());
                    adapter = new MyAdapter(getContext(), gifsList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GifsModel>> call, Throwable t) {
                dialog.hide();
                showErrorSnackBar();
            }
        });
    }

    //showing an error dialog box
    private void showErrorSnackBar(){
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),"Check your network connection..",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry",view -> {
            fetchVideos();
            snackbar.dismiss();
        });
        snackbar.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (adapter != null && query != null) {
                    adapter.filter(query, gifsList);
                }
                return true;
                

            }
        });
    }


    public void refreshItems(){
        //suffeling the data of arraylist and passing to adapter
        Collections.shuffle(gifsList,new Random(System.currentTimeMillis()));
        MyAdapter adapter = new MyAdapter(getContext(),gifsList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        refreshItems();
        refreshLayout.setRefreshing(false);
    }
}