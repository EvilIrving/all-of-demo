package com.ygkj.water.project.strategy;

import java.util.Collection;

public interface Strategy {

    String type();

    Collection records(String codes);
}
