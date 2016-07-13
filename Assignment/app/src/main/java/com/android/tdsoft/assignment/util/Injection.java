package com.android.tdsoft.assignment.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.tdsoft.assignment.data.source.AssignmentRepository;
import com.android.tdsoft.assignment.data.source.local.LocalDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Admin on 4/29/2016.
 */
public class Injection {
    public static AssignmentRepository provideChambaRepository(@NonNull Context context) {
        return AssignmentRepository.getInstance(LocalDataSource.getInstance(context));
    }
}
