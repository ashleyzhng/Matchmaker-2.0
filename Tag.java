/*
   File Name: Tag.java
   Name: Joyce Lin
   Class: ICS4U1
   Date: June 2026

   Description:
   The Tag object class represents ther different interests
*/
//constructor
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
    
    //constructor
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
    //---accessors and mutators as necessary---
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
    /* updates the tags to increase interest in a certain interest
    * @param int all the differetn interests represented as an integer
    */
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

    /* compares the similarities between two tags
    * @param other the other Tag object hat is being compared to
    * @return double a percentage of how close two tags are to eachotehr
    */
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

    /* compares the similarities between 2 different tags, a helper method to compareTo()
    * @param int the 2 interests that are being compared to
    * @return double a percentage of how close two interests are to eachotehr
    */
    public double compareCategory(int tag1, int tag2){
        if (tag1 == 0 && tag2 == 0){
            return 1.0;
        }
        double larger = Math.max(tag1, tag2);
        double smaller = Math.min(tag1, tag2);
        return smaller/larger;
    }
}
