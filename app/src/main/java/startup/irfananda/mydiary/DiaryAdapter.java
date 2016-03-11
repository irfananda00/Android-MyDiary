package startup.irfananda.mydiary;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>{

    private List<Diary> diaries;

    public DiaryAdapter(List<Diary> diaries) {
        this.diaries = diaries;
    }

    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_diary, parent,false);
        return new DiaryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DiaryViewHolder holder, int position) {
        holder.txt_date.setText(diaries.get(position).getDate());
        holder.txt_content.setText(diaries.get(position).getStory());
        holder.txt_category.setText(diaries.get(position).getCategory());
        if(diaries.get(position).getConclusion().equalsIgnoreCase("Good")) {
            holder.img_icon.setImageResource(R.drawable.ic_tag_faces_blue_48dp);
        }
        else if(diaries.get(position).getConclusion().equalsIgnoreCase("Bad")) {
            holder.img_icon.setImageResource(R.drawable.ic_tag_faces_grey_48dp);
        }
    }

    @Override
    public int getItemCount() {
        return diaries.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView txt_date;
        TextView txt_content;
        TextView txt_category;
        ImageView img_icon;

        public DiaryViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            txt_date = (TextView)itemView.findViewById(R.id.txt_date);
            txt_content = (TextView)itemView.findViewById(R.id.txt_content);
            txt_category = (TextView)itemView.findViewById(R.id.txt_category);
            img_icon = (ImageView)itemView.findViewById(R.id.img_icon);
        }
    }

}
