package com.ccs.ccs81.wyc_aw_ledger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ccs81 on 26/06/2017.
 */

public class BWOCustomAdapterDetail extends RecyclerView.Adapter<BWOCustomAdapterDetail.MyViewHolder> {


    private List<BWODetail> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewInvoiceNo, textViewInvoiceDate, textViewTranType, textViewReceiptNo, textViewReceiptDate, textViewChequeNo, textViewDebitAmount,
                textViewCreditAmount, textViewBalanceAmount, textViewDetailOfService,textViewNoOfDays;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewInvoiceNo = (TextView) itemView.findViewById(R.id.tv_invoiceno);
            textViewInvoiceDate = (TextView) itemView.findViewById(R.id.tv_invoicedate);
            textViewTranType = (TextView) itemView.findViewById(R.id.tv_trantype);
            textViewReceiptNo = (TextView) itemView.findViewById(R.id.tv_receiptno);
            textViewReceiptDate = (TextView) itemView.findViewById(R.id.tv_receiptdate);
            textViewNoOfDays  = (TextView) itemView.findViewById(R.id.tv_noofdays);
            textViewChequeNo = (TextView) itemView.findViewById(R.id.tv_chequeno);
            textViewCreditAmount = (TextView) itemView.findViewById(R.id.tv_creditamount);
            textViewDebitAmount = (TextView) itemView.findViewById(R.id.tv_debitamount);
            textViewBalanceAmount = (TextView) itemView.findViewById(R.id.tv_balanceamount);
            textViewDetailOfService = (TextView) itemView.findViewById(R.id.tv_detailofservice);

        }
    }

    public BWOCustomAdapterDetail(List<BWODetail> moviesList) {
        this.moviesList = moviesList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bwodetail_list_row, parent, false);

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

        BWODetail bwoDetail = moviesList.get(position);
        holder.textViewInvoiceNo.setText(bwoDetail.getInvoiceNo());
        holder.textViewInvoiceDate.setText(bwoDetail.getInvoiceDate());
        holder.textViewTranType.setText(bwoDetail.getTranType());
        holder.textViewReceiptNo.setText(bwoDetail.getReceiptNo());
        holder.textViewReceiptDate.setText(bwoDetail.getReceiptDate());
        holder.textViewNoOfDays.setText(bwoDetail.getNoOfDays());
        holder.textViewChequeNo.setText(bwoDetail.getChequeNo());
        holder.textViewCreditAmount.setText(bwoDetail.getDebitAmount());
        holder.textViewDebitAmount.setText(bwoDetail.getCreditAmount());
        holder.textViewBalanceAmount.setText(bwoDetail.getBalanceAmount());
        holder.textViewDetailOfService.setText(bwoDetail.getDetailOfService());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}
