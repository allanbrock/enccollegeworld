package com.endicott.edu.models;

import com.endicott.edu.simulators.CollegeManager;

import java.io.Serializable;
import java.lang.Math;
import java.util.HashMap;
import java.util.Map;

public class AvatarModel implements Serializable {
    private String accessory;      //The accessory the person has
    private String clothesColor;   //The color of the person's clothes
    private String clothes;        //The clothes the person is wearing
    private String eyes;           //The eye type the person has
    private String eyebrows;       //The type of eyebrow the person has
    private String facialHairColor;//The facial hair color of the person
    private String facialHair;     //The type of facialHair a person has
    private String hairColor;      //The hair color of the person
    private String hatColor;       //The color of the person's hat
    private String mouth;          //The type of mouth the person has
    private String skinColor;      //The color of the person's skin
    private String top;            //The top that the person is wearing(hair, hats, etc.)

    private String code;

    private static final String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Map<String, Integer> numStylesPerAttribute = new HashMap<String, Integer>() {
        private static final long serialVersionUID = -7794936286092854441L;

        {
        put("accessory", 7);
        put("clothesColor", 15);
        put("clothesType", 9);
        put("eyeType", 12);
        put("eyebrowType", 12);
        put("facialHairColor", 8);
        put("facialHairType", 6);
        put("graphicType", 11);
        put("hairColor", 11);
        put("mouthType", 12);
        put("skinColor", 7);
        put("topType", 35);
    }};


    //Following are arrays holding all the options
    private static String[] accessories = {"Blank", "Kurt", "Prescription01", "Prescription02", "Round", "Sunglasses", "Wayfarers"};
    private static String[] clothesColors = {"Black", "Blue03", "Grey02", "Heather", "PastelBlue", "PastelGreen", "PastelOrange",
                                            "PastelRed", "PastelYellow", "Pink", "Red", "White"};
    private static String[] clothesTypes = {"BlazerShirt", "BlazerSweater", "CollarSweater", "GraphicShirt", "Hoodie", "Overall",
                                            "ShirtCrewNeck", "ShirtScoopNeck", "ShirtVNeck"};
    private static String[] hairColors = {"Auburn", "Black", "Blonde", "BlondeGolden", "Brown", "BrownDark", "PastelPink", "Platinum",
                                            "Red", "SilverGray"};
    private static String[] skinColors = {"Tanned", "Yellow", "Pale", "Light", "Brown", "DarkBrown", "Black"};
    private static String[] femaleTopTypes = {"LongHairBigHair", "LongHairBob", "LongHairBun", "LongHairCurly", "LongHairCurvy",
                                                "LongHairMiaWallace", "LongHairStraight", "LongHairStraight2", "LongHairStraightStrand",
                                                "WinterHat1", "WinterHat2", "WinterHat3", "WinterHat4"};
    private static String[] maleTopTypes = {"NoHair", "LongHairDreads", "ShortHairDreads01", "ShortHairDreads02", "ShortHairFrizzle",
                                            "ShortHairShaggyMullet", "ShortHairShortCurly", "ShortHairShortFlat", "ShortHairShortRound",
                                            "ShortHairTheCaesar", "ShortHairTheCaesarSidePart", "WinterHat1", "WinterHat2", "WinterHat3",
                                            "WinterHat4"};
    private static String[] facialHairTypes = {"Blank", "BeardMedium", "BeardLight", "BeardMajestic", "MoustacheFancy", "MoustacheMagnum"};
    private static String[] unhappyMouths = {"Disbelief", "Concerned", "Sad", "ScreamOpen", "Serious"};
    private static String[] unhappyEyebrows = {"Angry", "SadConcerned","AngryNatural", "SadConcernedNatural"};
    private static String[] unhappyEyes = {"Cry", "EyeRoll", "Side"};
    private static String[] happyMouths = {"Default", "Smile", "Twinkle", "Tongue", "Grimace"};
    private static String[] happyEyebrows = {"Default", "RaisedExcited", "RaisedExcitedNatural", "UpDown", "UpDownNatural"};
    private static String[] happyEyes = {"Happy", "Wink", "WinkWacky", "Default", "Squint"};

    public AvatarModel() {
        accessory = "";
        clothesColor = "";
        clothes = "";
        eyes = "";
        eyebrows = "";
        facialHairColor = "";
        facialHair = "";
        hairColor = "";
        hatColor = "";
        mouth = "";
        skinColor = "";
        top = "";
        code = "";
    }

    /**
     * Generates a student avatar based on whether they are male or female.
     * Eyes, eyebrows and mouth are generated in separate functions based on happiness.
     * @param isFemale determines if the student is male or female
     */
    public void generateStudentAvatar(boolean isFemale){
        if(isFemale){
            top = femaleTopTypes[(int)(Math.random() * 100) % femaleTopTypes.length];
        }
        else{
            top = maleTopTypes[(int)(Math.random() * 100) % maleTopTypes.length];
            facialHair = facialHairTypes[(int)(Math.random() * 100) % facialHairTypes.length];
            facialHairColor = hairColors[(int)(Math.random() * 100) % hairColors.length];
        }
        accessory = accessories[(int)(Math.random() * 100) % accessories.length];
        clothes = clothesTypes[(int)(Math.random() * 100) % clothesTypes.length];
        clothesColor = clothesColors[(int)(Math.random() * 100) % clothesColors.length];
        skinColor = skinColors[(int)(Math.random() * 100) % skinColors.length];
        hatColor = clothesColors[(int)(Math.random() * 100) % clothesColors.length];
        hairColor = hairColors[(int)(Math.random() * 100) % hairColors.length];

        StringBuilder str = new StringBuilder();
        for (int numStyles : numStylesPerAttribute.values()) {
            str.append(alphanumeric.charAt((int) (Math.random() * numStyles)));
        }
        code = str.toString();
    }

    public void generateHappyAvatar(){
        eyes = happyEyes[(int)(Math.random() * 100) % happyEyes.length];
        eyebrows = happyEyebrows[(int)(Math.random() * 100) % happyEyebrows.length];
        mouth = happyMouths[(int)(Math.random() * 100) % happyMouths.length];
    }

    public void generateUnhappyAvatar(){
        eyes = unhappyEyes[(int)(Math.random() * 100) % unhappyEyes.length];
        eyebrows = unhappyEyebrows[(int)(Math.random() * 100) % unhappyEyebrows.length];
        mouth = unhappyMouths[(int)(Math.random() * 100) % unhappyMouths.length];
    }

    public String getEyes() {
        return eyes;
    }

    public String getEyebrows() {
        return eyebrows;
    }

    public String getFacialHairColor() {
        return facialHairColor;
    }

    public String getFacialHair() {
        return facialHair;
    }

    public String getHairColor() {
        return hairColor;
    }

    public String getHatColor() {
        return hatColor;
    }

    public String getMouth() {
        return mouth;
    }

    public String getSkinColor() {
        return skinColor;
    }

    public String getTop() {
        return top;
    }

    public void setMouth(String s){
        mouth = s;
    }

    public void setEyebrows(String s){
        eyebrows = s;
    }

    public void setEyes(String s){
        eyes = s;
    }
}
