package com.karriapps.easytehilim.tehilimlibrary.utils;

import com.karriapps.easytehilim.tehilimlibrary.R;
import com.karriapps.easytehilim.tehilimlibrary.utils.App.READING_MODE;

import java.util.ArrayList;
import java.util.TreeMap;

public class PsalmsHelper {

    private TreeMap<Integer, Integer> tehilimEasy = new TreeMap<Integer, Integer>();
    private TreeMap<Integer, Integer> tehilimT = new TreeMap<Integer, Integer>();
    private TreeMap<Integer, Integer> monthPsalms = new TreeMap<Integer, Integer>();
    private TreeMap<Integer, Integer> kufYudTet = new TreeMap<Integer, Integer>();
    private TreeMap<Integer, Integer> kufYudTetT = new TreeMap<Integer, Integer>();

    public static int BOOK_TWO_START = 42;
    public static int BOOK_THREE_START = 73;
    public static int BOOK_FOUR_START = 90;
    public static int BOOK_FIVE_START = 107;

    public static int DAY_TWO_START = 30;
    public static int DAY_THREE_START = 51;
    public static int DAY_FOUR_START = 73;
    public static int DAY_FIVE_START = 90;
    public static int DAY_SIX_START = 107;
    public static int DAY_SEVEN_START = 120;

    /**
     * Get the chapter resource by its id
     *
     * @param chapter_id The chapter's ordinal Tehilim position
     * @return The resource String of this chapter
     */
    public int getChapter(int chapter_id) {
        if (App.getInstance().getReadingMode().equals(READING_MODE.SIMPLE))
            return tehilimEasy.get(chapter_id);
        else
            return tehilimT.get(chapter_id);
    }

    /**
     * Get the chapter text by its id
     *
     * @param chapter_id The chapter's ordinal Tehilim position
     * @return The String of this chapter
     */
    public String getChapterText(int chapter_id) {
        if (App.getInstance().getReadingMode().equals(READING_MODE.SIMPLE))
            return App.getInstance().getAplicationContext().getString(tehilimEasy.get(chapter_id));
        else
            return App.getInstance().getAplicationContext().getString(tehilimT.get(chapter_id));
    }

    /**
     * Get the resource of the Kuf Yud Tet part by its id
     *
     * @param part_id The part's ordinal position in chapter 119
     * @return The resource id of this part
     */
    public int getKufYudTet(int part_id) {
        if (App.getInstance().getReadingMode().equals(READING_MODE.SIMPLE))
            return kufYudTet.get(part_id);
        else
            return kufYudTetT.get(part_id);
    }

    /**
     * Get the text of the Kuf Yud Tet part by its id
     *
     * @param part_id The part's ordinal position in chapter 119
     * @return The text of this part
     */
    public String getKufYudTetText(int part_id) {
        if (App.getInstance().getReadingMode().equals(READING_MODE.SIMPLE))
            return App.getInstance().getAplicationContext().getString(kufYudTet.get(part_id));
        else
            return App.getInstance().getAplicationContext().getString(kufYudTetT.get(part_id));
    }

    public ArrayList<Integer> getListFromAtoB(int start, int end) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = start; i < end; i++) {
            list.add(i);
        }
        return list;
    }

    public ArrayList<Integer> getListForDate(int dayInMonth) {
        if (dayInMonth == 25)
            return getListFromAtoB(monthPsalms.get(dayInMonth), 120);
        else
            return getListFromAtoB(monthPsalms.get(dayInMonth), monthPsalms.get(dayInMonth + 1));
    }

    public int getMonthPsalm(int dayInMonth) {
        return monthPsalms.get(dayInMonth);
    }

    public int getMonthLastPsalm(int dayInMonth) {
        if (dayInMonth == 25)
            return 120;
        else
            return monthPsalms.get(dayInMonth + 1);
    }

    public int getMonthKufYudPsalm(int dayInMonth) {
        if (dayInMonth == 25)
            return 1;
        else if (dayInMonth == 26)
            return 13;
        else return 0;
    }

    public int getMonthLastKufYudPsalm(int dayInMonth) {
        if (dayInMonth == 25)
            return 13;
        else if (dayInMonth == 26)
            return 23;
        else return 0;
    }

    public ArrayList<Integer> getKufYudListForDate(int dayInMonth) {
        if (dayInMonth == 25)
            return getListFromAtoB(1, 13);
        else if (dayInMonth == 26)
            return getListFromAtoB(13, 23);
        else
            return new ArrayList<Integer>();
    }

    public PsalmsHelper() {
        //defins the psalms of easy reading
        tehilimEasy.put(1, R.string._1);
        tehilimEasy.put(2, R.string._2);
        tehilimEasy.put(3, R.string._3);
        tehilimEasy.put(4, R.string._4);
        tehilimEasy.put(5, R.string._5);
        tehilimEasy.put(6, R.string._6);
        tehilimEasy.put(7, R.string._7);
        tehilimEasy.put(8, R.string._8);
        tehilimEasy.put(9, R.string._9);
        tehilimEasy.put(10, R.string._10);
        tehilimEasy.put(11, R.string._11);
        tehilimEasy.put(12, R.string._12);
        tehilimEasy.put(13, R.string._13);
        tehilimEasy.put(14, R.string._14);
        tehilimEasy.put(15, R.string._15);
        tehilimEasy.put(16, R.string._16);
        tehilimEasy.put(17, R.string._17);
        tehilimEasy.put(18, R.string._18);
        tehilimEasy.put(19, R.string._19);
        tehilimEasy.put(20, R.string._20);
        tehilimEasy.put(21, R.string._21);
        tehilimEasy.put(22, R.string._22);
        tehilimEasy.put(23, R.string._23);
        tehilimEasy.put(24, R.string._24);
        tehilimEasy.put(25, R.string._25);
        tehilimEasy.put(26, R.string._26);
        tehilimEasy.put(27, R.string._27);
        tehilimEasy.put(28, R.string._28);
        tehilimEasy.put(29, R.string._29);
        tehilimEasy.put(30, R.string._30);
        tehilimEasy.put(31, R.string._31);
        tehilimEasy.put(32, R.string._32);
        tehilimEasy.put(33, R.string._33);
        tehilimEasy.put(34, R.string._34);
        tehilimEasy.put(35, R.string._35);
        tehilimEasy.put(36, R.string._36);
        tehilimEasy.put(37, R.string._37);
        tehilimEasy.put(38, R.string._38);
        tehilimEasy.put(39, R.string._39);
        tehilimEasy.put(40, R.string._40);
        tehilimEasy.put(41, R.string._41);
        tehilimEasy.put(42, R.string._42);
        tehilimEasy.put(43, R.string._43);
        tehilimEasy.put(44, R.string._44);
        tehilimEasy.put(45, R.string._45);
        tehilimEasy.put(46, R.string._46);
        tehilimEasy.put(47, R.string._47);
        tehilimEasy.put(48, R.string._48);
        tehilimEasy.put(49, R.string._49);
        tehilimEasy.put(50, R.string._50);
        tehilimEasy.put(51, R.string._51);
        tehilimEasy.put(52, R.string._52);
        tehilimEasy.put(53, R.string._53);
        tehilimEasy.put(54, R.string._54);
        tehilimEasy.put(55, R.string._55);
        tehilimEasy.put(56, R.string._56);
        tehilimEasy.put(57, R.string._57);
        tehilimEasy.put(58, R.string._58);
        tehilimEasy.put(59, R.string._59);
        tehilimEasy.put(60, R.string._60);
        tehilimEasy.put(61, R.string._61);
        tehilimEasy.put(62, R.string._62);
        tehilimEasy.put(63, R.string._63);
        tehilimEasy.put(64, R.string._64);
        tehilimEasy.put(65, R.string._65);
        tehilimEasy.put(66, R.string._66);
        tehilimEasy.put(67, R.string._67);
        tehilimEasy.put(68, R.string._68);
        tehilimEasy.put(69, R.string._69);
        tehilimEasy.put(70, R.string._70);
        tehilimEasy.put(71, R.string._71);
        tehilimEasy.put(72, R.string._72);
        tehilimEasy.put(73, R.string._73);
        tehilimEasy.put(74, R.string._74);
        tehilimEasy.put(75, R.string._75);
        tehilimEasy.put(76, R.string._76);
        tehilimEasy.put(77, R.string._77);
        tehilimEasy.put(78, R.string._78);
        tehilimEasy.put(79, R.string._79);
        tehilimEasy.put(80, R.string._80);
        tehilimEasy.put(81, R.string._81);
        tehilimEasy.put(82, R.string._82);
        tehilimEasy.put(83, R.string._83);
        tehilimEasy.put(84, R.string._84);
        tehilimEasy.put(85, R.string._85);
        tehilimEasy.put(86, R.string._86);
        tehilimEasy.put(87, R.string._87);
        tehilimEasy.put(88, R.string._88);
        tehilimEasy.put(89, R.string._89);
        tehilimEasy.put(90, R.string._90);
        tehilimEasy.put(91, R.string._91);
        tehilimEasy.put(92, R.string._92);
        tehilimEasy.put(93, R.string._93);
        tehilimEasy.put(94, R.string._94);
        tehilimEasy.put(95, R.string._95);
        tehilimEasy.put(96, R.string._96);
        tehilimEasy.put(97, R.string._97);
        tehilimEasy.put(98, R.string._98);
        tehilimEasy.put(99, R.string._99);
        tehilimEasy.put(100, R.string._100);
        tehilimEasy.put(101, R.string._101);
        tehilimEasy.put(102, R.string._102);
        tehilimEasy.put(103, R.string._103);
        tehilimEasy.put(104, R.string._104);
        tehilimEasy.put(105, R.string._105);
        tehilimEasy.put(106, R.string._106);
        tehilimEasy.put(107, R.string._107);
        tehilimEasy.put(108, R.string._108);
        tehilimEasy.put(109, R.string._109);
        tehilimEasy.put(110, R.string._110);
        tehilimEasy.put(111, R.string._111);
        tehilimEasy.put(112, R.string._112);
        tehilimEasy.put(113, R.string._113);
        tehilimEasy.put(114, R.string._114);
        tehilimEasy.put(115, R.string._115);
        tehilimEasy.put(116, R.string._116);
        tehilimEasy.put(117, R.string._117);
        tehilimEasy.put(118, R.string._118);
        tehilimEasy.put(120, R.string._120);
        tehilimEasy.put(121, R.string._121);
        tehilimEasy.put(122, R.string._122);
        tehilimEasy.put(123, R.string._123);
        tehilimEasy.put(124, R.string._124);
        tehilimEasy.put(125, R.string._125);
        tehilimEasy.put(126, R.string._126);
        tehilimEasy.put(127, R.string._127);
        tehilimEasy.put(128, R.string._128);
        tehilimEasy.put(129, R.string._129);
        tehilimEasy.put(130, R.string._130);
        tehilimEasy.put(131, R.string._131);
        tehilimEasy.put(132, R.string._132);
        tehilimEasy.put(133, R.string._133);
        tehilimEasy.put(134, R.string._134);
        tehilimEasy.put(135, R.string._135);
        tehilimEasy.put(136, R.string._136);
        tehilimEasy.put(137, R.string._137);
        tehilimEasy.put(138, R.string._138);
        tehilimEasy.put(139, R.string._139);
        tehilimEasy.put(140, R.string._140);
        tehilimEasy.put(141, R.string._141);
        tehilimEasy.put(142, R.string._142);
        tehilimEasy.put(143, R.string._143);
        tehilimEasy.put(144, R.string._144);
        tehilimEasy.put(145, R.string._145);
        tehilimEasy.put(146, R.string._146);
        tehilimEasy.put(147, R.string._147);
        tehilimEasy.put(148, R.string._148);
        tehilimEasy.put(149, R.string._149);
        tehilimEasy.put(150, R.string._150);

        tehilimT.put(1, R.string._1t);
        tehilimT.put(2, R.string._2t);
        tehilimT.put(3, R.string._3t);
        tehilimT.put(4, R.string._4t);
        tehilimT.put(5, R.string._5t);
        tehilimT.put(6, R.string._6t);
        tehilimT.put(7, R.string._7t);
        tehilimT.put(8, R.string._8t);
        tehilimT.put(9, R.string._9t);
        tehilimT.put(10, R.string._10t);
        tehilimT.put(11, R.string._11t);
        tehilimT.put(12, R.string._12t);
        tehilimT.put(13, R.string._13t);
        tehilimT.put(14, R.string._14t);
        tehilimT.put(15, R.string._15t);
        tehilimT.put(16, R.string._16t);
        tehilimT.put(17, R.string._17t);
        tehilimT.put(18, R.string._18t);
        tehilimT.put(19, R.string._19t);
        tehilimT.put(20, R.string._20t);
        tehilimT.put(21, R.string._21t);
        tehilimT.put(22, R.string._22t);
        tehilimT.put(23, R.string._23t);
        tehilimT.put(24, R.string._24t);
        tehilimT.put(25, R.string._25t);
        tehilimT.put(26, R.string._26t);
        tehilimT.put(27, R.string._27t);
        tehilimT.put(28, R.string._28t);
        tehilimT.put(29, R.string._29t);
        tehilimT.put(30, R.string._30t);
        tehilimT.put(31, R.string._31t);
        tehilimT.put(32, R.string._32t);
        tehilimT.put(33, R.string._33t);
        tehilimT.put(34, R.string._34t);
        tehilimT.put(35, R.string._35t);
        tehilimT.put(36, R.string._36t);
        tehilimT.put(37, R.string._37t);
        tehilimT.put(38, R.string._38t);
        tehilimT.put(39, R.string._39t);
        tehilimT.put(40, R.string._40t);
        tehilimT.put(41, R.string._41t);
        tehilimT.put(42, R.string._42t);
        tehilimT.put(43, R.string._43t);
        tehilimT.put(44, R.string._44t);
        tehilimT.put(45, R.string._45t);
        tehilimT.put(46, R.string._46t);
        tehilimT.put(47, R.string._47t);
        tehilimT.put(48, R.string._48t);
        tehilimT.put(49, R.string._49t);
        tehilimT.put(50, R.string._50t);
        tehilimT.put(51, R.string._51t);
        tehilimT.put(52, R.string._52t);
        tehilimT.put(53, R.string._53t);
        tehilimT.put(54, R.string._54t);
        tehilimT.put(55, R.string._55t);
        tehilimT.put(56, R.string._56t);
        tehilimT.put(57, R.string._57t);
        tehilimT.put(58, R.string._58t);
        tehilimT.put(59, R.string._59t);
        tehilimT.put(60, R.string._60t);
        tehilimT.put(61, R.string._61t);
        tehilimT.put(62, R.string._62t);
        tehilimT.put(63, R.string._63t);
        tehilimT.put(64, R.string._64t);
        tehilimT.put(65, R.string._65t);
        tehilimT.put(66, R.string._66t);
        tehilimT.put(67, R.string._67t);
        tehilimT.put(68, R.string._68t);
        tehilimT.put(69, R.string._69t);
        tehilimT.put(70, R.string._70t);
        tehilimT.put(71, R.string._71t);
        tehilimT.put(72, R.string._72t);
        tehilimT.put(73, R.string._73t);
        tehilimT.put(74, R.string._74t);
        tehilimT.put(75, R.string._75t);
        tehilimT.put(76, R.string._76t);
        tehilimT.put(77, R.string._77t);
        tehilimT.put(78, R.string._78t);
        tehilimT.put(79, R.string._79t);
        tehilimT.put(80, R.string._80t);
        tehilimT.put(81, R.string._81t);
        tehilimT.put(82, R.string._82t);
        tehilimT.put(83, R.string._83t);
        tehilimT.put(84, R.string._84t);
        tehilimT.put(85, R.string._85t);
        tehilimT.put(86, R.string._86t);
        tehilimT.put(87, R.string._87t);
        tehilimT.put(88, R.string._88t);
        tehilimT.put(89, R.string._89t);
        tehilimT.put(90, R.string._90t);
        tehilimT.put(91, R.string._91t);
        tehilimT.put(92, R.string._92t);
        tehilimT.put(93, R.string._93t);
        tehilimT.put(94, R.string._94t);
        tehilimT.put(95, R.string._95t);
        tehilimT.put(96, R.string._96t);
        tehilimT.put(97, R.string._97t);
        tehilimT.put(98, R.string._98t);
        tehilimT.put(99, R.string._99t);
        tehilimT.put(100, R.string._100t);
        tehilimT.put(101, R.string._101t);
        tehilimT.put(102, R.string._102t);
        tehilimT.put(103, R.string._103t);
        tehilimT.put(104, R.string._104t);
        tehilimT.put(105, R.string._105t);
        tehilimT.put(106, R.string._106t);
        tehilimT.put(107, R.string._107t);
        tehilimT.put(108, R.string._108t);
        tehilimT.put(109, R.string._109t);
        tehilimT.put(110, R.string._110t);
        tehilimT.put(111, R.string._111t);
        tehilimT.put(112, R.string._112t);
        tehilimT.put(113, R.string._113t);
        tehilimT.put(114, R.string._114t);
        tehilimT.put(115, R.string._115t);
        tehilimT.put(116, R.string._116t);
        tehilimT.put(117, R.string._117t);
        tehilimT.put(118, R.string._118t);
        tehilimT.put(120, R.string._120t);
        tehilimT.put(121, R.string._121t);
        tehilimT.put(122, R.string._122t);
        tehilimT.put(123, R.string._123t);
        tehilimT.put(124, R.string._124t);
        tehilimT.put(125, R.string._125t);
        tehilimT.put(126, R.string._126t);
        tehilimT.put(127, R.string._127t);
        tehilimT.put(128, R.string._128t);
        tehilimT.put(129, R.string._129t);
        tehilimT.put(130, R.string._130t);
        tehilimT.put(131, R.string._131t);
        tehilimT.put(132, R.string._132t);
        tehilimT.put(133, R.string._133t);
        tehilimT.put(134, R.string._134t);
        tehilimT.put(135, R.string._135t);
        tehilimT.put(136, R.string._136t);
        tehilimT.put(137, R.string._137t);
        tehilimT.put(138, R.string._138t);
        tehilimT.put(139, R.string._139t);
        tehilimT.put(140, R.string._140t);
        tehilimT.put(141, R.string._141t);
        tehilimT.put(142, R.string._142t);
        tehilimT.put(143, R.string._143t);
        tehilimT.put(144, R.string._144t);
        tehilimT.put(145, R.string._145t);
        tehilimT.put(146, R.string._146t);
        tehilimT.put(147, R.string._147t);
        tehilimT.put(148, R.string._148t);
        tehilimT.put(149, R.string._149t);
        tehilimT.put(150, R.string._150t);

        monthPsalms.put(1, 1);
        monthPsalms.put(2, 10);
        monthPsalms.put(3, 18);
        monthPsalms.put(4, 23);
        monthPsalms.put(5, 29);
        monthPsalms.put(6, 35);
        monthPsalms.put(7, 39);
        monthPsalms.put(8, 44);
        monthPsalms.put(9, 49);
        monthPsalms.put(10, 55);
        monthPsalms.put(11, 61);
        monthPsalms.put(12, 66);
        monthPsalms.put(13, 69);
        monthPsalms.put(14, 72);
        monthPsalms.put(15, 77);
        monthPsalms.put(16, 79);
        monthPsalms.put(17, 83);
        monthPsalms.put(18, 88);
        monthPsalms.put(19, 90);
        monthPsalms.put(20, 97);
        monthPsalms.put(21, 104);
        monthPsalms.put(22, 106);
        monthPsalms.put(23, 108);
        monthPsalms.put(24, 113);
        monthPsalms.put(25, 119);
        monthPsalms.put(26, 119);
        monthPsalms.put(27, 120);
        monthPsalms.put(28, 135);
        monthPsalms.put(29, 140);
        monthPsalms.put(30, 145);
        monthPsalms.put(31, 150);

        kufYudTet.put(1, R.string._119_1);
        kufYudTet.put(2, R.string._119_2);
        kufYudTet.put(3, R.string._119_3);
        kufYudTet.put(4, R.string._119_4);
        kufYudTet.put(5, R.string._119_5);
        kufYudTet.put(6, R.string._119_6);
        kufYudTet.put(7, R.string._119_7);
        kufYudTet.put(8, R.string._119_8);
        kufYudTet.put(9, R.string._119_9);
        kufYudTet.put(10, R.string._119_10);
        kufYudTet.put(11, R.string._119_11);
        kufYudTet.put(12, R.string._119_12);
        kufYudTet.put(13, R.string._119_13);
        kufYudTet.put(14, R.string._119_14);
        kufYudTet.put(15, R.string._119_15);
        kufYudTet.put(16, R.string._119_16);
        kufYudTet.put(17, R.string._119_17);
        kufYudTet.put(18, R.string._119_18);
        kufYudTet.put(19, R.string._119_19);
        kufYudTet.put(20, R.string._119_20);
        kufYudTet.put(21, R.string._119_21);
        kufYudTet.put(22, R.string._119_22);

        kufYudTetT.put(1, R.string._119_1t);
        kufYudTetT.put(2, R.string._119_2t);
        kufYudTetT.put(3, R.string._119_3t);
        kufYudTetT.put(4, R.string._119_4t);
        kufYudTetT.put(5, R.string._119_5t);
        kufYudTetT.put(6, R.string._119_6t);
        kufYudTetT.put(7, R.string._119_7t);
        kufYudTetT.put(8, R.string._119_8t);
        kufYudTetT.put(9, R.string._119_9t);
        kufYudTetT.put(10, R.string._119_10t);
        kufYudTetT.put(11, R.string._119_11t);
        kufYudTetT.put(12, R.string._119_12t);
        kufYudTetT.put(13, R.string._119_13t);
        kufYudTetT.put(14, R.string._119_14t);
        kufYudTetT.put(15, R.string._119_15t);
        kufYudTetT.put(16, R.string._119_16t);
        kufYudTetT.put(17, R.string._119_17t);
        kufYudTetT.put(18, R.string._119_18t);
        kufYudTetT.put(19, R.string._119_19t);
        kufYudTetT.put(20, R.string._119_20t);
        kufYudTetT.put(21, R.string._119_21t);
        kufYudTetT.put(22, R.string._119_22t);
    }
}
