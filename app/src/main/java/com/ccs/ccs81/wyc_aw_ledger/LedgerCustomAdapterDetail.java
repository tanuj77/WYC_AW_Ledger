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
public class LedgerCustomAdapterDetail extends RecyclerView.Adapter<LedgerCustomAdapterDetail.MyViewHolder> {

    private List<LedgerDetailMovie> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewVoucherType, textViewVoucherNo, textViewVoucherDate, textViewBillNo,textViewBillDate,textViewNumber,textViewDate,
                textViewDebitAmount,textViewCreditAmount,textViewClosingBalance,textViewOpeningBalance,textViewVcDescription;

        public MyViewHolder(View view) {
            super(view);
            textViewVoucherType = (TextView) view.findViewById(R.id.tv_vouchertype) ;
            textViewVoucherNo = (TextView) view.findViewById(R.id.tv_vouchernumber);
            textViewVoucherDate = (TextView) view.findViewById(R.id.tv_voucherdate);
            textViewBillNo = (TextView) view.findViewById(R.id.tv_billno);
            textViewBillDate = (TextView) view.findViewById(R.id.tv_billdate);
            textViewNumber = (TextView) view.findViewById(R.id.tv_number);
            textViewDate = (TextView) view.findViewById(R.id.tv_date);
            textViewDebitAmount = (TextView) view.findViewById(R.id.tv_debitamount);
            textViewCreditAmount = (TextView) view.findViewById(R.id.tv_creditamount);
            textViewClosingBalance = (TextView) view.findViewById(R.id.tv_closingbalance);
            textViewOpeningBalance = (TextView) view.findViewById(R.id.tv_openingbalance);
            textViewVcDescription = (TextView) view.findViewById(R.id.tv_vcdescription);
        }
    }


    public LedgerCustomAdapterDetail(List<LedgerDetailMovie> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ledgerdetail_list_row, parent, false);

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

        LedgerDetailMovie detailMovie = moviesList.get(position);

        holder.textViewVoucherType.setText(detailMovie.getVoucherType());
        holder.textViewVoucherNo.setText(detailMovie.getVoucherNumber());
        holder.textViewVoucherDate.setText(detailMovie.getVoucherDate());
        holder.textViewBillNo.setText(detailMovie.getBillNo());
        holder.textViewBillDate.setText(detailMovie.getBillDate());
        holder.textViewNumber.setText(detailMovie.getNumber());
        holder.textViewDate.setText(detailMovie.getDate());
        holder.textViewDebitAmount.setText(detailMovie.getDebitAmount());
        holder.textViewCreditAmount.setText(detailMovie.getCreditAmount());
        holder.textViewClosingBalance.setText(detailMovie.getClosingBalance());
        holder.textViewOpeningBalance.setText(detailMovie.getOpeningBalance());
        holder.textViewVcDescription.setText(detailMovie.getVcDescription());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}