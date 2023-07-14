package com.example.cvm_mobile_application.ui.org.info;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.SpinnerOption;
import com.example.cvm_mobile_application.data.db.model.Organization;
import com.example.cvm_mobile_application.data.helpers.DVHCHelper;
import com.example.cvm_mobile_application.ui.SpinnerAdapter;
import com.example.cvm_mobile_application.ui.ViewStructure;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgProfileActivity extends AppCompatActivity implements ViewStructure {
    private FirebaseFirestore db;
    private DVHCHelper dvhcHelper;
    private Organization org;

    private EditText etId;
    private EditText etName;
    private Spinner spProvince;
    private List<SpinnerOption> provinceList = new ArrayList<>();
    private SpinnerAdapter spProvinceListAdapter;
    private Spinner spDistrict;
    private List<SpinnerOption> districtList = new ArrayList<>();
    private SpinnerAdapter spDistrictListAdapter;
    private Spinner spWard;
    private List<SpinnerOption> wardList = new ArrayList<>();
    private SpinnerAdapter spWardListAdapter;
    private EditText etStreet;
    private Button btnSaveProfile;
    private Button btnBack;
    private TextView tbTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_profile);
        db = FirebaseFirestore.getInstance();
        dvhcHelper = new DVHCHelper(getApplicationContext());
    }

    protected void onStart() {
        super.onStart();
        org = getIntent().getParcelableExtra("org");

        implementView();
        bindViewData();
        setViewListener();
    }

    @Override
    public void implementView() {
        btnBack = findViewById(R.id.btn_back);
        tbTitle = findViewById(R.id.tb_title);

        etId = findViewById(R.id.et_id);
        etName = findViewById(R.id.et_name);

        dvhcHelper.setSpProvince(findViewById(R.id.sp_province));
        dvhcHelper.setSpDistrict(findViewById(R.id.sp_district));
        dvhcHelper.setSpWard(findViewById(R.id.sp_ward));

        spProvince = findViewById(R.id.sp_province);
        spDistrict = findViewById(R.id.sp_district);
        spWard = findViewById(R.id.sp_ward);
        etStreet = findViewById(R.id.et_street);

        btnSaveProfile = findViewById(R.id.btn_save_profile);
    }

    @Override
    public void bindViewData() {
        tbTitle.setText("Chỉnh sửa thông tin đơn vị");

        etId.setText(org.getId());
        etId.setEnabled(false);

        etName.setText(org.getName());

        //SET LOCAL VALUE
        try {
            dvhcHelper.bindLocalListSpinnerData(getApplicationContext(),
                    org.getProvince_name(), org.getDistrict_name(), org.getWard_name());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        etStreet.setText(org.getStreet());
    }

    @Override
    public void setViewListener() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dvhcHelper.setLocalListSpinnerListener(() -> {});

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrgProfileActivity.this.updateProfile();
            }
        });
    }

    public void spProvinceTriggeredActivities() {
        try {
            SpinnerOption provinceOption =
                    (SpinnerOption) provinceList.get(spProvince.getSelectedItemPosition());
            districtList = dvhcHelper.getLocalList(
                    DVHCHelper.DISTRICT_LEVEL, provinceOption.getValue());
            spDistrictListAdapter.setOptionList(districtList);
            spDistrictListAdapter.notifyDataSetChanged();

            // Changing selected province triggers district listener to change district list.
            // And when changing district list, we also need to change ward list,

            // IN CASE, THE DISTRICT SPINNER HAS NOT BEEN SELECTED
            // (SELECTED POSITION STAYS = 0)
            // THEN WHEN .setSelection(0) IS CALLED
            // IT DOES NOT TRIGGER THE SELECTION OF THE DISTRICT SPINNER
            // (THE ACTIVITY WHEN DISTRICT SPINNER IS TRIGGERED IS CHANGING THE WARD LIST)
            // SO WE NEED TO DO THE ACTIVITY OF THE DISTRICT SPINNER TRIGGER BY HAND HERE
            if (spDistrict.getSelectedItemPosition() == 0) {
//                SpinnerOption districtOption = (SpinnerOption) districtList.get(0);
//                wardList = dvhcHelper.getLocalList(
//                        DVHCHelper.WARD_LEVEL, districtOption.getValue());
//                spWardListAdapter.setOptionList(wardList);
//                spWardListAdapter.notifyDataSetChanged();

                spDistrictTriggeredActivities();
            }
            // ELSE SET SELECTION TO 0 AND TRIGGER THE DISTRICT SPINNER AUTOMATICALLY
            else {
                spDistrict.setSelection(0, true);
            }

            // MORE EXPLANATION
            // THE REASON WE NEED TO TRIGGER THESE LISTENERS IN CHAIN THAT IS
            // TO MAKE SURE ALL THESE ACTIVITIES ARE ACTIVATED COMPLETELY AND IN ORDERLY

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void spDistrictTriggeredActivities() {
        try {
            SpinnerOption districtOption = (SpinnerOption) spDistrict.getSelectedItem();
            wardList = dvhcHelper.getLocalList(
                    DVHCHelper.WARD_LEVEL, districtOption.getValue());
            spWardListAdapter.setOptionList(wardList);
            spWardListAdapter.notifyDataSetChanged();

            // TRIGGER WARD SPINNER SELECTION FOR THE NEXT ACTIVITIES

            if (spWard.getSelectedItemPosition() == 0) {
                spWardTriggeredActivities();
            } else {
                spWard.setSelection(0, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void spWardTriggeredActivities() {
        SpinnerOption provinceOption = (SpinnerOption) spProvince.getSelectedItem();
        SpinnerOption districtOption = (SpinnerOption) spDistrict.getSelectedItem();
        SpinnerOption wardOption =
                (SpinnerOption) wardList.get(spWard.getSelectedItemPosition());
    }

    public void updateProfile() {
        Organization profile = new Organization();
        profile.setId(String.valueOf(etId.getText()));
        profile.setName(String.valueOf(etName.getText()));
        if (profile.getName().equals("")) {
            Toast.makeText(this, "*Nhập tên đơn vị", Toast.LENGTH_SHORT).show();
            return;
        }

        SpinnerOption spOption = (SpinnerOption) spProvince.getSelectedItem();
        profile.setProvince_name(spOption.getOption());

        spOption = (SpinnerOption) spDistrict.getSelectedItem();
        profile.setDistrict_name(spOption.getOption());

        spOption = (SpinnerOption) spWard.getSelectedItem();
        profile.setWard_name(spOption.getOption());

        profile.setStreet(String.valueOf(etStreet.getText()));
        if (profile.getStreet().equals("")) {
            Toast.makeText(this, "*Nhập địa chỉ cụ thể", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("name", profile.getName());
        data.put("id", profile.getId());
        data.put("province_name", profile.getProvince_name());
        data.put("district_name", profile.getDistrict_name());
        data.put("ward_name", profile.getWard_name());
        data.put("street", profile.getStreet());

        db.collection("organizations")
                .document(org.getId())
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OrgProfileActivity.this,
                                    "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OrgProfileActivity.this,
                                    "Đã có lỗi xảy ra", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
