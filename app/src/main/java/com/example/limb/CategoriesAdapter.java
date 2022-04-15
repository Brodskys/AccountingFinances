package com.example.limb;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
public class CategoriesAdapter extends ArrayAdapter<Categories> {
    private LayoutInflater inflater;
    private int layout;
    private List<Categories> categories;
    public CategoriesAdapter(Context context, int resource, List<Categories> categories) {
        super(context, resource, categories);
        this.categories = categories;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(this.layout, parent, false);
        ImageView flagView = (ImageView) view.findViewById(R.id.ctg_img);
        TextView nameView = (TextView) view.findViewById(R.id.name);
        Categories categor = categories.get(position);
        flagView.setImageResource(categor.getFlagResource());
        nameView.setText(categor.getName());
        return view;
    }
}
