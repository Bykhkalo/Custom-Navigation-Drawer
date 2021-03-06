package com.shrikanthravi.customnavigationdrawer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.R.layout.simple_list_item_1;


public class MusicFragment extends Fragment {

    // определяем строковый массив
    final String[] songNames = new String[] {
            "Dreamy Lands", "Sweet Legend", "Feel Good With Your Season", "Make Less Tense Sea", "The Time Has Come Again For Night",
            "Buenos Aires Desert", "Right Crash", "Going Chocolate", "End Of Dinner Time", "Bridge Of My Explosions",
            "Gratifying Together", "Calming Time", "Hidden Rainy Day"
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
                simple_list_item_1, songNames);



        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("tag", "onItemClick: position: " + i);
            }
        });
    }
}
