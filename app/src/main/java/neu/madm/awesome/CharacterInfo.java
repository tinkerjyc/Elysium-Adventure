package neu.madm.awesome;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterInfo {
    private String name;
    private String gender;
    private Map<String, List<String>> skills;

    public CharacterInfo() {
        this.name = "new character";
        this.gender = "male";
        this.skills = null;
    }


    public void setSkills(Map<String, List<String>> skills) {
        this.skills = new HashMap<>(skills);
    }


    public Map<String, List<String>> getSkills() {
        return new HashMap<>(this.skills);
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getName() {
        return name;
    }


    public String getGender() {
        return gender;
    }


    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        //return String.format("Name: %s\n Gender: %s\n" ,name, gender);
       // return String.format("Name: %s\n Gender: %s\n Skills:\n Intellect: %s " +
        //                "\n Motorics: %s\n Psyche: %s\n Physique: %s\n" ,
         //       name, gender,intellect.toString(),motorics.toString(),
          //      psyche.toString(),physique.toString());

        return String.format(" Name: %s\n Gender: %s\n Skills:\n Intellect: %s " +
                        "\n Motorics: %s\n Psyche: %s\n Physique: %s\n" ,
                name, gender,skills.get("Intellect"),skills.get("Motorics").toString(),
                skills.get("Psyche").toString(),skills.get("Physique").toString());
    }
}
