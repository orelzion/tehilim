package com.karriapps.tehilim.tehilimlibrary.generators;

import android.content.Context;

import com.karriapps.tehilim.tehilimlibrary.model.Perek;

import java.util.List;
import java.util.Map;

public interface ITehilimGenerator {
    public Map<String, Perek> generate(Context context);

    public Map<String, Perek> getList();

    public List<String> getKeys();
}
