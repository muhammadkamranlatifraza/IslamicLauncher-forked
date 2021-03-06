package fr.usg.islamiclauncher.constants;

public interface TAGS {
    String TIME_FORMAT = "timeFormat";

    String DATABASE_NAME = "QuranData.sqlite";
    int DATABASE_VERSION = 1;

    String CALCULATION_METHOD = "calculationMethod";

    String JURISTIC_METHOD = "juristicMethods";
    String FONT_COLOR = "fontColor";

    String RECOMMEND_APP = "Check this amazing application I am using\n" +
            "\n";

    String[] SWITCHES_NAMES = {"WIFI", "Bluetooth", "Mobile Data", "Airplane", "Rotation", "Torch"};

    String TIME_ZONE = "timeZone";
    String[] TIME_ZONE_ARRAY = new String[]{"GMT-12:00", "GMT-11:00", "GMT-10:00", "GMT-09:00", "GMT-08:00", "GMT-07:00", "GMT-06:00", "GMT-05:00", "GMT-04:30", "GMT-04:00", "GMT-03:30", "GMT-03:00", "GMT-02:00", "GMT-01:00", "GMT-00:00", "GMT+01:00", "GMT+02:00", "GMT+03:00", "GMT+03:30", "GMT+04:00", "GMT+04:30", "GMT+05:00", "GMT+05:30", "GMT+05:45", "GMT+06:00", "GMT+06:30", "GMT+07:00", "GMT+08:00", "GMT+09:00", "GMT+09:30", "GMT+10:00", "GMT+10:30", "GMT+11:00", "GMT+12:00", "GMT+13:00",};
    String TIME_ZONE_DOUBLE = "timeZoneDouble";
    double[] TIME_ZONE_DOUBLE_VALUES = new double[]{-12, -11, -10, -9, -8, -7, -6, -5, -4.5, -4, -3.5, -3, -2, -1, 0, 1, 2, 3, 3.5, 4, 4.5, 5, 5.5, 5.75, 6, 6.5, 7, 8, 9, 9.5, 10, 10.5, 11, 12, 13};

    String LAT = "lat";
    String LONG = "long";

    String[] monthsNames = {"January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"};
    String[] nameOfDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thrusday", "Friday", "Saturday"};

    String APP_VERSION = "app_version";
    String LOCATION = "location";
    String FIQAH = "fiqah";
    String NAMAZ_TIME_LANGUAGE = "namaz_time_language";

    String CITY_SELECTED = "city_selected";

    String FONT_SIZE = "font_size";
    String FONT_TYPE = "font_type";
    String FONT_TYPE_CUSTOM = "font_type_custom";
    String FONT_SIZE_ARABIC = "font_size_arabic";
    String FONT_SIZE_TRANSLITRATION = "font_size_translitration";
    String FONT_SIZE_TRANSLATION = "font_size_translation";

    String AYAH_FORMAT = "ayah_format";
    String READING_SURAH = "reading_surah";

    String[] SURAH_NAMES =
            {"1. سُّورَة الفَاتِحَة",
                    "2. سُّورَة البَقَرَة",
                    "3. سُّورَة آل عِمرَان",
                    "4. سُّورَة النِّسَاء",
                    "5. سُّورَة المَائدة",
                    "6. سُّورَة الأنعَام",
                    "7. سُّورَة الأعرَاف",
                    "8. سُّورَة الأنفَال",
                    "9. سُّورَة التوبَة",
                    "10. سُّورَة يُونس",
                    "11. سُّورَة هُود",
                    "12. سُّورَة يُوسُف",
                    "13. سُّورَة الرَّعْد",
                    "14. سُّورَة إبراهِيم",
                    "15. سُّورَة الحِجْر",
                    "16. سُّورَة النَّحْل",
                    "17. سُّورَة الإسْرَاء",
                    "18. سُّورَة الكهْف",
                    "19. سُّورَة مَريَم",
                    "20. سُّورَة طه",
                    "21. سُّورَة الأنبيَاء",
                    "22. سُّورَة الحَج",
                    "23. سُّورَة المُؤمنون",
                    "24. سُّورَة النُّور",
                    "25. سُّورَة الفُرْقان",
                    "26. سُّورَة الشُّعَرَاء",
                    "27. سُّورَة النَّمْل",
                    "28. سُّورَة القَصَص",
                    "29. سُّورَة العَنكبوت",
                    "30. سُّورَة الرُّوم",
                    "31. سُّورَة لقمَان",
                    "32. سُّورَة السَّجدَة",
                    "33. سُّورَة الأحزَاب",
                    "34. سُّورَة سَبَأ",
                    "35. سُّورَة فَاطِر",
                    "36. سُّورَة يس",
                    "37. سُّورَة الصَّافات",
                    "38. سُّورَة ص",
                    "39. سُّورَة الزُّمَر",
                    "40. سُّورَة غَافِر",
                    "41. سُّورَة فُصِّلَتْ",
                    "42. سُّورَة الشُّورَى",
                    "43. سُّورَة الزُّخْرُف",
                    "44. سُّورَة الدخَان",
                    "45. سُّورَة الجَاثيَة",
                    "46. سُّورَة الأحْقاف",
                    "47. سُّورَة محَمَّد",
                    "48. سُّورَة الفَتْح",
                    "49. سُّورَة الحُجرَات",
                    "50. سُّورَة ق",
                    "51. سُّورَة الذَّاريَات",
                    "52. سُّورَة الطُّور",
                    "53. سُّورَة النَّجْم",
                    "54. سُّورَة القَمَر",
                    "55. سُّورَة الرَّحمن",
                    "56. سُّورَة الوَاقِعَة",
                    "57. سُّورَة الحَديد",
                    "58. سُّورَة المجَادلة",
                    "59. سُّورَة الحَشر",
                    "60. سُّورَة المُمتَحنَة",
                    "61. سُّورَة الصَّف",
                    "62. سُّورَة الجُمُعَة",
                    "63. سُّورَة المنَافِقون",
                    "64. سُّورَة التغَابُن",
                    "65. سُّورَة الطلَاق",
                    "66. سُّورَة التحْريم",
                    "67. سُّورَة المُلْك",
                    "68. سُّورَة القَلَم",
                    "69. سُّورَة الحَاقَّة",
                    "70. سُّورَة المعَارج",
                    "71. سُّورَة نُوح",
                    "72. سُّورَة الجِن",
                    "73. سُّورَة المُزَّمِّل",
                    "74. سُّورَة المُدَّثِّر",
                    "75. سُّورَة القِيَامَة",
                    "76. سُّورَة الإنسَان",
                    "77. سُّورَة المُرسَلات",
                    "78. سُّورَة النَّبَأ",
                    "79. سُّورَة النّازعَات",
                    "80. سُّورَة عَبَس",
                    "81. سُّورَة التَّكوير",
                    "82. سُّورَة الانفِطار",
                    "83. سُّورَة المطفِّفِين",
                    "84. سُّورَة الانْشِقَاق",
                    "85. سُّورَة البرُوج",
                    "86. سُّورَة الطَّارِق",
                    "87. سُّورَة الأَعْلى",
                    "88. سُّورَة الغَاشِية",
                    "89. سُّورَة الفَجْر",
                    "90. سُّورَة البَلَد",
                    "91. سُّورَة الشَّمْس",
                    "92. سُّورَة الليْل",
                    "93. سُّورَة الضُّحَى",
                    "94. سُّورَة الشَّرْح",
                    "95. سُّورَة التِّين",
                    "96. سُّورَة العَلَق",
                    "97. سُّورَة القَدْر",
                    "98. سُّورَة البَينَة",
                    "99. سُّورَة الزلزَلة",
                    "100. سُّورَة العَادِيات",
                    "101. سُّورَة القَارِعة",
                    "102. سُّورَة التَّكَاثر",
                    "103. سُّورَة العَصْر",
                    "104. سُّورَة الهُمَزَة",
                    "105. سُّورَة الفِيل",
                    "106. سُّورَة قُرَيْش",
                    "107. سُّورَة المَاعُون",
                    "108. سُّورَة الكَوْثَر",
                    "109. سُّورَة الكَافِرُون",
                    "110. سُّورَة النَّصر",
                    "111. سُّورَة المَسَد",
                    "112. سُّورَة الإخْلَاص",
                    "113. سُّورَة الفَلَق",
                    "114. سُّورَة النَّاس"};
    int[] SURAH_AYAH_NUMBERS = {
            1,
            9,
            296,
            497,
            674,
            795,
            961,
            1168,
            1244,
            1374,
            1484,
            1608,
            1720,
            1764,
            1817,
            1917,
            2046,
            2158,
            2269,
            2368,
            2504,
            2617,
            2696,
            2815,
            2880,
            2958,
            3186,
            3280,
            3369,
            3439,
            3500,
            3535,
            3566,
            3640,
            3695,
            3741,
            3825,
            4008,
            4097,
            4173,
            4259,
            4314,
            4368,
            4458,
            4518,
            4556,
            4592,
            4631,
            4661,
            4680,
            4726,
            4787,
            4837,
            4900,
            4956,
            5035,
            5132,
            5162,
            5185,
            5210,
            5224,
            5239,
            5251,
            5263,
            5282,
            5295,
            5308,
            5339,
            5392,
            5445,
            5490,
            5519,
            5548,
            5569,
            5626,
            5667,
            5699,
            5750,
            5791,
            5838,
            5881,
            5911,
            5931,
            5968,
            5994,
            6017,
            6035,
            6055,
            6082,
            6113,
            6134,
            6150,
            6172,
            6184,
            6192,
            6202,
            6222,
            6228,
            6237,
            6246,
            6258,
            6270,
            6279,
            6283,
            6293,
            6299,
            6304,
            6312,
            6316,
            6323,
            6327,
            6333,
            6338,
            6344,
            6351};

    String CONTACTS_FIX = "contacts_fix";
    String APP1 = "app1";
    String APP2 = "app2";
    String APP3 = "app3";
    String APP4 = "app4";
    String NAME = "name";
    String PACKAGE = "package";
    String FAVORITE = "favorite";


}
