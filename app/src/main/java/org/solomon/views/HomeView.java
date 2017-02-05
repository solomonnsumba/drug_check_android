package org.solomon.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vistrav.pop.Pop;

import org.solomon.apiservice.DrugService;
import org.solomon.pharmascan.PharmActivity;
import org.solomon.pharmascan.R;
import org.solomon.pharmascan.ScannerView;
import org.solomon.storage.models.Drug;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class HomeView extends Fragment {
    private RestAdapter restAdapter;
    private DrugService drugService;
    private Realm realm;
    private static int REQUEST_CODE = 1111;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle bundle){
        View view = inflater.inflate(R.layout.home_view_layout, root, false);
        ButterKnife.bind(this, view);
        restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.server))
                .setLogLevel(RestAdapter.LogLevel.FULL).build();
        drugService = restAdapter.create(DrugService.class);
        realm = Realm.getDefaultInstance();
        return view;
    }


    @OnClick(R.id.update_btn)
    void updateInformation(){
        final AlertDialog progress = new SpotsDialog(getContext(), "Updating drug information ...");
        drugService.getDrugs(new Callback<List<Drug>>() {
            @Override
            public void success(final List<Drug> drugs, Response response) {
                if(progress.isShowing()){
                    progress.dismiss();
                }
                try{
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(drugs);
                    realm.commitTransaction();
                }catch (NullPointerException e){
                    e.printStackTrace();
                    realm.cancelTransaction();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(progress.isShowing()){
                    progress.dismiss();
                }
                Pop.on(getActivity()).with().when(R.string.ok, new Pop.Yah() {
                    @Override
                    public void clicked(DialogInterface dialog, @Nullable View view) {
                        updateInformation();
                    }
                }).when(R.string.cancel, new Pop.Nah() {
                    @Override
                    public void clicked(DialogInterface dialog, @Nullable View view) {
                        Toast.makeText(getContext(), " Update Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).cancelable(false).body(error.getLocalizedMessage()).show();
            }
        });
    }

    @OnClick(R.id.scan_btn)
    void scanProduct(){
        Intent intent = new Intent((PharmActivity)getActivity(), ScannerView.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Well , i guess you can do whatever you want here , i have no ideas but this
        if(resultCode == Activity.RESULT_OK){
            try {
                if(data.hasExtra("qr_result") && data.getStringExtra("qr_format").contains("QR_CODE")){
                    Bundle bundle = new Bundle();
                    bundle.putString("drug_code", data.getStringExtra("qr_result"));
                    DrugDetailView detailView = new DrugDetailView();
                    detailView.setArguments(bundle);
                    ((PharmActivity) getActivity()).shiftFragment(detailView);
                }else {
                    Toast.makeText(getActivity(), "Invalid QR CODE FORMAT", Toast.LENGTH_SHORT).show();
                }

            }catch (NullPointerException e){
                Toast.makeText(getActivity(), "Invalid QR CODE FORMAT", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(realm != null && !realm.isClosed()){
            realm.close();
            realm = null;
        }
    }

}
