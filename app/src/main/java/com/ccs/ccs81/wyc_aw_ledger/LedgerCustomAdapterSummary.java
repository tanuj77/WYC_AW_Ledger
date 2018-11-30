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
public class LedgerCustomAdapterSummary extends RecyclerView.Adapter<LedgerCustomAdapterSummary.MyViewHolder> {

    private List<LedgerSummaryMovie> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewVoucherType, textViewVoucherNo, textViewVoucherDate, textViewBillNo,textViewBillDate,textViewNumber,textViewDate,
                textViewDebitAmount,textViewCreditAmount,textViewClosingBalance,textViewOpeningBalance,textViewVcDescription;

        public MyViewHolder(View view) {
            super(view);
            textViewVoucherType = (TextView) view.findViewById(R.id.tv_vouchertype) ;
            textViewVoucherNo = (TextView) view.findViewById(R.id.tv_vouchernumber);
            textViewVoucherDate = (TextView) view.findViewById(R.id.tv_voucherdate);
            textViewDebitAmount = (TextView) view.findViewById(R.id.tv_debitamount);
            textViewCreditAmount = (TextView) view.findViewById(R.id.tv_creditamount);

        }
    }


    public LedgerCustomAdapterSummary(List<LedgerSummaryMovie> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledgersummary_list_row, parent, false);

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

        LedgerSummaryMovie detailMovie = moviesList.get(position);
        holder.textViewVoucherType.setText(detailMovie.getVc_Type());
        holder.textViewVoucherNo.setText(detailMovie.getVc_BlivNo());
        holder.textViewVoucherDate.setText(detailMovie.getVc_VouchDate());
        holder.textViewDebitAmount.setText(detailMovie.getVc_DrAmt());
        holder.textViewCreditAmount.setText(detailMovie.getVc_CrAmt());


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}