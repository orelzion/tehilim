package com.karriapps.tehilim.tehilimlibrary.generators;

import com.karriapps.tehilim.tehilimlibrary.model.Perek;

import java.util.List;
import java.util.Map;

public interface ITehilimGenerator {
    public Map<String, Perek> generate();

    public Map<String, Perek> getList();

    public List<String> getKeys();
}
