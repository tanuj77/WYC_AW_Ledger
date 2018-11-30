package com.ccs.ccs81.wyc_aw_ledger;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Banke Bihari on 10/6/2016.
 */
public class BWOCustomAdapterSummary extends RecyclerView.Adapter<BWOCustomAdapterSummary.MyViewHolder> {

    private List<BWOSummary> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewBillNo, textViewBillDate, textViewNoOfDays,textViewBillAmnt,textViewAmntRec,textViewCnAmnt,textViewBillDue,
                textViewBalance,textViewPassenger;

        public MyViewHolder(View view) {
            super(view);
            textViewBillNo = (TextView) view.findViewById(R.id.tv_billno);
            textViewBillDate = (TextView) view.findViewById(R.id.tv_billdate);
            textViewNoOfDays = (TextView) view.findViewById(R.id.tv_noofdays);
            textViewBillAmnt = (TextView) view.findViewById(R.id.tv_billamnt);
            textViewAmntRec = (TextView) view.findViewById(R.id.tv_amntrec);
            textViewCnAmnt = (TextView) view.findViewById(R.id.tv_cnamnt);
            textViewBillDue = (TextView) view.findViewById(R.id.tv_billdue);
            textViewBalance = (TextView) view.findViewById(R.id.tv_balance);
            textViewPassenger = (TextView) view.findViewById(R.id.tv_passenger);

        }
    }


    public BWOCustomAdapterSummary(List<BWOSummary> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bwosummary_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(position % 2 == 0)
        {
            //holder.rootView.setBackgroundColor(Color.BLACK);
            holder.itemView.setBackgroundResource(R.color.LightGrey);
        }
        else
        {
            //holder.rootView.setBackgroundColor(Color.WHITE);
            holder.itemView.setBackgroundResource(R.color.White);
        }

        BWOSummary bwoSummary = moviesList.get(position);
        holder.textViewBillNo.setText(bwoSummary.getBillNo());
        holder.textViewBillDate.setText(bwoSummary.getBillDate());
        holder.textViewNoOfDays.setText(bwoSummary.getNoOfDays());
        holder.textViewBillAmnt.setText(bwoSummary.getBillAmount());
        holder.textViewAmntRec.setText(bwoSummary.getAmountRec());
        holder.textViewCnAmnt.setText(bwoSummary.getCnAmount());
        holder.textViewBillDue.setText(bwoSummary.getBillDue());
        holder.textViewBalance.setText(bwoSummary.getBalance());
        holder.textViewPassenger.setText(bwoSummary.getPassenger());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}