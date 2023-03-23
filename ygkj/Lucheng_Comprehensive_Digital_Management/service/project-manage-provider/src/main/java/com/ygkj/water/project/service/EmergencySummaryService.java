package com.ygkj.water.project.service;

import com.ygkj.project.model.FloodControlAndPresentation;

public interface EmergencySummaryService {
    Object loadPage(FloodControlAndPresentation entity);

    boolean delete(FloodControlAndPresentation entity);
}
