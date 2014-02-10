package com.karriapps.tehilimlibrary.generators;

import java.util.List;
import java.util.Map;

import com.karriapps.tehilimlibrary.Perek;

public interface ITehilimGenerator {
	public Map<String, Perek> generate();
	public Map<String, Perek> getList();
	public List<String> getKeys();
}
