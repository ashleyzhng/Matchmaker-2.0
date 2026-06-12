/**
	File Name: Tag.java
	Name: Joyce Lin
	Class: ICS4U1
	Date: June 2, 2026
	Description: This program represents a Tag object
*/
public class Tag {
    public static final int NUM_TAGS = 10;
    private int religion;
    private int sports;
    private int music;
    private int art;
    private int traveling;
    private int fitness;
    private int gaming;
    private int cooking;
    private int animal;
    private int reading;
    
    /**
    * Sets 
    *
    * @param String  the card holder's name	
    */ 
    public Tag(int r, int s, int m, int a, int t, int f, int g, int c, int an, int re){
        religion = r;
        sports = s;
        music = m;
        art = a;
        traveling = t;
        fitness = f;
        gaming = g;
        cooking = c;
        animal = an;
        reading = re;
    }

    public int getReligion(){
        return religion;
    }

    public void setReligion(int r){
        religion = r;
    }

    public int getSports(){
        return sports;
    }

    public void setSports(int s){
        sports = s;
    }

    public int getMusic(){
        return music;
    }

    public void setMusic(int m){
        music = m;
    }

    public int getArt(){
        return art;
    }

    public void setArt(int a){
        art = a;
    }

    public int getTraveling(){
        return traveling;
    }

    public void setTraveling(int t){
        traveling = t;
    }

    public int getFitness(){
        return fitness;
    }

    public void setFitness(int f){
        fitness = f;
    }

    public int getGaming(){
        return gaming;
    }

    public void setGaming(int g){
        gaming = g;
    }

    public int getCooking(){
        return cooking;
    }

    public void setCooking(int c){
        cooking = c;
    }

    public int getAnimal(){
        return animal;
    }

    public void setAnimal(int an){
        animal = an;
    }

    public int getReading(){
        return reading;
    }

    public void setReading(int re){
        reading = re;
    }

    public void updateTag(int r, int s, int m, int a, int t, int f, int g, int c, int an, int re){
        religion += r;
        sports += s;
        music += m;
        art += a;
        traveling += t;
        fitness += f;
        gaming += g;
        cooking += c;
        animal += an;
        reading += re;
    }

    public double compareTo(Tag other){
        double total = 0;
        total += compareCategory(religion, other.religion);
        total += compareCategory(sports, other.sports);
        total += compareCategory(music, other.music);
        total += compareCategory(art, other.art);
        total += compareCategory(traveling, other.traveling);
        total += compareCategory(fitness, other.fitness);
        total += compareCategory(gaming, other.gaming);
        total += compareCategory(cooking, other.cooking);
        total += compareCategory(animal, other.animal);
        total += compareCategory(reading, other.reading);

        return (total/NUM_TAGS) * 100.0;
    }

    public double compareCategory(int tag1, int tag2){
        if (tag1 == 0 && tag2 == 0){
            return 1.0;
        }
        double larger = Math.max(tag1, tag2);
        double smaller = Math.min(tag1, tag2);
        return smaller/larger;
    }
}
