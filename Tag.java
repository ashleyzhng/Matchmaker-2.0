public class Tag {
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
}
