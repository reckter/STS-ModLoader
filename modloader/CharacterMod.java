package com.modloader;

import java.util.ArrayList;
import java.util.List;

public class CharacterMod {
    List<String> addCards;
    List<String> removeCards;
    List<String> addRelics;
    List<String> removeRelics;
    
    public CharacterMod() {
        this.addCards = new ArrayList<String>();
        this.removeCards = new ArrayList<String>();
        this.addRelics = new ArrayList<String>();
        this.removeRelics = new ArrayList<String>();
    }
}