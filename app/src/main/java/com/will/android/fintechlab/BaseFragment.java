package com.will.android.fintechlab;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class BaseFragment extends Fragment implements QueueItemReleaseListener {

    private MainActivity mainActivity;
    private String url;

    public BaseFragment(MainActivity mainActivity, String url) {
        this.mainActivity = mainActivity;
        this.url = url;
    }

    private View baseFr;
    private ImageView imageView;
    private TextView description;
    private Button nextButton;
    private Button prevButton;
    private QueueManager queueManager;
    private QueueGifItem item;
    private BaseFragment fragmentListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseFr = inflater.inflate(R.layout.base_fragment, container, false);

        description = baseFr.findViewById(R.id.textViewTest);
        imageView = baseFr.findViewById(R.id.imageView);
        nextButton = baseFr.findViewById(R.id.button_next);
        prevButton = baseFr.findViewById(R.id.button_prev);
        fragmentListener = this;

        prevButton.setEnabled(false);
        description.setMovementMethod(new ScrollingMovementMethod());

        queueManager = new QueueManager(mainActivity, url);
        queueManager.init(fragmentListener);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton.setEnabled(false);
                queueManager.next(fragmentListener);

            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queueManager.prev(fragmentListener);
            }
        });

        return baseFr;
    }

    private void updateUI() {
        prevButton.setEnabled(queueManager.getIndex() != 0);
        description.setText(R.string.loading);

        Glide.with(mainActivity)
                .load(item.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.progress_animation)
                .fitCenter()
                .error(R.drawable.ic_baseline_broken_image_24)
                .fallback(R.drawable.ic_baseline_gif_24)
                .into(imageView);

        if (item.getDesc() != null) {
            description.setText(item.getDesc());
        } else {
            description.setText("Описание отсутствует.");
        }

    }


    @Override
    public void onItemReleased(QueueGifItem queueGifItem) {
        nextButton.setEnabled(true);
        item = queueGifItem;
        if (item == null) {
            Toast toast = Toast.makeText(mainActivity,
                    "Что-то пошло не так, попробуйте снова!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else {
            updateUI();
        }
    }
}
