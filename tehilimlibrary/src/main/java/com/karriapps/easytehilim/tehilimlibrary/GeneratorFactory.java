package com.karriapps.easytehilim.tehilimlibrary;

import android.content.Context;

import com.karriapps.easytehilim.tehilimlibrary.generators.PsalmsGenerator;
import com.karriapps.easytehilim.tehilimlibrary.generators.ShiraGenerator;
import com.karriapps.easytehilim.tehilimlibrary.generators.SickPrayerGenerator;
import com.karriapps.easytehilim.tehilimlibrary.generators.TehilimGenerator;
import com.karriapps.easytehilim.tehilimlibrary.generators.TikunKlaliGenerator;
import com.karriapps.easytehilim.tehilimlibrary.utils.App;

/**
 * Created by orelsara on 5/6/14.
 */
public class GeneratorFactory {

    Context context = App.getInstance();

    private GeneratorFactory() {
    }

    public TehilimGenerator getGenerator(String name) {
        if (name.equals(context.getString(R.string.shiraTitle))) {
            return new ShiraGenerator();
        } else if (name.equals(context.getString(R.string.tikunKlaliTitle))) {
            return new TikunKlaliGenerator();
        } else if (name.equals(context.getString(R.string.sickTitle))) {
            return new SickPrayerGenerator();
        }

        return getGenerator(1, 151, 1, 23);
    }

    /**
     * Generates PsalmsGenerator
     *
     * @param values values[0] = first chapter
     *               \n values[1] = second last chapter
     *               \n values[2] = first kuf yud letter
     *               \n values[3] = last kuf yud letter
     */
    public TehilimGenerator getGenerator(int... values) {
        return new PsalmsGenerator(values[0], values[1], values[2], values[3]);
    }

    public static GeneratorFactory createGeneratorFactory() {
        return new GeneratorFactory();
    }
}
