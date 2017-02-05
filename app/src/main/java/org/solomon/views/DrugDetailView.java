package org.solomon.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.solomon.pharmascan.PharmActivity;
import org.solomon.pharmascan.R;
import org.solomon.storage.models.Drug;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;


public class DrugDetailView extends Fragment {
    @Bind(R.id.thumb_nail)
    ImageView thumbNail;
    @Bind(R.id.drug_name)
    TextView drugName;
    @Bind(R.id.description)
    TextView description;
    @Bind(R.id.manufacturer)
    TextView manufacturer;
    @Bind(R.id.exp_date)
    TextView exp_date;
    private static final String DRUG_CODE = "drug_code";
    private Realm realm;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle bundle){
            View view = inflater.inflate(R.layout.drug_detail_layout, root, false);
        ButterKnife.bind(this, view);
        description.setMovementMethod(new ScrollingMovementMethod());
        realm = Realm.getDefaultInstance();
        Bundle args = getArguments();

        try {
            if(args.containsKey(DRUG_CODE)) {
                Drug drug = realm.where(Drug.class).equalTo("code", args.getString(DRUG_CODE)).findFirst();
                Log.d("DRUG", drug.toString());
                prepareDrugInfo(drug);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Invalid QR CODE FORMAT", Toast.LENGTH_SHORT).show();
        }


        return view;
    }


    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);


    }

    private void prepareDrugInfo(final Drug drug){
        drugName.setText(drug.getDrugName());
        description.setText(drug.getDescription());
        manufacturer.setText(drug.getManufacturer());
        exp_date.setText(drug.getExp_date());
        Picasso.with(getContext()).load(drug.getThumbNail())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit().into(thumbNail, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(getContext()).load(drug.getThumbNail()).centerCrop().fit().into(thumbNail);
            }
        });
    }

    @OnClick(R.id.done_btn)
    void onDone(){
        // Return to the scanner view
        ((PharmActivity)getActivity()).shiftFragment(new HomeView());

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(realm != null && !realm.isClosed()){
            realm.close();
            realm = null;
        }
    }
}
