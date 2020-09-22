package com.endicott.edu.models;

import com.endicott.edu.simulators.CollegeManager;

import java.lang.Math;
import java.util.ArrayList;

public class AvatarModel {
    private String avatarUrl = ""; //the url of the avatar picture
    private static ArrayList<String> accessories;
    private static ArrayList<String> clothesColor;
    private static ArrayList<String> clothesType;
    private static ArrayList<String> eyeType;
    private static ArrayList<String> eyebrowType;
    private static ArrayList<String> facialHairType;
    private static ArrayList<String> hairColor;
    private static ArrayList<String> mouthType;
    private static ArrayList<String> skinColor;
    private static ArrayList<String> topType;

    public AvatarModel() {
        accessories = new ArrayList();
        clothesColor = new ArrayList();
        clothesType = new ArrayList();
        eyeType = new ArrayList();
        eyebrowType = new ArrayList();
        facialHairType = new ArrayList();
        hairColor = new ArrayList();
        mouthType = new ArrayList();
        skinColor = new ArrayList();
        topType = new ArrayList();

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

    public String getAvatarUrl(){
        return avatarUrl;
    }

    public void setAvatarUrl(String s){
        avatarUrl = s;
    }

    public String randomize(){
        StringBuilder sb = new StringBuilder();

        int accessoriesVal = (int)(Math.random() * 100) % accessories.size();
        sb.append("https://avataaars.io/" + "?accessoriesType=" + accessories.get(accessoriesVal));
        System.out.println(sb);

        int clothesColorVal = (int)(Math.random() * 100) % clothesColor.size();
        sb.append("&clotheColor=" + clothesColor.get(clothesColorVal));

        sb.append("&avatarStyle=Circle"); //this adds the background color to the picture

        int clothesTypeVal = (int)(Math.random() * 100) % clothesType.size();
        sb.append("&clotheType=" + clothesType.get(clothesTypeVal));

        int eyeVal = (int)(Math.random() * 100) % eyeType.size();
        sb.append("&eyeType=" + eyeType.get(eyeVal));

        int eyeBrowVal = (int)(Math.random() * 100) % eyebrowType.size();
        sb.append("&eyebrowType=" + eyebrowType.get(eyeBrowVal));

        int hairColorVal = (int)(Math.random() * 100) % hairColor.size();
        sb.append("&facialHairColor=" + hairColor.get(hairColorVal));

        int facialHairTypeVal = (int)(Math.random() * 100) % facialHairType.size();
        sb.append("&facialHairType=" + facialHairType.get(facialHairTypeVal));

        hairColorVal = (int)(Math.random() * 100) % hairColor.size();
        sb.append("&hairColor=" + hairColor.get(hairColorVal));

        clothesColorVal = (int)(Math.random() * 100) % clothesColor.size();
        sb.append("&hatColor=" + clothesColor.get(clothesColorVal));

        int mouthVal = (int)(Math.random() * 100) % mouthType.size();
        sb.append("&mouthType=" + mouthType.get(mouthVal));

        int skinVal = (int)(Math.random() * 100) % skinColor.size();
        sb.append("&skinColor=" + skinColor.get(skinVal));

        int topVal = (int)(Math.random() * 100) % topType.size();
        sb.append("&topType=" + topType.get(topVal));

        avatarUrl = sb.toString();
        System.out.println(avatarUrl);
        return avatarUrl;
    }

    public void addAccessories(){
        accessories.add("Blank");
        accessories.add("Kurt");
        accessories.add("Prescription01");
        accessories.add("Prescription02");
        accessories.add("Round");
        accessories.add("Sunglasses");
        accessories.add("Wayfarers");
    }

    //Hat color options are the same as these
    public void addClothesColor(){
        clothesColor.add("Black");
        clothesColor.add("Blue03");
        clothesColor.add("Grey02");
        clothesColor.add("Heather");
        clothesColor.add("PastelBlue");
        clothesColor.add("PastelGreen");
        clothesColor.add("PastelOrange");
        clothesColor.add("PastelRed");
        clothesColor.add("PastelYellow");
        clothesColor.add("Pink");
        clothesColor.add("Red");
        clothesColor.add("White");
    }

    public void addClothesType(){
        clothesType.add("BlazerShirt");
        clothesType.add("BlazerSweater");
        clothesType.add("CollarSweater");
        clothesType.add("GraphicShirt");
        clothesType.add("Hoodie");
        clothesType.add("Overall");
        clothesType.add("ShirtCrewNeck");
        clothesType.add("ShirtScoopNeck");
        clothesType.add("ShirtVNeck");
    }

    public void addEyeType(){
        eyeType.add("Default");
        eyeType.add("Close");
        eyeType.add("Cry");
        eyeType.add("Dizzy");
        eyeType.add("EyeRoll");
        eyeType.add("Happy");
        eyeType.add("Hearts");
        eyeType.add("Side");
        eyeType.add("Squint");
        eyeType.add("Surprise");
        eyeType.add("Wink");
        eyeType.add("WinkWacky");
    }

    public void addEyebrowType(){
        eyebrowType.add("Default");
        eyebrowType.add("Angry");
        eyebrowType.add("RaisedExcited");
        eyebrowType.add("SadConcerned");
        eyebrowType.add("UnibrowNatural");
        eyebrowType.add("UpDown");
    }

    public void addFacialHairType(){
        facialHairType.add("Blank");
        facialHairType.add("BeardMedium");
        facialHairType.add("BeardLight");
        facialHairType.add("BeardMajestic");
        facialHairType.add("MoustacheFancy");
        facialHairType.add("MoustacheMagnum");
    }

    //Color for both facial hair and hair
    public void addHairColor(){
        hairColor.add("Auburn");
        hairColor.add("Black");
        hairColor.add("Blonde");
        hairColor.add("BlondeGolden");
        hairColor.add("Brown");
        hairColor.add("BrownDark");
        hairColor.add("PastelPink");
        hairColor.add("Platinum");
        hairColor.add("Red");
        hairColor.add("SilverGray");
    }

    public void addMouthType(){
        mouthType.add("Default");
        mouthType.add("Concerned");
        mouthType.add("Disbelief");
        mouthType.add("Eating");
        mouthType.add("Grimace");
        mouthType.add("Sad");
        mouthType.add("ScreamOpen");
        mouthType.add("Serious");
        mouthType.add("Smile");
        mouthType.add("Tongue");
        mouthType.add("Twinkle");
        mouthType.add("Vomit");
    }

    public void addSkinColor(){
        skinColor.add("Tanned");
        skinColor.add("Yellow");
        skinColor.add("Pale");
        skinColor.add("Light");
        skinColor.add("Brown");
        skinColor.add("DarkBrown");
        skinColor.add("Black");
    }

    public void addTopType(){
        topType.add("NoHair");
        topType.add("EyePatch");
        topType.add("Hat");
        topType.add("Hijab");
        topType.add("Turban");
        topType.add("WinterHat2");
        topType.add("LongHairBigHair");
        topType.add("LongHairBob");
        topType.add("LongHairBun");
        topType.add("LongHairCurly");
        topType.add("LongHairCurvy");
        topType.add("LongHairDreads");
        topType.add("LongHairFrida");
        topType.add("LongHairFro");
        topType.add("LongHairFroBand");
        topType.add("LongHairNotTooLong");
        topType.add("LongHairShavedSides");
        topType.add("LongHairMiaWallace");
        topType.add("LongHairStraight2");
        topType.add("LongHairStraightStrand");
        topType.add("ShortHairDreads01");
        topType.add("ShortHairDreads02");
        topType.add("ShortHairFrizzle");
        topType.add("ShortHairShaggyMullet");
        topType.add("ShortHairShortCurly");
        topType.add("ShortHairShortFlat");
        topType.add("ShortHairShortRound");
        topType.add("ShortHairShortWaved");
        topType.add("ShortHairSides");
        topType.add("ShortHairTheCaesar");
        topType.add("ShortHairTheCaesarSidePart");
    }

}
