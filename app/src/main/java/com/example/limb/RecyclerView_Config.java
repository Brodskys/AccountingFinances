package com.example.limb;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public class RecyclerView_Config {
    private Context mContext;
    private OperationsAdapter mOperationsAdapter;
    public  void setConfig(RecyclerView recyclerView, Context context, List<Operations_add> operationsadds, List<String> keys){
        mContext =context;
        mOperationsAdapter = new OperationsAdapter(operationsadds,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mOperationsAdapter); }
    class OperationItemsView extends RecyclerView.ViewHolder{
        private TextView mOperation;
        private TextView mSumm;
        private TextView mTime;
        private TextView mCategory;
        private TextView mData;
        private TextView mDetailed;
        private TextView mGeo;
        private String key;
        public OperationItemsView(ViewGroup parent){
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.list_item2,parent,false));
            mOperation =(TextView) itemView.findViewById(R.id.name_operation_text);
            mSumm =(TextView) itemView.findViewById(R.id.summ_text);
            mTime =(TextView) itemView.findViewById(R.id.time_text);
            mCategory =(TextView) itemView.findViewById(R.id.category_text);
            mData =(TextView) itemView.findViewById(R.id.data_text);
            mDetailed =(TextView) itemView.findViewById(R.id.details);
            mGeo =(TextView) itemView.findViewById(R.id.geo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,OperationDetails.class);
                    intent.putExtra("key",key);
                    intent.putExtra("Name_operation",mOperation.getText().toString());
                    intent.putExtra("Summ",mSumm.getText().toString());
                    intent.putExtra("Categoty",mCategory.getText().toString());
                    intent.putExtra("Date",mData.getText().toString());
                    intent.putExtra("Time",mTime.getText().toString());
                    intent.putExtra("Detailed",mDetailed.getText().toString());
                    intent.putExtra("Geo",mGeo.getText().toString());
                    mContext.startActivity(intent); }}); }
        public void  bind (Operations_add operationsadd, String key){
            mOperation.setText(operationsadd.getName_operation());
            mCategory.setText(operationsadd.getCategoty());
            mDetailed.setText(operationsadd.getDetailed());
            mGeo.setText(operationsadd.getGeo());
            Date tm = operationsadd.getTime();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String str = timeFormat.format(tm);
           mTime.setText(str);
            Date tm2 = operationsadd.getDate();
            SimpleDateFormat timeFormat2 = new SimpleDateFormat("dd.MM.yyyy");
            String str2 = timeFormat2.format(tm2);
           mData.setText(str2);
            mSumm.setText(operationsadd.getSumm().toString());
            this.key=key; }}
    class OperationsAdapter extends RecyclerView.Adapter<OperationItemsView>{
        private List<Operations_add> mOperationsaddList;
        private List<String> mKeys;
        public OperationsAdapter(List<Operations_add> mOperationsaddList, List<String> mKeys) {
            this.mOperationsaddList = mOperationsaddList;
            this.mKeys = mKeys; }
        @NonNull
        @Override
        public OperationItemsView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OperationItemsView(parent); }
        @Override
        public void onBindViewHolder(@NonNull OperationItemsView  holder, int position) {
            holder.bind(mOperationsaddList.get(position), mKeys.get(position)); }
        @Override
        public int getItemCount() {
            return mOperationsaddList.size();
        }}}
