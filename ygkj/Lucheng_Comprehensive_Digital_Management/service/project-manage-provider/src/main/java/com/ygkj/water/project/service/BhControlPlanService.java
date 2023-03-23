package com.ygkj.water.project.service;

import com.ygkj.project.model.BhControlPlan;

public interface BhControlPlanService {
    void insert(BhControlPlan plan);

    void update(BhControlPlan plan);

    Object loadPage(BhControlPlan plan);

    boolean delete(BhControlPlan plan);
}
