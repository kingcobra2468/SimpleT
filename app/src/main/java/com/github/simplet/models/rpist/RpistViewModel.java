package com.github.simplet.models.rpist;

import com.github.simplet.utils.RpistNode;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel for holding onto the rpist node references.
 */
public class RpistViewModel extends ViewModel {
    private final MutableLiveData<List<RpistNode>> rpistsLiveData;

    /**
     * Instantiates a new rpist view model.
     */
    public RpistViewModel() {
        rpistsLiveData = new MutableLiveData<>();
    }

    /**
     * Fetches the stored rpist nodes.
     *
     * @return the rpist nodes
     */
    public MutableLiveData<List<RpistNode>> getRpists() {
        return rpistsLiveData;
    }
}
