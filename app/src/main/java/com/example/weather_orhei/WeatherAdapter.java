package com.example.weather_orhei;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DiffUtil;

public class WeatherAdapter extends ListAdapter<WeatherItem, WeatherAdapter.WeatherViewHolder> {
    private Context context;

    public WeatherAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<WeatherItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<WeatherItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull WeatherItem oldItem, @NonNull WeatherItem newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()); // Используйте заголовок как уникальный идентификатор
        }

        @Override
        public boolean areContentsTheSame(@NonNull WeatherItem oldItem, @NonNull WeatherItem newItem) {
            return oldItem.getDescription().equals(newItem.getDescription()); // Проверяем, совпадают ли описания
        }
    };

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherItem item = getItem(position);  // Получаем объект WeatherItem
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.weatherTitle);
            description = itemView.findViewById(R.id.weatherDescription);
        }
    }
}

