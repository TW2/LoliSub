package loli.enumeration;

import loli.Sub;

public enum AssActorType {
    Male(Sub.L.getString("actor.male")),
    Female(Sub.L.getString("actor.female")),
    Robot(Sub.L.getString("actor.robot")),
    Narrator(Sub.L.getString("actor.narrator")),
    Unknown(Sub.L.getString("actor.default"));

    final String name;

    private AssActorType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static AssActorType get(String s){
        AssActorType kind = AssActorType.Unknown;

        for(AssActorType k : AssActorType.values()){
            if(s.equalsIgnoreCase(k.getName())){
                kind = k;
                break;
            }
        }

        return kind;
    }
}
