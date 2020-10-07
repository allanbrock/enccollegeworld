package com.endicott.edu.models;

import com.endicott.edu.simulators.CollegeManager;

import java.lang.Math;
import java.util.ArrayList;

public class AvatarModel {
    private String avatarUrl = "";      //the url of the avatar picture
    private String accessory = "";      //The accessory the person has
    private String clothesColor = "";   //The color of the person's clothes
    private String clothes = "";        //The clothes the person is wearing
    private String eye = "";            //The eye type the person has
    private String eyebrow = "";        //The type of eyebrow the person has
    private String facialHairColor = "";//The facial hair color of the person
    private String facialHair = "";     //The type of facialHair a person has
    private String hairColor = "";      //The hair color of the person
    private String hatColor = "";       //The color of the person's hat
    private String mouth = "";          //The type of mouth the person has
    private String skinColor = "";      //The color of the person's skin
    private String top = "";            //The top that the person is wearing

    //Following are arraylists holding all the options
    private static ArrayList<String> accessories = new ArrayList();
    private static ArrayList<String> clothesColors = new ArrayList();
    private static ArrayList<String> clothesTypes = new ArrayList();
    private static ArrayList<String> eyeTypes = new ArrayList();
    private static ArrayList<String> eyebrowTypes = new ArrayList();
    private static ArrayList<String> facialHairTypes = new ArrayList();
    private static ArrayList<String> hairColors = new ArrayList();
    private static ArrayList<String> mouthTypes = new ArrayList();
    private static ArrayList<String> skinColors = new ArrayList();
    private static ArrayList<String> topTypes = new ArrayList();

    //Default constructor, assumed you will set the paramters before using
    public AvatarModel() {
        randomize();    //Change this so that it isn't totally random, depending gender/nature students look different
    }

    //Function called to set up the arraylists so that new models can be created
    public static void generateAvatarOptions() {
        addAccessories();
        addClothesColor();
        addClothesType();
        addEyebrowType();
        addEyeType();
        addMouthType();
        addSkinColor();
        addTopType();
        addHairColor();
        addFacialHairType();
    }

    public String getAvatarUrl(){ return avatarUrl;}

    public void setAvatarUrl(String s){ avatarUrl = s;}

    //Function for completely randomizing the person's icon (no specific gender or nature calculations)
    public String randomize(){
        StringBuilder sb = new StringBuilder();

        int accessoriesVal = (int)(Math.random() * 100) % accessories.size();
        accessory = accessories.get(accessoriesVal);
        sb.append("https://avataaars.io/" + "?accessoriesType=" + accessories.get(accessoriesVal));

        int clothesColorVal = (int)(Math.random() * 100) % clothesColors.size();
        clothesColor = clothesColors.get(clothesColorVal);
        sb.append("&clotheColor=" + clothesColors.get(clothesColorVal));

        sb.append("&avatarStyle=Circle"); //this adds the background color to the picture

        int clothesTypeVal = (int)(Math.random() * 100) % clothesTypes.size();
        clothes = clothesTypes.get(clothesTypeVal);
        sb.append("&clotheType=" + clothesTypes.get(clothesTypeVal));

        int eyeVal = (int)(Math.random() * 100) % eyeTypes.size();
        eye = eyeTypes.get(eyeVal);
        sb.append("&eyeType=" + eyeTypes.get(eyeVal));

        int eyeBrowVal = (int)(Math.random() * 100) % eyebrowTypes.size();
        eyebrow = eyebrowTypes.get(eyeBrowVal);
        sb.append("&eyebrowType=" + eyebrowTypes.get(eyeBrowVal));

        int hairColorVal = (int)(Math.random() * 100) % hairColors.size();
        facialHairColor = hairColors.get(hairColorVal);
        sb.append("&facialHairColor=" + hairColors.get(hairColorVal));

        int facialHairTypeVal = (int)(Math.random() * 100) % facialHairTypes.size();
        facialHair = facialHairTypes.get(facialHairTypeVal);
        sb.append("&facialHairType=" + facialHairTypes.get(facialHairTypeVal));

        hairColorVal = (int)(Math.random() * 100) % hairColors.size();
        hairColor = hairColors.get(hairColorVal);
        sb.append("&hairColor=" + hairColors.get(hairColorVal));

        clothesColorVal = (int)(Math.random() * 100) % clothesColors.size();
        hatColor = clothesColors.get(clothesColorVal);
        sb.append("&hatColor=" + clothesColors.get(clothesColorVal));

        int mouthVal = (int)(Math.random() * 100) % mouthTypes.size();
        mouth = mouthTypes.get(mouthVal);
        sb.append("&mouthType=" + mouthTypes.get(mouthVal));

        int skinVal = (int)(Math.random() * 100) % skinColors.size();
        skinColor = skinColors.get(skinVal);
        sb.append("&skinColor=" + skinColors.get(skinVal));

        int topVal = (int)(Math.random() * 100) % topTypes.size();
        top = topTypes.get(topVal);
        sb.append("&topType=" + topTypes.get(topVal));

        avatarUrl = sb.toString();
        return avatarUrl;
    }

    //FOLLOWING ARE FUNCTIONS TO ADD ALL OPTIONS TO EACH CATEGORY (CALLED ONCE, UPDATE IF ADDING MORE OPTIONS)
    public static void addAccessories(){
        accessories.add("Blank");
        accessories.add("Kurt");
        accessories.add("Prescription01");
        accessories.add("Prescription02");
        accessories.add("Round");
        accessories.add("Sunglasses");
        accessories.add("Wayfarers");
    }

    //Hat color options are the same as these
    public static void addClothesColor(){
        clothesColors.add("Black");
        clothesColors.add("Blue03");
        clothesColors.add("Grey02");
        clothesColors.add("Heather");
        clothesColors.add("PastelBlue");
        clothesColors.add("PastelGreen");
        clothesColors.add("PastelOrange");
        clothesColors.add("PastelRed");
        clothesColors.add("PastelYellow");
        clothesColors.add("Pink");
        clothesColors.add("Red");
        clothesColors.add("White");
    }

    public static void addClothesType(){
        clothesTypes.add("BlazerShirt");
        clothesTypes.add("BlazerSweater");
        clothesTypes.add("CollarSweater");
        clothesTypes.add("GraphicShirt");
        clothesTypes.add("Hoodie");
        clothesTypes.add("Overall");
        clothesTypes.add("ShirtCrewNeck");
        clothesTypes.add("ShirtScoopNeck");
        clothesTypes.add("ShirtVNeck");
    }

    public static void addEyeType(){
        eyeTypes.add("Default");
        eyeTypes.add("Close");
        eyeTypes.add("Cry");
        eyeTypes.add("Dizzy");
        eyeTypes.add("EyeRoll");
        eyeTypes.add("Happy");
        eyeTypes.add("Hearts");
        eyeTypes.add("Side");
        eyeTypes.add("Squint");
        eyeTypes.add("Surprised");
        eyeTypes.add("Wink");
        eyeTypes.add("WinkWacky");
    }

    public static void addEyebrowType(){
        eyebrowTypes.add("Default");
        eyebrowTypes.add("Angry");
        eyebrowTypes.add("RaisedExcited");
        eyebrowTypes.add("SadConcerned");
        eyebrowTypes.add("UnibrowNatural");
        eyebrowTypes.add("UpDown");
    }

    public static void addFacialHairType(){
        facialHairTypes.add("Blank");
        facialHairTypes.add("BeardMedium");
        facialHairTypes.add("BeardLight");
        facialHairTypes.add("BeardMajestic");
        facialHairTypes.add("MoustacheFancy");
        facialHairTypes.add("MoustacheMagnum");
    }

    //Color for both facial hair and hair
    public static void addHairColor(){
        hairColors.add("Auburn");
        hairColors.add("Black");
        hairColors.add("Blonde");
        hairColors.add("BlondeGolden");
        hairColors.add("Brown");
        hairColors.add("BrownDark");
        hairColors.add("PastelPink");
        hairColors.add("Platinum");
        hairColors.add("Red");
        hairColors.add("SilverGray");
    }

    public static void addMouthType(){
        mouthTypes.add("Default");
        mouthTypes.add("Concerned");
        mouthTypes.add("Disbelief");
        mouthTypes.add("Eating");
        mouthTypes.add("Grimace");
        mouthTypes.add("Sad");
        mouthTypes.add("ScreamOpen");
        mouthTypes.add("Serious");
        mouthTypes.add("Smile");
        mouthTypes.add("Tongue");
        mouthTypes.add("Twinkle");
        mouthTypes.add("Vomit");
    }

    public static void addSkinColor(){
        skinColors.add("Tanned");
        skinColors.add("Yellow");
        skinColors.add("Pale");
        skinColors.add("Light");
        skinColors.add("Brown");
        skinColors.add("DarkBrown");
        skinColors.add("Black");
    }

    public static void addTopType(){
        topTypes.add("NoHair");
        topTypes.add("EyePatch");
        topTypes.add("Hat");
        topTypes.add("Hijab");
        topTypes.add("Turban");
        topTypes.add("WinterHat2");
        topTypes.add("LongHairBigHair");
        topTypes.add("LongHairBob");
        topTypes.add("LongHairBun");
        topTypes.add("LongHairCurly");
        topTypes.add("LongHairCurvy");
        topTypes.add("LongHairDreads");
        topTypes.add("LongHairFrida");
        topTypes.add("LongHairFro");
        topTypes.add("LongHairFroBand");
        topTypes.add("LongHairNotTooLong");
        topTypes.add("LongHairShavedSides");
        topTypes.add("LongHairMiaWallace");
        topTypes.add("LongHairStraight2");
        topTypes.add("LongHairStraightStrand");
        topTypes.add("ShortHairDreads01");
        topTypes.add("ShortHairDreads02");
        topTypes.add("ShortHairFrizzle");
        topTypes.add("ShortHairShaggyMullet");
        topTypes.add("ShortHairShortCurly");
        topTypes.add("ShortHairShortFlat");
        topTypes.add("ShortHairShortRound");
        topTypes.add("ShortHairShortWaved");
        topTypes.add("ShortHairSides");
        topTypes.add("ShortHairTheCaesar");
        topTypes.add("ShortHairTheCaesarSidePart");
    }
}
