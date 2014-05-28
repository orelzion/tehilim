package com.karriapps.easytehilim.tehilimlibrary.generators;

import com.karriapps.easytehilim.tehilimlibrary.model.Perek;

import java.util.List;
import java.util.Map;

public interface ITehilimGenerator {
    public Map<String, Perek> generate();

    public Map<String, Perek> getList();

    public List<String> getKeys();
}
