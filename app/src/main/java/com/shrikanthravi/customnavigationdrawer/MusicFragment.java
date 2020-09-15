package com.shrikanthravi.customnavigationdrawer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.R.layout.simple_list_item_1;


public class MusicFragment extends Fragment {

    // определяем строковый массив
    final String[] catNames = new String[] {
            "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька",
            "Томасина", "Кристина", "Пушок", "Дымка", "Кузя",
            "Китти", "Масяня", "Симба"
    };

    public MusicFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
    }

    private void init(View view) {
        // получаем экземпляр элемента ListView
        ListView listView = view.findViewById(R.id.list_view1);



// используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                simple_list_item_1, catNames);

        listView.setAdapter(adapter);
    }
}
