package com.ygkj.water.project.service;

public interface StormSurgeService {
    Object forecastWaveHeight(String tideLevelJsonArray, String windSpeedJsonArray, Double fetchLength, Double averageDepth);
}

