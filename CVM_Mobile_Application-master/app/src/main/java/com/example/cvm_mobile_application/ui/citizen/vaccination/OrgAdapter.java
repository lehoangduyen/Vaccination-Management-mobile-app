package com.example.cvm_mobile_application.ui.citizen.vaccination;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cvm_mobile_application.R;
import com.example.cvm_mobile_application.data.db.model.Organization;

import java.util.List;

public class OrgAdapter extends RecyclerView.Adapter<OrgAdapter.OrganizationViewHolder> {
    private final Context context;
    private List<Organization> orgList;
    private OnOrgItemClickListener listener;

    public OrgAdapter(Context context, List<Organization> orgList) {
        this.context = context;
        this.orgList = orgList;
    }

    public List<Organization> getOrgList() {
        return orgList;
    }

    public OnOrgItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnOrgItemClickListener listener) {
        this.listener = listener;
    }

    public void setOrgList (List<Organization> orgList) {
        this.orgList = orgList;
    }

    @NonNull
    @Override
    public OrganizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_org, parent, false);

        return new OrganizationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrganizationViewHolder holder, int position) {
        Organization org = orgList.get(position);
        holder.tvOrgName.setText(org.getName());

        holder.tvOrgAddress.setText(org.getStreet());
//        holder.tvOrgContact.setText(org.getContact());
//        holder.tvOrgNSchedules.setText(org.getNSchedules());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(org);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orgList.size();
    }

    public static class OrganizationViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrgId;
        TextView tvOrgName;
        TextView tvOrgAddress;
        TextView tvOrgContact;
        TextView tvOrgNSchedules;

        public OrganizationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrgId = itemView.findViewById(R.id.tv_org_id);
            tvOrgName = itemView.findViewById(R.id.tv_org_name);
            tvOrgAddress = itemView.findViewById(R.id.tv_org_address);
            tvOrgContact = itemView.findViewById(R.id.tv_org_contact);
            tvOrgNSchedules = itemView.findViewById(R.id.tv_org_n_schedules);
        }
    }
}
