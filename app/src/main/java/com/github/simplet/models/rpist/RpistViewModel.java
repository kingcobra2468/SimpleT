package com.github.simplet.models.rpist;

import com.github.simplet.utils.RpistNode;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RpistViewModel extends ViewModel {
    private final MutableLiveData<List<RpistNode>> rpistsLiveData;

    public RpistViewModel() {
        rpistsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<RpistNode>> getRpists() {
        return rpistsLiveData;
    }
}
