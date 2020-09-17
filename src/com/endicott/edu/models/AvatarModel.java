package com.endicott.edu.models;

import java.lang.Math;
import java.util.ArrayList;

public class AvatarModel {
    //private String url = "https://avataaars.io/?accessoriesType=Round&avatarStyle=Circle&clotheColor=PastelRed&clotheType=BlazerSweater&eyeType=Default&eyebrowType=RaisedExcited&facialHairColor=Black&facialHairType=MoustacheFancy&hairColor=SilverGray&hatColor=Red&mouthType=Default&skinColor=Pale&topType=Eyepatch";
    private static ArrayList<String> randomAvatars;

    public AvatarModel(){
        randomAvatars = new ArrayList<>();
        randomAvatars.add("https://avataaars.io/?avatarStyle=Circle&topType=LongHairShavedSides&accessoriesType=Wayfarers&facialHairType=Blank&clotheType=CollarSweater&clotheColor=Heather&eyeType=Surprised&eyebrowType=DefaultNatural&mouthType=Serious&skinColor=Brown");
        randomAvatars.add("https://avataaars.io/?accessoriesType=Round&avatarStyle=Circle&clotheColor=PastelRed&clotheType=BlazerSweater&eyeType=Default&eyebrowType=RaisedExcited&facialHairColor=Black&facialHairType=MoustacheFancy&hairColor=SilverGray&hatColor=Red&mouthType=Default&skinColor=Pale&topType=Eyepatch");
        randomAvatars.add("https://avataaars.io/?avatarStyle=Circle&topType=ShortHairShortCurly&accessoriesType=Sunglasses&hairColor=BlondeGolden&facialHairType=BeardLight&facialHairColor=Auburn&clotheType=GraphicShirt&clotheColor=Blue03&graphicType=Hola&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Pale");
        randomAvatars.add("https://avataaars.io/?avatarStyle=Circle&topType=LongHairStraight&accessoriesType=Wayfarers&hairColor=BlondeGolden&facialHairType=Blank&facialHairColor=Brown&clotheType=Hoodie&clotheColor=Blue01&eyeType=Wink&eyebrowType=AngryNatural&mouthType=Serious&skinColor=Light");
        randomAvatars.add("https://avataaars.io/?avatarStyle=Circle&topType=LongHairFroBand&accessoriesType=Kurt&hairColor=Brown&facialHairType=Blank&facialHairColor=Auburn&clotheType=BlazerSweater&eyeType=Side&eyebrowType=RaisedExcitedNatural&mouthType=Twinkle&skinColor=Light");
        randomAvatars.add("https://avataaars.io/?avatarStyle=Circle&topType=WinterHat4&accessoriesType=Prescription01&hatColor=Heather&hairColor=Brown&facialHairType=MoustacheMagnum&facialHairColor=Red&clotheType=Overall&clotheColor=PastelRed&eyeType=EyeRoll&eyebrowType=UpDownNatural&mouthType=Grimace&skinColor=Brown");
        randomAvatars.add("https://avataaars.io/?avatarStyle=Circle&topType=LongHairBigHair&accessoriesType=Sunglasses&hairColor=Brown&facialHairType=Blank&facialHairColor=Brown&clotheType=Overall&clotheColor=Heather&eyeType=Cry&eyebrowType=AngryNatural&mouthType=Grimace&skinColor=Brown");
    }

    public ArrayList<String> getRandomAvatars(){
        return randomAvatars;
    }

    /**
     * Generates a random number from 0-6 to get one of the random avatar images from
     * the array list
     * @return the index of the array list
     */
    public static String getRandomAvatar(){
        int randNum = (int)(Math.random() * 100) % 7;
        return randomAvatars.get(randNum);
    }
}
